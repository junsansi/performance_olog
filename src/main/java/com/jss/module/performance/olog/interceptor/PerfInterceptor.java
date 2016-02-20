package com.jss.module.performance.olog.interceptor;

import java.util.concurrent.ConcurrentHashMap;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.jss.module.performance.olog.domain.MethodStats;
import com.jss.module.performance.olog.kafka.KafkaProducer;
import com.jss.module.performance.olog.redis.JedisUtil;
import com.jss.module.performance.olog.utils.IpAddrUtil;

/**
 * 基于AOP拦截指定类型的方法，基于SLF4J输出类包、方法名及方法执行时间
 * 
 * @author junsansi
 *
 */
public class PerfInterceptor implements MethodInterceptor {

	/**
	 * performance.log.open 指定拦截器状态 1表示启用，0表示禁用 当不指定时，默认为启用状态
	 */
	@Value("${performance.log.open:1}") // 默认为启用状态
	private long isOpen;

	/**
	 * performance.log.frequency 指定对方法的拦截频率，应为大于0的正整数 默认值为1,表示每次均会拦截
	 */
	@Value("${performance.log.frequency:1}") // 默认为每次均进行拦截
	private long frequency;

	/**
	 * performance.log.longquerytime 指定方法执行时间将做为慢查询方法的时间阀值，以ms为单位，默认是500ms
	 */
	@Value("${performance.log.longquerytime:50}")
	private long longquerytime;

	/**
	 * performance.log.destination
	 * 指定日志文件输出路径，目前支持logfile/redis/kafka等数种，默认为logfile
	 */
	@Value("${performance.log.destination:logfile}")
	private String fileDest;

	/**
	 * 指定日志输出主题或键名，默认值为perfolog
	 */
	@Value("${performance.log.topic:perfolog}")
	private String topic;

	/**
	 * 指定kafka的连接地址，当broker为空时，destination默认值为logfile
	 */
	@Value("${performance.log.kafka.broker}")
	private String kafkabroker;

	/**
	 * 指定redis的连接地址和端口，可指定多个地址，格式为ip1:port1,ip2:port2:ip3:port3.....
	 */
	@Value("${performance.log.redis.hosturi}")
	private String redisHosts;
	
	/**
	 * kafka工具类
	 */
	private static KafkaProducer kafkaProducer;
	
	/**
	 * 获取kafka工具类实例
	 */
	private KafkaProducer getKafkaProducer(){
		if (kafkaProducer == null){
			kafkaProducer = new KafkaProducer(topic, kafkabroker);
		}
		return kafkaProducer;
	}
	
	/**
	 * redis工具类
	 */
	private static  JedisUtil jedisUtil;
	
	/**
	 * 获取redis工具类实例
	 */
	private JedisUtil getJedisUtil(){
		if (jedisUtil == null){
			jedisUtil = new JedisUtil(redisHosts);
		}
		return jedisUtil;
	}
	
	/**
	 * 被调用方法缓存区
	 */
	private ConcurrentHashMap<String, MethodStats> methodStats = new ConcurrentHashMap<String, MethodStats>();

	private static final Logger logger = LoggerFactory.getLogger(PerfInterceptor.class);
	
	//获取本地IP地址
	private static String localIpAddr;
	private String getLocalIpAddr(){
		if (localIpAddr == null){
			localIpAddr =  IpAddrUtil.getLocalIp();
		}
		return localIpAddr;
	}

	public Object invoke(MethodInvocation method) throws Throwable {
		//被拦截方法开始时间
		long methodStarttime = System.currentTimeMillis();
		
		try {
			return method.proceed();
		} finally {
			if (isOpen == 1) {
				updateStats(method,  methodStarttime);
			}
		}
	}

	private void updateStats(MethodInvocation method, long methodStarttime) {

		long methodElapsedTime = System.currentTimeMillis() - methodStarttime;
		
		String methodClass = method.getMethod().getDeclaringClass().getName();
		String methodName = method.getMethod().getName();
		String methodFullname = methodClass + "." + methodName;
		
		MethodStats stats = methodStats.get(methodFullname);
		if (stats == null) {
			
			stats = new MethodStats(methodFullname);
			stats.setExecutedNum(1);
			stats.setMethodElapsedTime(methodElapsedTime);
			stats.setMaxTime(methodElapsedTime);
			stats.setTotalTime(methodElapsedTime);
			stats.setMethodName(methodName);
			stats.setLocalIpAddr(this.getLocalIpAddr());
			//stats.setRemoteIpAddr(remoteIpAddr);   //目前考虑到性能，暂不记录该属性，如有必要，可以通过request获取来访IP地址
			
			methodStats.put(methodFullname, stats);
		}else{
			stats.setExecutedNum(stats.getExecutedNum()+1);
			if (stats.getMaxTime() < methodElapsedTime) stats.setMaxTime(methodElapsedTime);
			stats.setMethodElapsedTime(methodElapsedTime);
			stats.setTotalTime(stats.getTotalTime()+methodElapsedTime);
		}
		
		String message = stats.toString();
		
		if (StringUtils.isBlank(kafkabroker) && StringUtils.isBlank(redisHosts)) {
			fileDest = "logfile";
		}
		// 检查执行频率条件是否满足
		if (stats.getExecutedNum() % frequency == 0) {

			// 检查执行时间条件是否满足
			if (methodElapsedTime >= longquerytime) {
				switch (fileDest) {
				// 输出到kafka
				case "kafka":
					KafkaProducer kp = this.getKafkaProducer();
					kp.sendMsg(message);
					break;
				// 输出到redis
				case "redis":
					JedisUtil jedis = this.getJedisUtil();
					jedis.lpush(topic, message);
					break;
				// 默认输出到日志文件
				default:
					logger.info(message);
					break;
				}

			}

		}
		//拦截器的执行时间
		/*
		long interceptorElapsedTime = System.currentTimeMillis() - methodStarttime - methodElapsedTime;
		stats.setInterceptorElapsedTime(interceptorElapsedTime);
		logger.info(stats.toString()+"  interceptorExecuted time: "+stats.getInterceptorElapsedTime()+" ms.");
		*/
	}
	
	public void destroy(){
		try{
			this.getJedisUtil().close();
		}catch(Exception ex){
			logger.error("Destroy jedis error", ex);
		}
		
		try{
			this.getKafkaProducer().close();
		}catch(Exception ex){
			logger.error("Destroy kafkaProducer error", ex);
		}
		
	}
	
}
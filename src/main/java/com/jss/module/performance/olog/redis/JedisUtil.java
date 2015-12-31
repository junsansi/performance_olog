package com.jss.module.performance.olog.redis;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class JedisUtil {
	private static final Logger logger = LoggerFactory.getLogger(JedisUtil.class);

    //未分片客户端连接池  
    //private JedisPool jedisPool;  
    //分片客户端连接池  
    private ShardedJedisPool shardedJedisPool;  
	
  //初始化集群资源池
	public JedisUtil(String hosturi){
		JedisPoolConfig config = this.initJedisPoolConfig();
		//int timeout = 60000;
		//jedisPool = new JedisPool(config, jedisIp, jedisPort, timeout);
	    List<JedisShardInfo> shards = getJedisAddrs(hosturi, 6379); 
	    shardedJedisPool = new ShardedJedisPool(config,shards);  
	}

	//jedispool配置 
	public JedisPoolConfig initJedisPoolConfig() {  
	     JedisPoolConfig config = new JedisPoolConfig();  
        config.setMaxTotal(100);  
        config.setMaxIdle(256);  
        config.setMaxWaitMillis(5000L);  
        config.setTestOnBorrow(true);  
        config.setTestOnReturn(true);  
		config.setTestWhileIdle(true);
		config.setMinEvictableIdleTimeMillis(60000l);
		config.setTimeBetweenEvictionRunsMillis(3000l);
		config.setNumTestsPerEvictionRun(-1);
		return config;
    }  
		
	public void close() {
		shardedJedisPool.close();
		logger.debug("Close jedis resource!");
	}

	public ShardedJedis getJedis(){
		ShardedJedis jedis = shardedJedisPool.getResource();
		return jedis;
	}
	
	public void lpush(String key, String value) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		try {
			jedis.lpush(key.getBytes(), ObjectUtil.objectToBytes(value));
		} catch (Exception e) {
			logger.error("Met some error"+e);
		} finally{
			shardedJedisPool.returnResourceObject(jedis);
		}

	}

	public byte[] rpop(String key) {
		ShardedJedis jedis = shardedJedisPool.getResource();
		byte[] bytes = null;
		try{
			bytes = jedis.rpop(key.getBytes());			
		}finally{
			shardedJedisPool.returnResourceObject(jedis);
		}
		return bytes;

	}

	/**
	 * 将字符串转换为JedisShardInfo的数组
	 * @param hosturi，格式为ip1:port1,ip2:port2:ip3:port3.....
	 * @return
	 */
	public static List<JedisShardInfo> getJedisAddrs(String hosturi, int defaultPort){
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();  
		String[] hosts =  hosturi.split(",");
		if (hosts.length<1){
			//throw new Exception("The pattern of hostUri was incorrect. Please check the parameter and run again. ");
			logger.error("The pattern of hostUri was incorrect. Please check the parameter and run again. ");
		}else{
			int timeout = 60000;
			for (int i=0;i<hosts.length;i++){
				
				String[] host = hosts[i].split(":");
				if (host.length == 1){
					//指定默认端口
					shards.add(new JedisShardInfo(host[0],defaultPort, timeout));
				}else{
					shards.add(new JedisShardInfo(host[0],Integer.parseInt(host[1]), timeout));
				}
			}
		}
		return shards;
	}
	
}

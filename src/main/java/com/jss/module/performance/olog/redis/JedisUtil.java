package com.jss.module.performance.olog.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil {
	private static final Logger logger = LoggerFactory.getLogger(JedisUtil.class);
	private JedisPool jedisPool;
	private Jedis jedis;

	public JedisUtil(String jedisIp, int jedisPort) {
		JedisPoolConfig config = new JedisPoolConfig();
		// config.setMaxActive(5000);
		config.setMaxIdle(256);// 20
		// config.setMaxWait(5000L);
		config.setTestOnBorrow(true);
		config.setTestOnReturn(true);
		config.setTestWhileIdle(true);
		config.setMinEvictableIdleTimeMillis(60000l);
		config.setTimeBetweenEvictionRunsMillis(3000l);
		config.setNumTestsPerEvictionRun(-1);
		int timeout = 60000;
		jedisPool = new JedisPool(config, jedisIp, jedisPort, timeout);
		if (jedisPool != null) {
			jedis = jedisPool.getResource();
			logger.debug("Init jedis resource!");
		}
	}

	public void close() {
		jedis.close();
		jedisPool.close();
		logger.debug("Close jedis resource!");
	}

	public void lpush(String key, String value) {
		try {
			jedis.lpush(key.getBytes(), ObjectUtil.objectToBytes(value));
		} catch (Exception e) {
			logger.error("Met some error"+e);
			e.printStackTrace();
		}

	}

	public byte[] rpop(String key) {

		byte[] bytes = null;
		bytes = jedis.rpop(key.getBytes());
		return bytes;

	}

}

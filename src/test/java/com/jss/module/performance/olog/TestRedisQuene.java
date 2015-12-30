package com.jss.module.performance.olog;

import org.junit.Test;

import com.jss.module.performance.olog.redis.JedisUtil;
import com.jss.module.performance.olog.redis.ObjectUtil;

public class TestRedisQuene {
	private String redisKey = "perfolog";
	private JedisUtil ju = new JedisUtil("172.16.129.134", 6379);
	
	public void pushMessage() throws Exception{
		
		ju.lpush(redisKey, "内容1");
		ju.lpush(redisKey, "内容2");
		ju.lpush(redisKey, "内容3");
	}
	
	public void popMessage() throws Exception {
		while (true){
			byte[] bytes = ju.rpop(redisKey);
			if (bytes != null){
				String msg = (String) ObjectUtil.bytesToObject(bytes);
				if(msg != null){
					System.out.println(msg);
				}			
			}else{
				break;
			}
		}
	}
	
	@Test
	public void doPushPop() throws Exception{
		this.pushMessage();
		this.popMessage();
	}
	
	
}

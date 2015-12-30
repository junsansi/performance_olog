package com.jss.module.performance.olog;

import org.junit.Test;

import com.jss.module.performance.olog.kafka.KafkaProducer;

public class KafkaProducerTest {

	@Test
	public void saveMessage(){
		String message="hahaha";
		String topic="perfolog";
		String broker = "172.16.129.134:9092";
		KafkaProducer kp = new KafkaProducer(topic, broker);
		System.out.println(kp.sendMsg(message));
		
	}
	
}

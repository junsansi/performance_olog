package com.jss.module.performance.olog.kafka;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 消息发送端实现
 * @author kevin
 *
 */
public class KafkaProducer {
	private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
	private Producer<Integer, String> producer = null;
	private String topic;
	
	public KafkaProducer(String topic, String broker) {
		Properties props = new Properties();
		props.put("serializer.class", "com.jss.module.performance.olog.kafka.KeywordMessageEncoder");
		props.put("partitioner.class", "com.jss.module.performance.olog.kafka.TopicPartitioner");
		props.put("metadata.broker.list", broker);
		//props.put("producer.type", "async");
		//props.put("message.send.max.retries", "2");
		//props.put("retry.backoff.ms", "300");
		//0当queue满时丢掉
		//props.put("queue.enqueue.timeout.ms", "0");
		
		ProducerConfig config = new ProducerConfig(props);
		producer = new Producer<Integer, String>(config);
		this.topic = topic;
	}

	public boolean sendMsg(String message) {
		boolean sendFlag = false;
		if (message == null || "".equals(message)) {
			return sendFlag;
		}

		if (topic == null || "".equals(topic)) {
			logger.warn("未找到对应的topic!");
			return sendFlag;
		}
		try {
			KeyedMessage<Integer, String> data = new KeyedMessage<Integer, String>(topic,  message.hashCode(), message);
			producer.send(data);
			
			sendFlag =  true;
		} catch (Exception ex) {
			logger.error("发送消息失败", ex);
		}
		//System.out.println(sendFlag);
		return sendFlag;
	}
	
	public void close(){
		producer.close();
	}
	
}

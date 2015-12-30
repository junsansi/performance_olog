package com.jss.module.performance.olog.kafka;

import java.io.UnsupportedEncodingException;

import kafka.utils.VerifiableProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义消息编码器
 * 
 * @author kevin
 *
 */
public class KeywordMessageEncoder implements kafka.serializer.Encoder<Object> {
	static Logger logger = LoggerFactory.getLogger(KeywordMessageEncoder.class);

	public KeywordMessageEncoder(VerifiableProperties props) {

	}

	public byte[] toBytes(Object obj) {
		byte[] bytes = null;
		try {
			if (obj instanceof String) {
				bytes = ((String) obj).getBytes("utf-8");
			} else if (obj instanceof byte[]) {
				bytes = (byte[]) obj;
			}

		} catch (UnsupportedEncodingException e) {
			logger.error("KeywordMessageEncoder tobytes error {}",e);
		}

		return bytes;
	}

}

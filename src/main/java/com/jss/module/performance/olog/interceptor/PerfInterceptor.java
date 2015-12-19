package com.jss.module.performance.olog.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 拦截指定类型的方法，基于SLF4J输出类包、方法名及方法执行时间
 * @author junsansi
 *
 */
public class PerfInterceptor implements MethodInterceptor {
	
	private static final Logger logger = LoggerFactory.getLogger(PerfInterceptor.class);
  
   public Object invoke(MethodInvocation method) throws Throwable {
       long start = System.currentTimeMillis();
       
       try {
           return method.proceed();
       }
       finally {
           updateStats(method.getMethod().getDeclaringClass().getName(), method.getMethod().getName(),(System.currentTimeMillis() - start));
       }
   }

   private void updateStats(String methodClass, String methodName, long elapsedTime) {
       
       logger.info("Method: "+methodClass+"."  +methodName + "(), executed time: " + elapsedTime);

   }
}
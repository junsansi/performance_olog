package com.jss.module.performance.olog.domain;


/**
 * 被调用方法基本属性类
 * @author junsansi
 *
 */
public class MethodStats {
	//完整方法名（含包名）
	public String methodFullname;
	//方法名
    public String methodName;
    //拦截方法执行次数
    public long executedNum;
	//拦截方法总执行时间
    public long totalTime;
    //拦截方法本次执行时间
    public long methodElapsedTime;
    //拦截器本次执行时间
    public long interceptorElapsedTime;
    //拦截方法最大执行时间
    public long maxTime;
    
    public MethodStats(String methodFullname) {
        this.methodFullname = methodFullname;
    }
    
    public MethodStats(String methodFullname, String methodName) {
    	this.methodFullname = methodFullname;
        this.methodName = methodName;
    }

	public String getMethodFullname() {
		return methodFullname;
	}

	public void setMethodFullname(String methodFullname) {
		this.methodFullname = methodFullname;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
    public long getExecutedNum() {
		return executedNum;
	}

	public void setExecutedNum(long executedNum) {
		this.executedNum = executedNum;
	}

	public long getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}

	public long getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(long maxTime) {
		this.maxTime = maxTime;
	}

	public long getMethodElapsedTime() {
		return methodElapsedTime;
	}

	public void setMethodElapsedTime(long methodElapsedTime) {
		this.methodElapsedTime = methodElapsedTime;
	}

	public long getInterceptorElapsedTime() {
		return interceptorElapsedTime;
	}

	public void setInterceptorElapsedTime(long interceptorElapsedTime) {
		this.interceptorElapsedTime = interceptorElapsedTime;
	}

	@Override
	public String toString(){
		return  "Method: " + this.methodFullname + "(), methodExecuted time: " + this.methodElapsedTime + " ms.";
		
	}
	
}


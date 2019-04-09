package com.dcits.comet.mq.api.exception;

/**
 * AMQ异常类
 *
 */
public class MQException extends Exception {
	private static final long serialVersionUID = -7934628351809597349L;
	/**
	 * 错误编码
	 */
	private String errorCode;

	public MQException() {
		super();
	}

	public MQException(String msg) {
		super(msg);
	}

	public MQException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public MQException(Throwable cause) {
		super(cause);
	}
	
	public MQException(String errorCode, String msg) {
		super(msg);
		this.errorCode = errorCode;
	}

	public MQException(String errorCode, String msg, Throwable cause) {
		super(msg, cause);
		this.errorCode = errorCode;
	}
	
	/**
	 * 获取错误编码
	 * 
	 * @return
	 */
	public String getErrorCode() {
		return this.errorCode;
	}

	/**
	 * 设置错误编码
	 * 
	 * @param errorCode
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}

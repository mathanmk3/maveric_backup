package com.citi.train.exception;

public class ServiceException extends RuntimeException {

	private final String message;
	private StackTraceElement[] stackTrace;
	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}

	public ServiceException(String message) {
		super();
		this.message = message;
	}

	

}

package com.claritrics.service.exceptions;

/**
 * @author Shanmugam
 * 
 */
public class ServiceException extends Exception {
	private static final long serialVersionUID = 1001L;
	private String code;
	private String message;
	private StackTraceElement[] stackTrace;
	private String exceptionCaughtMessage;

	public ServiceException(String message) {
		super(message);
		this.message = message;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the stackTrace
	 */
	public StackTraceElement[] getStackTrace() {
		return stackTrace;
	}

	/**
	 * @param stackTrace
	 *            the stackTrace to set
	 */
	public void setStackTrace(StackTraceElement[] stackTrace) {
		this.stackTrace = stackTrace;
	}

	public ServiceException(final String code, final String message) {
		this.code = code;
		this.message = message;
	}

	public ServiceException(final String code, final String message,
			final StackTraceElement[] stackTrace) {
		this.code = code;
		this.message = message;
		this.stackTrace = stackTrace;
	}

	public ServiceException(final String code, final String message,
			final StackTraceElement[] stackTrace,
			final String exceptionCaughtMessage) {
		this.code = code;
		this.message = message;
		this.stackTrace = stackTrace;
		this.setExceptionCaughtMessage(exceptionCaughtMessage);
	}

	public ServiceException(final String message,
			final StackTraceElement[] stackTrace) {
		this.message = message;
		this.stackTrace = stackTrace;
	}

	public ServiceException(final Exception exception) {
		this.message = exception.getMessage();
		this.stackTrace = exception.getStackTrace();
	}

	public String getExceptionCaughtMessage() {
		return exceptionCaughtMessage;
	}

	public void setExceptionCaughtMessage(String exceptionCaughtMessage) {
		this.exceptionCaughtMessage = exceptionCaughtMessage;
	}
}

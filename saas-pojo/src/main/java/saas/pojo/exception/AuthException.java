package saas.pojo.exception;

import saas.pojo.enums.ErrorCodeEnum;

/**
 * 业务异常.
 *
 *
 */
public class AuthException extends RuntimeException {

	/**
	 * 异常码
	 */
	protected int code;

	private static final long serialVersionUID = 3160241586346324994L;

	public AuthException() {
		super("UNAUTHORIZED");
		this.code = 401;
	}

	public AuthException(Throwable cause) {
		super(cause);
	}

	public AuthException(String message) {
		super(message);
	}

	public AuthException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthException(int code, String message) {
		super(message);
		this.code = code;
	}

	public AuthException(int code, String msgFormat, Object... args) {
		super(String.format(msgFormat, args));
		this.code = code;
	}

	public AuthException(ErrorCodeEnum codeEnum, Object... args) {
		super(String.format(codeEnum.msg(), args));
		this.code = codeEnum.code();
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}

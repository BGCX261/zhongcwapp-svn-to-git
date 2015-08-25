package com.ufida.gkb.util;

public class Result {

	private static Result instance = new Result();

	private boolean success;

	private String msg;

	private Object obj;

	public boolean isSuccess() {
		return success;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static Result success(String msg) {
		instance.success = true;
		instance.msg = msg;
		instance.obj = null;
		return instance;
	}

	public static Result fail(String msg) {
		instance.success = false;
		instance.msg = msg;
		instance.obj = null;
		return instance;
	}
}

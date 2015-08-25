package com.ufida.gkb.util;

public class NetworkException extends Exception {
	private String msg;

	public NetworkException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}

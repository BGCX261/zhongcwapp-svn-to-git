package com.zhongcw.android.service;

import com.zhongcw.android.util.Network;

public class LoginService {
	public Object login(String username, String password) {
		Network.getInstance().get("/login.do?username=jerry&relogin=true");
		return null;
	}
}

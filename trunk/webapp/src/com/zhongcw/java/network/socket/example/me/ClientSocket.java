package com.zhongcw.java.network.socket.example.me;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSocket {

	public static void main(String[] args) throws IOException {
		Socket clientSocket = null;
		BufferedReader in = null;
		try {
			clientSocket = new Socket("127.0.0.1", 4444);
			in = new BufferedReader(new InputStreamReader(clientSocket
					.getInputStream()));

		} catch (Exception e) {
			e.printStackTrace();
		}

		String fromServer;
		while ((fromServer = in.readLine()) != null) {
			System.out.println(fromServer);
		}
		in.close();
		clientSocket.close();
	}

}

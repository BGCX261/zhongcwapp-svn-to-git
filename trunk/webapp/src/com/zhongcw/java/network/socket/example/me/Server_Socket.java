package com.zhongcw.java.network.socket.example.me;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server_Socket {

	public static void main(String[] args) throws Exception {
		ServerSocket serverSocket = null;
		Socket clientSocket = null;
		BufferedWriter out = null;
		serverSocket = new ServerSocket(9876);
		while (true) {
			clientSocket = serverSocket.accept();
			out = new BufferedWriter(new OutputStreamWriter(clientSocket
					.getOutputStream()));
			out.write("abc");
			out.close();
			if (false) {
				break;
			}
		}
		clientSocket.close();
		serverSocket.close();
	}
}

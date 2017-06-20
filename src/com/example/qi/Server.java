package com.example.qi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * @author Crunchify.com
 *
 */

public class Server {

	private ServerSocket serverSocket;
	private HashMap<String, Thread> messageHandlers = new HashMap<>();

	public static void main(String[] args) throws IOException {
		new Server().start();
	}

	private void start() {
		try {
			serverSocket = new ServerSocket(4444);
			System.out.println("服务器已启动。。。。。。");
			while (true) {
				Socket accept = serverSocket.accept();
				InetAddress inetAddress = accept.getInetAddress();
				String hostName = inetAddress.getHostName();
				if (messageHandlers.get(hostName) == null) {
					messageHandlers.put(hostName, new Thread(new MessageHandler(accept)));
					messageHandlers.get(hostName).start();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class MessageHandler implements Runnable {

		private Socket socket;

		public MessageHandler(Socket client) {
			this.socket = client;
		}

		@Override
		public void run() {
			BufferedReader in = null;
			PrintWriter out = null;
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
				while (true) {
					String result;
					// 通过BufferedReader读取一行
					// 如果已经读到输入流尾部，返回null,退出循环
					// 如果得到非空值，就尝试计算结果并返回
					if ((result = in.readLine()) == null)
						break;
					System.out.println("服务器收到消息：" + result);
					out.println("Server : " + result);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				// 一些必要的清理工作
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					in = null;
				}
				if (out != null) {
					out.close();
					out = null;
				}
				if (socket != null) {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					socket = null;
				}
			}

		}

	}
}
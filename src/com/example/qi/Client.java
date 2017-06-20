package com.example.qi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Crunchify.com
 *
 */

public class Client {

	public static void main(String[] args) throws IOException, InterruptedException {
		new Client().start();
	}

	private void start() {
		Socket socket = null;
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			socket = new Socket("127.0.0.1", 4444);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			while (true) {
				Scanner scanner = new Scanner(System.in);
				out.println(scanner.next());
				System.out.println("___结果为：" + in.readLine());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 一下必要的清理工作
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
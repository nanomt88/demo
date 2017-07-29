package com.nanomt88.demo.curator.cluster;



public class Client1 {

	public static void main(String[] args) throws Exception{
		
		CuratorWatcher watcher = new CuratorWatcher();
		System.out.println("client1 started...");
		Thread.sleep(100000000);
	}
}

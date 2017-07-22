package com.nanomt88.demo.zkclient.watcher;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

/**
 * zkClient 客户端 使用 watch demo。
 *
 *      注意：
 *          只监听节点的新增和删除，节点的内容变化不会触发
 *          删除的时候，是递归删除，如果有子节点 会触发两次事件
 *
 */
public class ZkClientWatcher1 {

	/** zookeeper地址 */
	static final String CONNECT_ADDR = "192.168.1.130:2181,192.168.1.140:2181,192.168.1.141:2181";
	/** session超时时间 */
	static final int  SESSION_TIMEOUT = 5000;//ms

	public static void main(String[] args) throws Exception {
		ZkClient zkc = new ZkClient(new ZkConnection(CONNECT_ADDR), SESSION_TIMEOUT );
		
		//对父节点添加监听子节点变化。
        //只监听节点的新增和删除，节点的内容变化不会触发
		zkc.subscribeChildChanges("/super", new IZkChildListener() {
			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				System.out.println("parentPath: " + parentPath);
				System.out.println("currentChilds: " + currentChilds);
			}
		});
		
		Thread.sleep(3000);
		
		zkc.createPersistent("/super");
		Thread.sleep(1000);
		
		zkc.createPersistent("/super" + "/" + "c1", "c1内容");
		Thread.sleep(1000);
		
		zkc.createPersistent("/super" + "/" + "c2", "c2内容");
		Thread.sleep(1000);

//        zkc.createPersistent("/super" + "/" + "c2" + "/c22", "c22内容");
//        Thread.sleep(1000);

        zkc.delete("/super/c2");
		Thread.sleep(1000);	

		//在删除之前，/super节点下有一个 c2节点；会递归删除，先删除子节点再删除父节点
        //所以会触发两次事件
		zkc.deleteRecursive("/super");
		Thread.sleep(Integer.MAX_VALUE);
		
		
	}
}

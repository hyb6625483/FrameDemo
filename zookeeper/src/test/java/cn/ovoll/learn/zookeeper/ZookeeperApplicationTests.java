package cn.ovoll.learn.zookeeper;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.List;

@SpringBootTest
class ZookeeperApplicationTests {

	private CuratorFramework client;

	@AfterEach
	public void close() {
		System.out.println("关闭连接");
		if (client != null) {
			client.close();
		}
	}

	/**
	 * 建立连接
	 */
	@Test
	void testConnect() {
		// 重试策略
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(3000, 3);
		// 创建连接对象
		client = CuratorFrameworkFactory.builder()
				.connectString("192.168.31.4:2181")
				.sessionTimeoutMs(60000)
				.connectionTimeoutMs(15000)
				.retryPolicy(retryPolicy)
				.namespace("learn")
				.build();
		// 开启连接
		client.start();
		System.out.println("建立连接");
	}

	/**
	 * 创建节点
	 */
	@Test
	void testCreateNode() throws Exception {
		testConnect();
		// 如果创建节点的时候没有添加数据，默认添加当前客户端的ip地址作为数据
		String path = client.create().forPath("/app1");
		System.out.println(path);
		// 创建带数据的节点
		String data = "测试添加数据";
		String path2 = client.create().forPath("/app2", data.getBytes(StandardCharsets.UTF_8));
		System.out.println(path2);
		// 创建带节点类型的节点，默认持久化节点
		String path3 = client.create().withMode(CreateMode.EPHEMERAL).forPath("/app3");
		System.out.println(path3);
		// 创建多级节点
		String path4 = client.create().creatingParentsIfNeeded().forPath("/app4/p1");
		System.out.println(path4);
	}

	@Test
	void testGetData() throws Exception {
		testConnect();
		// 查询数据
		byte[] bytes = client.getData().forPath("/app1");
		System.out.println(new String(bytes));
		// 查询子节点
		List<String> list = client.getChildren().forPath("/");
		System.out.println(list);
		// 查询节点状态信息
		Stat stat = new Stat();
		client.getData().storingStatIn(stat).forPath("/app1");
		System.out.println(stat);
	}

	@Test
	void testUpdateData() throws Exception {
		testConnect();
		// 修改节点数据
		Stat path = client.setData().forPath("/app1", "测试修改数据".getBytes(StandardCharsets.UTF_8));
		System.out.println(path);
		// 根据版本修改节点数据
		// 1.获取节点版本信息
		Stat stat = new Stat();
		client.getData().storingStatIn(stat).forPath("/app1");
		// 2.修改数据
		Stat result = client.setData().withVersion(stat.getVersion()).forPath("/app1", "测试根据版本修改数据".getBytes(StandardCharsets.UTF_8));
		System.out.println(result);
	}

	@Test
	void testDeleteNode() throws Exception {
		testConnect();
		// 删除节点
		client.delete().forPath("/app1");
		// 删除带有子节点的节点
		client.delete().deletingChildrenIfNeeded().forPath("/app4");
		// 必须删除成功
		client.delete().guaranteed().forPath("/app2");
		// 回调
		client.delete().guaranteed().inBackground((client1, event) -> {
			System.out.println("成功删除节点");
			System.out.println(event);
		}).forPath("/app3");
	}

	@Test
	void testNodeCacheWatcher() {
		testConnect();
		// 创建指定节点的Cache对象
		CuratorCache cache = CuratorCache.builder(client, "/app1").build();
		// 创建监听器
		CuratorCacheListener listener = CuratorCacheListener.builder().forNodeCache(() -> {
			System.out.println("触发节点监听");
		}).build();
		// 添加监听器
		cache.listenable().addListener(listener);
		// 开启监听
		cache.start();
		while (true) {

		}
	}

	@Test
	void testPathChildrenCacheWatcher() {
		testConnect();
		// 创建指定节点的Cache对象
		CuratorCache cache = CuratorCache.builder(client, "/app1").build();
		// 创建监听器
		CuratorCacheListener listener = CuratorCacheListener.builder().forPathChildrenCache("/app1", client, (client, event) -> {
			System.out.println("触发子节点监听");
		}).build();
		// 添加监听器
		cache.listenable().addListener(listener);
		// 开启监听
		cache.start();
		while (true) {

		}
	}

	@Test
	void testTreeCacheWatcher() {
		testConnect();
		// 创建指定节点的Cache对象
		CuratorCache cache = CuratorCache.builder(client, "/app1").build();
		// 创建监听器
		CuratorCacheListener listener = CuratorCacheListener.builder().forTreeCache(client, (client, event) -> {
			System.out.println("触发子节点监听");
		}).build();
		// 添加监听器
		cache.listenable().addListener(listener);
		// 开启监听
		cache.start();
		while (true) {

		}
	}
}

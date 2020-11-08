package yejy.client.cluster;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * @author yejunyang2012@163.com
 * @date 2019/9/6 22:13
 **/
public class OuterClass {
    private static JedisCluster jedisCluster = null;
    private static final int MAX_ACTIVE = 10;
    private static final int MAX_IDLE = 5;
    private static final int MAX_WAIT = 2;
    private static final boolean TEST_ON_BORROW = true;
    public synchronized static JedisCluster getJedisCluster() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(MAX_ACTIVE);
        config.setMaxIdle(MAX_IDLE);
        config.setMaxWaitMillis(MAX_WAIT);
        config.setTestOnBorrow(TEST_ON_BORROW);

        // 集群模式
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();

        HostAndPort hostAndPort1 = new HostAndPort("192.168.56.101", 6379);
        HostAndPort hostAndPort2 = new HostAndPort("192.168.56.102", 6379);
        HostAndPort hostAndPort3 = new HostAndPort("192.168.56.103", 6379);
        HostAndPort hostAndPort4 = new HostAndPort("192.168.56.104", 6379);
        HostAndPort hostAndPort5 = new HostAndPort("192.168.56.105", 6379);
        HostAndPort hostAndPort6 = new HostAndPort("192.168.56.106", 6379);

        nodes.add(hostAndPort1);
        nodes.add(hostAndPort2);
        nodes.add(hostAndPort3);
        nodes.add(hostAndPort4);
        nodes.add(hostAndPort5);
        nodes.add(hostAndPort6);

        // 只有当jedisCluster为空时才实例化
        if (jedisCluster == null) {
            jedisCluster = new JedisCluster(nodes, poolConfig);
        }

        return jedisCluster;
    }

    public static void main(String[] args) {
        JedisCluster jedisCluster = OuterClass.getJedisCluster();
        jedisCluster.set("testcluster","hello redis cluster");
        try {
            while (true){
                System.out.println( jedisCluster.get("testcluster"));
                Thread.sleep(1000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


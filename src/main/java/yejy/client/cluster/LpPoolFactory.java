package yejy.client.cluster;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author yejunyang2012@163.com
 * @date 2019/9/6 22:13
 **/
public class LpPoolFactory  implements PooledObjectFactory<JedisCluster> {

    @Override
    public PooledObject<JedisCluster> makeObject() throws Exception {
        System.out.println("make Object");
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 最大连接数  
        poolConfig.setMaxTotal(300);
        // 最大空闲数  
        poolConfig.setMaxIdle(10);
        // 最大允许等待时间，如果超过这个时间还未获取到连接，则会报JedisException异常：  
        // Could not get a resource from the pool  
        poolConfig.setMaxWaitMillis(1000);
        Set<HostAndPort> nodes = new LinkedHashSet<HostAndPort>();
        nodes.add(new HostAndPort("192.168.56.101", 6379));
        nodes.add(new HostAndPort("192.168.56.102", 6379));
        nodes.add(new HostAndPort("192.168.56.103", 6379));
        nodes.add(new HostAndPort("192.168.56.104", 6379));
        nodes.add(new HostAndPort("192.168.56.105", 6379));
        nodes.add(new HostAndPort("192.168.56.106", 6379));
        JedisCluster JedisCluster = new JedisCluster(nodes, poolConfig);
        return new DefaultPooledObject<JedisCluster>(JedisCluster);
    }

    @Override
    public void destroyObject(PooledObject<JedisCluster> pooledObject) throws Exception {
        System.out.println("destroy Object");
        JedisCluster JedisCluster = pooledObject.getObject();
        JedisCluster = null;
    }

    /**
      * 功能描述：判断资源对象是否有效，有效返回 true，无效返回 false
      * 
      * 什么时候会调用此方法
      * 1：从资源池中获取资源的时候，参数 testOnBorrow 或者 testOnCreate 中有一个 配置 为 true 时，则调用  factory.validateObject() 方法
      * 2：将资源返还给资源池的时候，参数 testOnReturn，配置为 true 时，调用此方法
      * 3：资源回收线程，回收资源的时候，参数 testWhileIdle，配置为 true 时，调用此方法
      */
    @Override
    public boolean validateObject(PooledObject<JedisCluster> pooledObject) {
        System.out.println("validate Object");
        return true;
    }
    /**
      * 功能描述：激活资源对象
      * 
      * 什么时候会调用此方法
      * 1：从资源池中获取资源的时候
      * 2：资源回收线程，回收资源的时候，根据配置的 testWhileIdle 参数，
      * 判断 是否执行 factory.activateObject()方法，true 执行，false 不执行
      *
      */
    @Override
    public void activateObject(PooledObject<JedisCluster> pooledObject) throws Exception {
        System.out.println("activate Object");
    }
    /**
     * 功能描述：钝化资源对象
     *
     * 什么时候会调用此方法
     * 1：将资源返还给资源池时，调用此方法。
     */
    @Override
    public void passivateObject(PooledObject<JedisCluster> pooledObject) throws Exception {
        System.out.println("passivate Object");
    }
}

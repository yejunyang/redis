package yejy.client.sentinel;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yejunyang2012@163.com
 * @date 2019/8/28 20:15
 **/
public class RedisSentinel {

    //连接池配置
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    Jedis jedis = null;
    JedisSentinelPool pool = null;


    public void init(){
        jedisPoolConfig.setMaxTotal(9);
        jedisPoolConfig.setMaxIdle(4);
        jedisPoolConfig.setMinIdle(2);
        //哨兵信息
        Set<String> sentinels = new HashSet<String>(Arrays.asList(
                "192.168.56.104:26379",
                "192.168.56.105:26379",
                "192.168.56.106:26379"
        ));
        //创建连接池
        //mymaster是我们配置给哨兵的服务名称
        //sentinels是哨兵信息
        //jedisPoolConfig是连接池配置
        //abcdefg是连接Redis服务器的密码
        pool = new JedisSentinelPool("mymaster", sentinels, jedisPoolConfig, null);
        //获取客户端
        jedis = pool.getResource();
        //执行两个命令
        //jedis.set("mykey", "myvalue");

    }

   public void getData(String key){
        try {
            String myvalue = jedis.get(key);
            //打印信息
            System.out.println(myvalue);
        }catch (Exception e){
            jedis = pool.getResource();
            e.printStackTrace();
        }

   }

    public static void main(String[] args) {
        RedisSentinel redisSentinel = new RedisSentinel();
        redisSentinel.init();
        while (true){
            try {
                redisSentinel.getData("test");
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}

package yejy.client.cluster.lua;

import redis.clients.jedis.JedisCluster;
import yejy.client.cluster.Operate;
import yejy.client.cluster.RedisClusterPool;

/**
 * @author yejunyang2012@163.com
 * @date 2020/11/11 0:33
 **/
public class ExistAndDelete {

    public void existAndDelet(JedisCluster jedisCluster, int numKey,String key,String value){
        /*jedisCluster.scriptLoad();*/
        jedisCluster.eval(ReadFile.readFile("isExistAndDelete.lua"),numKey,key,value);
    }

    public static void main(String[] args) {
        JedisCluster jedisCluster = RedisClusterPool.getJedisCluster();
        Operate operate = new Operate();
        String key = "luaKey";
        String value = "1";
        operate.setString(jedisCluster,key,value);
        System.out.println("====执行lua脚本前===="+operate.getString(jedisCluster,key));
        ExistAndDelete existAndDelete = new ExistAndDelete();
        existAndDelete.existAndDelet(jedisCluster,1,key,value);
        System.out.println("====执行lua脚本后===="+operate.getString(jedisCluster,key));
    }
}

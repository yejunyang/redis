package yejy.client.cluster;

import redis.clients.jedis.JedisCluster;
import yejy.client.cluster.lua.LuaCommand;

/**
 * @author yejunyang2012@163.com
 * @date 2020/11/11 22:37
 **/
public class StringType {

    /**
     * 不存在才设置
     * @param jedisCluster
     * @param key
     * @param value
     * @param time 单位是秒
     * @return
     */
    public String setIfNotExistSecond(JedisCluster jedisCluster, String key, String value,long time){
        String resout = jedisCluster.set(key, value, "NX", "EX", time);
        return resout;
    }

    /**
     * 不存在才设置
     * @param jedisCluster
     * @param key
     * @param value
     * @param time 单位是毫秒
     * @return
     */
    public String setIfNotExistMilliSecond(JedisCluster jedisCluster, String key, String value,long time){
        String resout = jedisCluster.set(key, value, "NX", "PX", time);
        return resout;
    }

    /**
     * 存在才设置
     * @param jedisCluster
     * @param key
     * @param value
     * @param time 单位是秒
     * @return
     */
    public String setIfExistSecond(JedisCluster jedisCluster, String key, String value,long time){
        String resout = jedisCluster.set(key, value, "XX", "EX", time);
        return resout;
    }

    /**
     * 存在才设置
     * @param jedisCluster
     * @param key
     * @param value
     * @param time 单位是毫秒
     * @return
     */
    public String setIfExistMilliSecond(JedisCluster jedisCluster, String key, String value,long time){
        String resout = jedisCluster.set(key, value, "XX", "PX", time);
        return resout;
    }

    public static void main(String[] args) throws InterruptedException {
        JedisCluster jedisCluster = RedisClusterPool.getJedisCluster();
        Operate operate = new Operate();
        StringType stringType = new StringType();

        System.out.println("================不存在执行，秒======================");
        String key = "nxex2";
        System.out.println(operate.getString(jedisCluster,key));
        long time = System.currentTimeMillis();
        System.out.println(stringType.setIfNotExistSecond(jedisCluster,key,time+"",2));
        for (int i=0; i<4 ;i++){
            stringType.setIfExistSecond(jedisCluster,key,time+"",2);
            System.out.println(operate.getString(jedisCluster,key));
            Thread.sleep(1_000);
        }
        LuaCommand ifExistRefresh = new LuaCommand();
        for (int i=0; i<4 ;i++){
            Object o = ifExistRefresh.ifExistRefresh(jedisCluster, 1, key, time + "");
            System.out.println("===lua脚本访问后，刷新过期时间，原子操作=====:"+o);
            System.out.println(operate.getString(jedisCluster,key));
            Thread.sleep(1_000);
        }
        Thread.sleep(3_000);
        System.out.println(operate.getString(jedisCluster,key));

        System.out.println("================不存在执行，毫秒======================");
        String nxex = "nxpx1";
        System.out.println(operate.getString(jedisCluster,nxex));
        System.out.println(stringType.setIfNotExistMilliSecond(jedisCluster,nxex,System.currentTimeMillis()+"",2000));
        System.out.println(operate.getString(jedisCluster,nxex));
        Thread.sleep(3_000);
        System.out.println(operate.getString(jedisCluster,nxex));

        System.out.println("================存在执行，秒======================");
        String xxex = "xxex";
        System.out.println(operate.getString(jedisCluster,xxex));
        System.out.println(stringType.setIfExistSecond(jedisCluster,xxex,System.currentTimeMillis()+"",2));
        System.out.println(operate.getString(jedisCluster,xxex));
        stringType.setIfNotExistSecond(jedisCluster,xxex,System.currentTimeMillis()+"",2);
        System.out.println(operate.getString(jedisCluster,xxex));
        System.out.println(stringType.setIfExistSecond(jedisCluster,xxex,System.currentTimeMillis()+"",2));
        System.out.println(operate.getString(jedisCluster,xxex));
        Thread.sleep(3_000);
        System.out.println(operate.getString(jedisCluster,xxex));

        System.out.println("================存在执行，秒======================");
        String xxpx = "xxpx";
        System.out.println(operate.getString(jedisCluster,xxpx));
        System.out.println(stringType.setIfExistMilliSecond(jedisCluster,xxpx,System.currentTimeMillis()+"",2000));
        System.out.println(operate.getString(jedisCluster,xxpx));
        stringType.setIfNotExistSecond(jedisCluster,xxpx,System.currentTimeMillis()+"",2);
        System.out.println(operate.getString(jedisCluster,xxpx));
        System.out.println(stringType.setIfExistMilliSecond(jedisCluster,xxpx,System.currentTimeMillis()+"",2000));
        System.out.println(operate.getString(jedisCluster,xxpx));
        Thread.sleep(3_000);
        System.out.println(operate.getString(jedisCluster,xxpx));
    }
}

package yejy.client.cluster;

import redis.clients.jedis.BitOP;
import redis.clients.jedis.JedisCluster;

/**
 * @author yejunyang2012@163.com
 * @date 2020/11/9 23:45
 **/
public class BitMap {

    /**
     *
     * @param jedisCluster
     * @param key
     * @param offset
     * @param value 只能是“0”或者“1”
     * @return
     */
    public boolean setBitMap(JedisCluster jedisCluster,String key,long offset,String value){
        Boolean result = jedisCluster.setbit(key, offset, value);
        System.out.println("setbitmap result:"+ result);
        return result;
    }

    public boolean setBitMap(JedisCluster jedisCluster,String key,long offset,boolean value){
        Boolean result = jedisCluster.setbit(key, offset, value);
        System.out.println("setbitmap result:"+ result);
        return result;
    }

    public boolean getBitMap(JedisCluster jedisCluster,String key,long offset){
        return jedisCluster.getbit(key, offset);
    }

    public long bigCount(JedisCluster jedisCluster,String key){
        return jedisCluster.bitcount(key);
    }

    /**
     * start到end之间的字节数中1的个数，范围:[start,end]
     * @param jedisCluster
     * @param key
     * @param start
     * @param end
     * @return
     */
    public long bigCount(JedisCluster jedisCluster,String key,long start,long end){
        return jedisCluster.bitcount(key,start,end);
    }

    /**
     * 当集群模式时，如果需要统计的key不在一个节点上，无法使用该方法，否则会抛如下异常
     * Exception：No way to dispatch this command to Redis Cluster because keys have different slots.
     * @param jedisCluster
     * @param bitOP
     * @param destKey
     * @param keys
     * @return
     */
    public Long bitTop(JedisCluster jedisCluster, BitOP bitOP,String destKey,String ... keys){
        Long result = jedisCluster.bitop(bitOP, destKey, keys);
        System.out.println("bitop:"+ result);
        return bigCount(jedisCluster,destKey);
    }

    public static void main(String[] args) {
        JedisCluster jedisCluster = RedisClusterPool.getJedisCluster();
        BitMap bitMap = new BitMap();
        bitMap.setBitMap(jedisCluster,"testBit",0,"1");
        System.out.println(bitMap.getBitMap(jedisCluster,"testBit",0));
        bitMap.setBitMap(jedisCluster,"testBit",1,true);
        bitMap.setBitMap(jedisCluster,"testBit",2,true);
        bitMap.setBitMap(jedisCluster,"testBit",3,true);
        bitMap.setBitMap(jedisCluster,"testBit",8,true);
        System.out.println(bitMap.getBitMap(jedisCluster,"testBit",1));
        System.out.println(bitMap.bigCount(jedisCluster,"testBit"));
        System.out.println(bitMap.bigCount(jedisCluster,"testBit",0,0));

        bitMap.setBitMap(jedisCluster,"testBit1",1,true);
        bitMap.setBitMap(jedisCluster,"testBit1",2,false);
        bitMap.setBitMap(jedisCluster,"testBit1",3,false);
        bitMap.setBitMap(jedisCluster,"testBit1",8,true);
        System.out.println(bitMap.bigCount(jedisCluster,"testBit1"));
        //bitMap.bitTop(jedisCluster,BitOP.AND,"testBit2","testBit","testBit1");
        //bitMap.bitTop(jedisCluster,BitOP.OR,"testBit3","testBit","testBit1");
    }
}

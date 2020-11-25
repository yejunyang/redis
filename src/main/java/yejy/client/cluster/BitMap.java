package yejy.client.cluster;

import redis.clients.jedis.BitOP;
import redis.clients.jedis.JedisCluster;

import java.io.UnsupportedEncodingException;

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
     * @param start  开始的字节下标
     * @param end    结束的字节下标
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

    /**
     * redis中第0位是左边开始算的
     * 如 0111111010000000，第0位是指最左边的0
     * @param jedisCluster
     * @param destKey
     * @return
     */
    public String bitMapToBinaryString(JedisCluster jedisCluster,String destKey){
        Operate operate = new Operate();
        byte[] testBits = operate.getSetForBit(jedisCluster, destKey);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <testBits.length ; i++) {
            stringBuilder.append(Integer.toBinaryString((testBits[i] & 0xFF) + 0x100).substring(1));
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        JedisCluster jedisCluster = RedisClusterPool.getJedisCluster();
        BitMap bitMap = new BitMap();
        bitMap.setBitMap(jedisCluster,"testBit",0,"0");
        System.out.println(bitMap.getBitMap(jedisCluster,"testBit",0));
        bitMap.setBitMap(jedisCluster,"testBit",1,true);
        bitMap.setBitMap(jedisCluster,"testBit",2,true);
        bitMap.setBitMap(jedisCluster,"testBit",3,true);
        bitMap.setBitMap(jedisCluster,"testBit",4,true);
        bitMap.setBitMap(jedisCluster,"testBit",5,true);
        bitMap.setBitMap(jedisCluster,"testBit",6,true);
        bitMap.setBitMap(jedisCluster,"testBit",8,true);
        bitMap.setBitMap(jedisCluster,"testBit",10*365,true);
        System.out.println(bitMap.getBitMap(jedisCluster,"testBit",1));
        System.out.println(bitMap.bigCount(jedisCluster,"testBit"));
        System.out.println(bitMap.bigCount(jedisCluster,"testBit",0,0));
        System.out.println(bitMap.bigCount(jedisCluster,"testBit",10*365/8,10*365/8));
        System.out.println(bitMap.bitMapToBinaryString(jedisCluster,"testBit"));
        bitMap.setBitMap(jedisCluster,"testBit1",1,true);
        bitMap.setBitMap(jedisCluster,"testBit1",2,false);
        bitMap.setBitMap(jedisCluster,"testBit1",3,false);
        bitMap.setBitMap(jedisCluster,"testBit1",8,true);
        System.out.println(bitMap.bigCount(jedisCluster,"testBit1"));
        //bitMap.bitTop(jedisCluster,BitOP.AND,"testBit2","testBit","testBit1");
        //bitMap.bitTop(jedisCluster,BitOP.OR,"testBit3","testBit","testBit1");
    }
}

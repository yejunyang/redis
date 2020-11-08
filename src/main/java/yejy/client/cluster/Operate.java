package yejy.client.cluster;

import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Set;

/**
 * @author yejunyang2012@163.com
 * @date 2020/11/8 22:24
 **/
public class Operate {
    public Long setHash(JedisCluster jedisCluster,String hashName,String key,String value){
        Long hst = jedisCluster.hset(hashName, key, value);
        System.out.println("set Hash result:"+hst);
        return hst;
    }

    public String getHash(JedisCluster jedisCluster,String hashName,String key){
        return jedisCluster.hget(hashName,key);
    }

    public String setSet(JedisCluster jedisCluster,String setName,String value){
        String set = jedisCluster.set(setName, value);
        System.out.println("set Set result:"+set);
        return set;
    }

    public String getSet(JedisCluster jedisCluster,String setName){
        return jedisCluster.get(setName);
    }

    public Long setList(JedisCluster jedisCluster, String listName, String[] values){
        Long result = jedisCluster.lpush(listName, values);
        System.out.println("push list result:" + result);
        return result;
    }

    public List<String> getList(JedisCluster jedisCluster,String listName,int start,int end){
        return jedisCluster.lrange(listName,start,end);
    }

    public String lpopList(JedisCluster jedisCluster,String listName){
        return jedisCluster.lpop(listName);
    }

    public String rpopList(JedisCluster jedisCluster,String listName){
        return jedisCluster.rpop(listName);
    }

    public Long setZset(JedisCluster jedisCluster,String zsetName,String value,double score){
        Long result = jedisCluster.zadd(zsetName, score, value);
        System.out.println("set zset result:" + result);
        return result;
    }

    public Set<String> getZset(JedisCluster jedisCluster, String zsetName, int start, int end){
        return jedisCluster.zrange(zsetName,start,end);
    }

    public Set<Tuple> getZsetWithScore(JedisCluster jedisCluster, String zsetName, int start, int end){
        return jedisCluster.zrangeWithScores(zsetName,start,end);
    }

    public String setString(JedisCluster jedisCluster,String key,String value){
        String result = jedisCluster.set(key, value);
        System.out.println("set string result:" + result);
        return result;
    }

    public String getString(JedisCluster jedisCluster,String key){
        return jedisCluster.get(key);
    }

    public static void main(String[] args) {
        JedisCluster jedisCluster = RedisClusterPool.getJedisCluster();
        Operate operate = new Operate();
        System.out.println("=================Hash==================");
        operate.setHash(jedisCluster,"testHash","test","hash:"+System.currentTimeMillis());
        String hash = operate.getHash(jedisCluster, "testHash", "test");
        System.out.println(hash);
        System.out.println("==================Set=================");
        operate.setSet(jedisCluster,"testSet","set:"+System.currentTimeMillis());
        String testSet = operate.getSet(jedisCluster, "testSet");
        System.out.println(testSet);
        System.out.println("===================List================");
        operate.setList(jedisCluster,"testList",new String[]{"1+"+System.currentTimeMillis(),
                "2+"+System.currentTimeMillis(),"3+"+System.currentTimeMillis(),
                "4+"+System.currentTimeMillis(),"5+"+System.currentTimeMillis(),});
        System.out.println(operate.getList(jedisCluster,"testList",0,-1));
        System.out.println(operate.lpopList(jedisCluster,"testList"));
        System.out.println(operate.getList(jedisCluster,"testList",0,-1));
        System.out.println(operate.rpopList(jedisCluster,"testList"));
        System.out.println(operate.getList(jedisCluster,"testList",0,-1));
        System.out.println("==================Zset=================");
        operate.setZset(jedisCluster,"testZset","r"+System.currentTimeMillis(),1);
        operate.setZset(jedisCluster,"testZset","z"+System.currentTimeMillis(),2);
        System.out.println(operate.getZset(jedisCluster,"testZset",0,-1));
        System.out.println(operate.getZsetWithScore(jedisCluster,"testZset",0,-1));
        System.out.println("==================String=================");
        operate.setString(jedisCluster,"testString" , "string+"+System.currentTimeMillis());
        System.out.println(operate.getString(jedisCluster,"testString"));
    }
}

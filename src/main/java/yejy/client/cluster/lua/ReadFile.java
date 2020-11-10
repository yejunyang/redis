package yejy.client.cluster.lua;

import java.io.*;
import java.net.FileNameMap;
import java.net.URL;

/**
 * @author yejunyang2012@163.com
 * @date 2020/11/11 0:52
 **/
public class ReadFile {

    public static String readFile(String fileName){
        // classpath目录下
        URL resource = ReadFile.class.getResource("/lua/"+fileName);
        // classpath+该类所在路径
        /*URL resource1 = ReadFile.class.getResource("");
        // classpath目录下
        URL resource2 = ReadFile.class.getClassLoader().getResource("");
        // null
        URL resource3 = ReadFile.class.getClassLoader().getResource("/");
        //file:/D:/sourceCode/demo4/target/classes/
        System.out.println(resource);
        //file:/D:/sourceCode/demo4/target/classes/com/example/demo4/asm/
        System.out.println(resource1);
        //file:/D:/sourceCode/demo4/target/classes/
        System.out.println(resource2);
        //null
        System.out.println(resource3);*/
        try {
            StringBuffer buffer = new StringBuffer();
            BufferedReader bf= new BufferedReader(new FileReader(resource.getFile()));
            String s = null;
            //使用readLine方法，一次读一行
            while((s = bf.readLine())!=null){
                if (s != null && !s.trim().startsWith("--")){
                    buffer.append(s).append(" ");
                }
            }
            return buffer.toString();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    public static void main(String[] args) {
        System.out.println(ReadFile.readFile("isExistAndDelete.lua"));
    }
}

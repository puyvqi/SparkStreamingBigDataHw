package Steamer;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.apache.spark.*;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.*;
import scala.Tuple2;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Streamer {
    public static void main(String[] agrs){

        Runnable r=new Server();
        Thread t=new Thread(r);
        t.start();
        System.setProperty("hadoop.home.dir", "E:\\hadoop-common-2.2.0-bin-master");
        SparkConf conf=new SparkConf().setAppName("streaming test").setMaster("local[2]");
        JavaStreamingContext jsc=new JavaStreamingContext(conf,new Duration(3000));
        JavaReceiverInputDStream<String> lines = jsc.socketTextStream("127.0.0.1", 9999);


        JavaDStream<String[]> noCommaData=lines.map(s->s.split(","));
        JavaDStream<String[]> baddata=noCommaData.filter(new Function<String[], Boolean>() {
            @Override
            public Boolean call(String[] strings) throws Exception {
                 if(Integer.parseInt(strings[2])>=150)return true;//pm2.5
                 else if(Integer.parseInt(strings[3])>=350)return true;
                 else if(Integer.parseInt(strings[4])>=800)return true;
                 else if(Integer.parseInt(strings[5])>=60)return true;
                 else if(Integer.parseInt(strings[6])>=1200)return true;
                 else if(Integer.parseInt(strings[7])>=400)return true;
                 else return false;
            }
        });
        JavaDStream<String[]> output=baddata.flatMap(new FlatMapFunction<String[], String[]>() {
            @Override
            public Iterator<String[]> call(String[] strings) throws Exception {

                ArrayList<String[]> kk=new ArrayList<String[]>();
                for(int i=2;i<strings.length;i++){
                    String[] res=new String[4];
                    if(Integer.parseInt(strings[i])>=Integer.parseInt(critera[i])){
                        res[0]=strings[0];
                        res[1]=strings[1];
                        res[2]=template[i];
                        res[3]=strings[i];
                        kk.add(res);
                    }
                }
                return kk.iterator();
            }
            public String[] critera={"time","id","150","350","800","60","1200","400"};
            public String[] template={"time","id","pm2.5","pm10","so2","cd","no2","o3"};
        });
        JavaDStream<String> result=output.map(new Function<String[], String>() {
            @Override
            public String call(String[] strings) throws Exception {
                return String.join("|",strings);
            }
        });
        result.dstream().print();

        //result.dstream().saveAsTextFiles("pr-time","txt");

        jsc.start();

        try{
            jsc.awaitTermination();
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}

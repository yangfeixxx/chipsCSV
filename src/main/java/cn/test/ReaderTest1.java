package cn.test;


import core.CsvReader;
import core.CsvWriter;
import core.People;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReaderTest1 {
    public static void main(String[] args) throws Exception {

        writeList();
        writeObjectList();
        readRowTest();
       readAllRawTest();
        //System.out.println(csvReader.getAllHeaderRawMap());
//        while(csvReader.readable()){
//            System.out.println(csvReader.getValueByIndex(5));
//        }csvReader.getAllHeaderRawMap()
        //        }csvReader.getAllHeaderRawMap()
    }

    public static void writeObjectList() throws Exception {
        //对于实体类可以自动生成表头，并封装数据为CSV格式，可以自定义分隔符和编码

        // CsvWriter cw = new CsvWriter("/Users/yangfei/Desktop/heh.csv","GBK","/");
        CsvWriter cw = new CsvWriter("/Users/yangfei/Desktop/oo.csv");
        List list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            People p = new People();
            p.setAge(i + 10 + "");
            p.setName("小明" + i);
            list.add(p);
        }
        cw.writerRawRecordByObjects(list);
        cw.flush();
    }
    public static void writeList() throws Exception {
        //也可以用常规写法

        CsvWriter cw = new CsvWriter("/Users/yangfei/Desktop/oo.csv");
        List headers = new ArrayList<>();
        List rowRecord = new ArrayList();
        headers.add("name");
        headers.add("age");
        rowRecord.add("张三");
        rowRecord.add("11");
        rowRecord.add("李四");
        rowRecord.add("14");
        rowRecord.add("李四");
        rowRecord.add("14");
        cw.writerHeader(headers);
        cw.writerRawRecord(rowRecord);
        cw.flush();
    }

    public static void readAllRawTest() throws IOException {
        //对于数据量小的，可以直接拿全部数据
        CsvReader csvReader = new CsvReader("/Users/yangfei/Desktop/heh.csv", "utf-8");
        Arrays.asList(csvReader.readHeader()).forEach(a->{
            System.out.println(a);
        }); //可以拿表头
        List<CsvReader.HeaderRawMap> allHeaderRawMap = csvReader.getAllHeaderRawMap();
        allHeaderRawMap.forEach(a -> {
            System.out.println("根据表头拿："+a.getValueByIndex(1));//根据表头拿
            System.out.println("根据表头下标拿数据："+a.getValueByName("name"));//根据表头下标拿数据
        });
    }

    public static void readRowTest() throws IOException {
        //数据量大的，一行一行的拿
        CsvReader csvReader = new CsvReader("/Users/yangfei/Desktop/oo.csv", "utf-8");
        while (csvReader.readable()) {

            System.out.println("拿一行数据："+csvReader.getRawRecord());//拿一行数据
            System.out.println("根据表头拿："+csvReader.getValueByName("age"));//根据表头拿
            System.out.println("根据表头下标拿数据："+csvReader.getValueByIndex(0));//根据表头下标拿数据
        }
    }


}

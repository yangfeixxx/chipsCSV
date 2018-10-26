package cn.test;


import core.CsvReader;
import core.CsvWriter;
import core.People;

import java.util.ArrayList;
import java.util.List;

public class ReaderTest1 {
    public static void main(String[] args) throws Exception {
        CsvReader csvReader = new CsvReader("/Users/yangfei/Desktop/hhh.csv","utf-8");
        //System.out.println(csvReader.getAllHeaderRawMap());
//        while(csvReader.readable()){
//            System.out.println(csvReader.getValueByIndex(5));
//        }csvReader.getAllHeaderRawMap()
        //        }csvReader.getAllHeaderRawMap()
        List<CsvReader.HeaderRawMap> allHeaderRawMap = csvReader.getAllHeaderRawMap();
        allHeaderRawMap.forEach(a-> System.out.println(a.getValueByIndex(1)));
        CsvWriter cw = new CsvWriter("/Users/yangfei/Desktop/heh.csv");

        List list = new ArrayList<>();
        for (int i = 0; i <20 ; i++) {
            People p = new People();
            p.setAge(i+10+"");
            p.setName("小明"+i);
            list.add(p);
        }
        cw.writerRawRecordByObjects(list);
        cw.flush();
    }
}

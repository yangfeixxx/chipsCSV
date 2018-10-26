package core;


import java.io.*;
import java.util.*;

/**
 * Author: 羊飞
 * Since: 1.8
 * CreateBy: 2018/10/26
 */
public class CsvReader {
    private BufferedReader               reader;
    private String                       fileName;
    private HeaderHolder                 headerHolder;
    private File                         file;
    private List                         allHeaderRawMap;
    private String                       charset;
    private boolean                      bufferIsClosed;
    private String                       line;
    private HeaderRawMap<String, String> hrm;
    private String                       separator;

    private static final String          DEFAULTCHARSET = "utf-8";
    private static final String          DEFAULTSEPARATOR = ",";

    public CsvReader(String fileName) throws IOException {
        this(fileName, DEFAULTCHARSET,DEFAULTSEPARATOR);
    }

    public CsvReader(String fileName,String charset) throws IOException {
        this(fileName, charset,DEFAULTSEPARATOR);
    }

    public CsvReader(String fileName, String charset,String separator) throws IOException {
        if (!(file = new File(fileName)).exists()) {
            throw new FileNotFoundException(fileName + "文件未找到");
        }
        this.charset = charset;
        this.fileName = fileName;
        this.bufferIsClosed = false;
        this.separator = separator;
        headerHolder = new HeaderHolder();
    }

    public class HeaderRawMap<A, B> extends LinkedHashMap<A, B> {
        public B getValueByName(String name) {
            return  this.get(name);
        }

        public B getValueByIndex(int index) {
            return  this.get(headerHolder.indexheaderMap.get(index));
        }
    }

    private class HeaderHolder {
        public Map<Integer, String> indexheaderMap = new HashMap<>();
        public String[] headers;

        public HeaderHolder() throws IOException {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
            String header = reader.readLine();
            if(header==null){
                System.err.println("此文件没有数据");
                return;
            }
            headers = header.split(separator);
            for (int i = 0, size = headers.length; i < size; i++) {
                indexheaderMap.put(i, headers[i]);
            }
        }
    }

    public String[] readHeader() {
        return headerHolder.headers;
    }

    public List<HeaderRawMap> getAllHeaderRawMap() throws IOException {
        if (allHeaderRawMap != null)
            return allHeaderRawMap;
        List list = new ArrayList();
        while (readable()) {
            list.add(hrm);
        }
        allHeaderRawMap = list;
        return list;
    }


   public boolean readable() throws IOException {
        if (bufferIsClosed) {
            System.err.println("流通道已经被关闭了");
            return false;
        }
        try {
            if ((line = reader.readLine()) != null) {
                hrm = new HeaderRawMap();
                String[] rows = line.split(separator);
                for (int i = 0, size = rows.length; i < size; i++) {
                    hrm.put(headerHolder.headers[i], rows[i]);
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            reader.close();
        }
        reader.close();
        bufferIsClosed = true;
        return false;
    }


    public String getValueByName(String name) {
        return hrm.getValueByName(name);
    }

    public String getValueByIndex(int index) {
        return hrm.getValueByIndex(index);
    }
    public String getRawRecord(){
        return line;
    }
}

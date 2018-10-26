package core;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: 羊飞
 * Since: 1.8
 * CreateBy: 2018/10/26
 */
public class CsvWriter {
    private File          file;
    private StringBuilder headerBuffer;
    private StringBuilder rowBuffer;
    private String        separator;
    private String        fileName;
    private String        charset;
    private boolean       hasHeaderBuffer;
    private static final String DEFAULTCHARSET = "utf-8";
    private static final String DEFAULTSEPARATOR = ",";
    public CsvWriter(String fileName) throws IOException {
        this(fileName, DEFAULTCHARSET, DEFAULTSEPARATOR);
    }

    public CsvWriter(String fileName, String charset) throws IOException {
        this(fileName, charset, DEFAULTSEPARATOR);
    }

    public CsvWriter(String fileName, String charset, String separator) throws IOException {
        if (!(file = new File(fileName)).exists())
            file.createNewFile();
        this.fileName        = fileName;
        this.charset         = charset;
        this.separator       = separator;
        this.hasHeaderBuffer = true;
        this.rowBuffer       = new StringBuilder();
    }


    public void writerHeader(List headers) {
        StringBuilder sb = new StringBuilder();
        writerBuffer(headers, sb);
        headerBuffer = sb;
    }


    public void writerRawRecord(List rawRecord) {
        StringBuilder sb = new StringBuilder();
        writerBuffer(rawRecord, sb);
        rowBuffer.append(sb);
    }

    public void writerRawRecordByObjects(List list) throws Exception {
        if (list == null || list.size() == 0) {
            throw new Exception("集合不能为空");
        }
        Object o1 = list.get(0);
        if (headerBuffer == null&&hasHeaderBuffer) {
            writerHeader(Arrays.asList(o1.getClass().getDeclaredFields()).stream().map(Field::getName).collect(Collectors.toList()));
        }

        list.forEach(o -> {
                    List var1 = new ArrayList();
                    Class<?> aClass = o.getClass();
                    Field[] declaredFields = aClass.getDeclaredFields();
                    Arrays.asList(declaredFields).forEach(field -> {
                        String key = field.getName();
                        try {
                            PropertyDescriptor pd = new PropertyDescriptor(key, aClass);
                            Method readMethod = pd.getReadMethod();
                            var1.add(readMethod.invoke(o));
                        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    });
                    writerRawRecord(var1);
                }
        );

    }

    private void writerBuffer(List<String> asList, StringBuilder sb) {
        asList.forEach(r ->
                sb.append(r + separator)
        );
        int size = sb.length();
        sb.replace(size - 1, size, "\n");
    }

    public void flush() throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),charset));
        StringBuilder sb  = null;
        try {
        if(hasHeaderBuffer) {
            headerBuffer.append(rowBuffer);
            sb = headerBuffer;
        }else
            sb = rowBuffer;
            writer.write(sb.toString(), 0, sb.length());

        }catch (Exception e){
            e.printStackTrace();

        }finally {
            writer.close();
        }
        headerBuffer=null;
        rowBuffer = null;
        hasHeaderBuffer = false;
    }


}


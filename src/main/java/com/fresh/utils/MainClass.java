package com.fresh.utils;


import cn.hutool.crypto.digest.MD5;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass {

    static volatile AtomicLong process = new AtomicLong(0);
    static long length = 0;
    static byte[] bs = new byte[100];
    volatile static int j = 0;

    static Map<String, String> columnMap = null;
    static Map<String, String> propertyMap = null;

    static {
        columnMap = new HashMap<>();
        columnMap.put("java.lang.Long", "`%s` bigint(20) NOT NULL AUTO_INCREMENT,\n");
        columnMap.put("java.lang.String", "`%s` varchar(255) DEFAULT NULL,\n");
        columnMap.put("java.lang.Integer", "`%s` int(11) DEFAULT NULL,\n ");
        columnMap.put("java.util.Date", "`%s` datetime DEFAULT NULL, \n ");
        columnMap.put("java.math.BigDecimal", "`%s` decimal(10,2) DEFAULT NULL, \n ");
        columnMap.put("java.lang.Boolean", "`%s` bit(1) DEFAULT NULL,\n");
        columnMap.put("java.lang.Double", "`%s` double(10,2) DEFAULT NULL,\n");
        columnMap.put("java.lang.Float", "`%s` float(10,2) DEFAULT NULL,");
        columnMap.put("primaryKey", " PRIMARY KEY (`id`) \n");

        propertyMap = new HashMap<>();
        propertyMap.put("bigint",   "\t\t\t\tprivate java.lang.Long %s ; \n");
        propertyMap.put("varchar",  "\t\t\t\tprivate java.lang.String %s ; \n");
        propertyMap.put("char",     "\t\t\t\tprivate java.lang.String %s ; \n");
        propertyMap.put("int",      "\t\t\t\tprivate java.lang.Integer %s ; \n");
        propertyMap.put("datetime", "\t\t\t\tprivate java.util.Date %s ; \n");
        propertyMap.put("date",     "\t\t\t\tprivate java.util.Date %s ; \n");
        propertyMap.put("decimal",  "\t\t\t\tprivate java.math.BigDecimal %s ; \n");
        propertyMap.put("bit",      "\t\t\t\tprivate java.lang.Boolean %s ; \n");
        propertyMap.put("double",   "\t\t\t\tprivate java.lang.Double %s ; \n");
        propertyMap.put("float",    "\t\t\t\tprivate java.lang.Float %s ; \n");
    }




    public static void main(String[] args) throws ClassNotFoundException, MalformedURLException, UnsupportedEncodingException {
        String x = UUID.randomUUID().toString();
        System.out.println(x);
        String x1 = x.replaceAll("-", "");
        System.out.println(x1);
        System.out.println(x1.length());

    }

    private static String toBean(String sql) {
        StringBuffer buffer = new StringBuffer();
        String tableName = sql.substring(sql.indexOf("`"), sql.indexOf("(")).trim().replace("`","");// 获取表名
        String beanName = toHump(tableName, true);

        String columns = sql.substring(sql.indexOf("(")+1, sql.lastIndexOf(")"));// 获取字段内容部分
        String[] propertyList = columns.split("\n");// 分割
        StringBuffer pf = new StringBuffer();
        for (String item : propertyList) {
            String result = toPropertyContent(item);
            if (StringUtils.isNotBlank(result)) {
                pf.append(result);
            }
        }
        buffer.append(String.format("public class %s { \n %s \n }", beanName,pf));
        return buffer.toString();
    }

    /**
     * 将每一条sql 提取java属性类型 形如：`user_name` varchar(255) DEFAULT NULL, 提取为 private String userName;
     * @param columnContent
     * @return
     */
    private static String toPropertyContent(String columnContent) {
        if (columnContent.indexOf("`") != -1) {
            String result =  columnContent.substring(columnContent.indexOf("`") + 1, columnContent.lastIndexOf("`"));
            Set<String> nameSet = propertyMap.keySet();
            Iterator<String> iterator = nameSet.iterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                if (columnContent.contains(next)) {
                    return String.format(propertyMap.get(next), toHump(result,false));
                }
            }
        }
        return null;
    }

    /**
     * 获取完整的类名 形如：com.xx.entity.Users
     * @param path
     * @return
     */
    private static ArrayList<String> getCompleteClassName(String path) {
        File file = new File(path);
        System.out.println(file.isDirectory());
        File[] files = file.listFiles();
        ArrayList<String> list = new ArrayList<>();
        for (File item : files) {
            String replace = item.getName().replace(".java", "");
            String absolutePath = file.getAbsolutePath();
            String newPackName = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.length()).replace("\\", ".");
            String classPack = newPackName + "." + replace;
            list.add(classPack);
        }
        return list;
    }

    /**
     * 转脚本操作
     * @param list
     * @throws ClassNotFoundException
     */
    private static void toMysqlScript(List<String> list) throws ClassNotFoundException {
        for (String item : list) {
            Class<?> aClass = Class.forName(item);
            String simpleName = aClass.getSimpleName();
            String tableName = toHump(simpleName,false);
            StringBuffer sb = new StringBuffer();
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field field : declaredFields) {
                sb.append(toMYSQLColumn(field.getType().getName(), toUnderscore(field.getName())));
            }
            sb.append(columnMap.get("primaryKey"));
            System.err.println(String.format("CREATE TABLE  `%s` (\n%s ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;", toUnderscore(tableName), sb.toString()));
        }
    }

    private static String toMYSQLColumn(String name,String fieldName) {
        return String.format(columnMap.get(name),fieldName);
    }

    /**
     * 下划线转驼峰
     * @param target
     * @param isTableName 是否表名称 默认false
     * @return
     */
    private static String toHump(String target,Boolean isTableName) {
        if (target == null || "".equals(target)) {
            return target;
        }

        char[] chars = target.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int j = 0; j < chars.length; j++) {
            if (chars[j] == '_' && j + 1 < chars.length) {
                chars[j + 1] = Character.toUpperCase(chars[j + 1]);
            }
            if (chars[j] != '_') {
                if (j == 0 ) {
                    chars[j] = Character.toLowerCase(chars[j]);
                }
                if (isTableName && j == 0) {
                    chars[j] = Character.toUpperCase(chars[j]);
                }
                sb.append(chars[j]);
            }
        }

        return sb.toString();
    }

    /**
     * 驼峰->下划线
     * @param property
     * @return
     */
    public static String toUnderscore(String property) {// 下划线
        char[] chars = property.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            if (Character.isUpperCase(aChar)) {
                sb.append(i == 0 ?  "" : "_");
                sb.append(Character.toLowerCase(aChar));
                continue;
            }
            sb.append(aChar);
        }
        return sb.toString();
    }

    private static void t() {
        ArrayBlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(10);
        ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 10, 1, TimeUnit.SECONDS, workQueue);
        pool.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println();
            }
        });
    }

    private static void putExistKey(int hashCode) {
        bs[hashCode % bs.length] = 1;
    }

    private static boolean isNotExist(int hashCode) {
        int index = hashCode % bs.length;
        return bs[index] == 0;
    }


    private static void sogusAttributeLock() {
        BogusRedis bogusRedis = new BogusRedis();
        bogusRedis.lock();

        for (; ; ) {

        }
    }

    static class BogusRedis {
        private volatile int state = 0;
        private long expire;

        public BogusRedis() {
            expire = 3;
        }

        public BogusRedis(long expire) {
            this.expire = expire;
        }

        public boolean lock() {
            while (state == 1) {
            }
            state = 1;
            threadMonitor();//
            return true;
        }

        private void threadMonitor() {
            Thread current = Thread.currentThread();
            AtomicLong l = new AtomicLong(0l);
            new Thread(() -> {
                while (current.isAlive()) {
                    try {
                        if (expire <= (DateUtils.addMinutes(new Date(), 3)).getTime()) {
                            Date date = DateUtils.addMinutes(new Date(), 3);
                            expire = date.getTime();
                            System.out.println("当前时间为 :" + DateFormatUtils.format(new Date(), "YYYY-MM-dd HH:mm:ss"));
                            System.out.println("过期时间为 :" + DateFormatUtils.format(date, "YYYY-MM-dd HH:mm:ss"));
                        }
                        TimeUnit.SECONDS.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(current.getName() + " has result !");
            }).start();
        }

        public boolean unLock() {
            if (state == 0) {
                state = 0;
                return true;
            }
            return false;
        }
    }


    /**
     * 得到网页中图片的地址
     */
    public static List<String> getImgStr(String htmlStr) {
        String img = "";
        Pattern p_image;
        Matcher m_image;
        List<String> pics = new ArrayList<String>();
        String regEx_img = "<img.*src=(.*?)[^>]*?>"; //图片链接地址
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(htmlStr);
        while (m_image.find()) {
            img = img + "," + m_image.group();
            Matcher m = Pattern.compile("src=\"?(.*?)(\"|>|\\s+)").matcher(img);
            //匹配src
            while (m.find()) {
                pics.add(m.group(1));
            }
        }
        return pics;
    }       //重点在于正则表达式 <img.*src=(.*?)[^>]*?>        //               src=\"?(.*?)(\"|>|\\s+)
}




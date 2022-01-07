package com.fresh.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

public class BeanSQLUtils {

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
        propertyMap.put("bigint", "\t\t\t\tpublic java.lang.Long %s ; \n");
        propertyMap.put("varchar", "\t\t\t\tpublic java.lang.String %s ; \n");
        propertyMap.put("char", "\t\t\t\tpublic java.lang.String %s ; \n");
        propertyMap.put("int", "\t\t\t\tpublic java.lang.Integer %s ; \n");
        propertyMap.put("datetime", "\t\t\t\tpublic java.util.Date %s ; \n");
        propertyMap.put("date", "\t\t\t\tpublic java.util.Date %s ; \n");
        propertyMap.put("decimal", "\t\t\t\tpublic java.math.BigDecimal %s ; \n");
        propertyMap.put("bit", "\t\t\t\tpublic java.lang.Boolean %s ; \n");
        propertyMap.put("double", "\t\t\t\tpublic java.lang.Double %s ; \n");
        propertyMap.put("float", "\t\t\t\tpublic java.lang.Float %s ; \n");
    }

    public static void main(String[] args) throws ClassNotFoundException {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE `kxmall_activity_coupon` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `activity_id` bigint(20) DEFAULT NULL COMMENT 'remark:活动主表id',\n" +
                "  `coupon_id` bigint(20) DEFAULT NULL COMMENT 'remark:优惠券id',\n" +
                "  `min_money` int(4) DEFAULT NULL COMMENT 'remark:最低消费',\n" +
                "  `num_limit` int(4) DEFAULT NULL COMMENT 'remark:每日限量（0表示不限量）',\n" +
                "  `man_limit` int(4) DEFAULT NULL COMMENT 'remark:每人限领(0表示不限）',\n" +
                "  `num_send` int(4) DEFAULT NULL COMMENT 'remark:今日优惠券已发送的量',\n" +
                "  `gmt_update` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',\n" +
                "  `gmt_create` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=COMPACT;");


        System.out.println(toBeanContent(sb.toString()));
    }


    /**
     * 转SQL脚本操作
     *
     * @param list 实体类的 完整路径的集合
     * @throws ClassNotFoundException
     */
    public static void toMysqlScript(List<String> list) throws ClassNotFoundException {
        for (String item : list) {
            Class<?> aClass = Class.forName(item);
            String simpleName = aClass.getSimpleName();
            String tableName = toHump(simpleName, false);
            StringBuffer sb = new StringBuffer();
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field field : declaredFields) {
                sb.append(toMYSQLColumn(field.getType().getName(), toUnderscore(field.getName())));
            }
            sb.append(columnMap.get("primaryKey"));
            System.err.println(String.format("CREATE TABLE  `%s` (\n%s ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;\n", toUnderscore(tableName), sb.toString()));
        }
    }

    /**
     * 下划线转驼峰
     *
     * @param target
     * @param isTableName 是否表名称 默认false
     * @return
     */
    public static String toHump(String target, Boolean isTableName) {
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
                if (j == 0) {
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
     *
     * @param property
     * @return
     */
    public static String toUnderscore(String property) {// 下划线
        char[] chars = property.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            char aChar = chars[i];
            if (Character.isUpperCase(aChar)) {
                sb.append(i == 0 ? "" : "_");
                sb.append(Character.toLowerCase(aChar));
                continue;
            }
            sb.append(aChar);
        }
        return sb.toString();
    }

    /**
     * 生成每一个字段的SQL 语句String 形如： `user_name` varchar(255) DEFAULT NULL,
     *
     * @param name
     * @param fieldName
     * @return
     */
    public static String toMYSQLColumn(String name, String fieldName) {
        return String.format(columnMap.get(name), fieldName);
    }

    /**
     * 获取完整的类名 形如：com.xx.entity.Users
     *
     * @param path
     * @return
     */
    public static ArrayList<String> getCompleteClassName(String path) {
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
     * SQL 转 java Bean
     *
     * @param sql
     * @return
     */
    public static String toBeanContent(String sql) {
        StringBuffer buffer = new StringBuffer();
        String tableName = sql.substring(sql.indexOf("`"), sql.indexOf("(")).trim().replace("`", "");// 获取表名
        String beanName = toHump(tableName, true);

        String columns = sql.substring(sql.indexOf("(") + 1, sql.lastIndexOf(")"));// 获取字段内容部分
        String[] propertyList = columns.split("\n");// 分割
        StringBuffer pf = new StringBuffer();
        for (String item : propertyList) {
            String result = toPropertyContent(item);
            if (StringUtils.isNotBlank(result)) {
                pf.append(result);
            }
        }
        buffer.append(String.format("public class %s { \n %s \n }", beanName, pf));
        return buffer.toString();
    }

    /**
     * 将每一条sql 提取java属性类型 形如：`user_name` varchar(255) DEFAULT NULL, 提取为 public String userName;
     *
     * @param columnContent
     * @return
     */
    public static String toPropertyContent(String columnContent) {
        if (columnContent.indexOf("`") != -1) {
            String result = columnContent.substring(columnContent.indexOf("`") + 1, columnContent.lastIndexOf("`"));
            Set<String> nameSet = propertyMap.keySet();
            Iterator<String> iterator = nameSet.iterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                if (columnContent.contains(next)) {
                    return String.format(propertyMap.get(next), toHump(result, false));
                }
            }
        }
        return null;
    }


}

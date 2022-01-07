package com.fresh.dao;

import cn.hutool.core.date.DateUtil;
import com.fresh.utils.BeanSQLUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommonRepository<T, P> implements Base<T, P> {

    private Logger log = LoggerFactory.getLogger(" CommonRepository :");
    private String tableName = null;
    private List<String> columnNameList = null;
    private List<String> columnValueList = null;

    public CommonRepository() {
        columnNameList = new ArrayList<>();
        columnValueList = new ArrayList<>();
    }

    private String getTableName(T t) {
        if (tableName != null) {
            return tableName;
        }
        String className = t.getClass().getSimpleName();
        className = className.substring(className.lastIndexOf(".") + 1);
        String tableName = BeanSQLUtils.toUnderscore(className);
        this.tableName = tableName;
        return tableName;
    }

    private String getColumnName(T t) {
        StringBuffer sb = new StringBuffer();
        if (columnNameList != null && columnNameList.size() > 0) {
            columnNameList.forEach(value -> {
                sb.append(",`");
                sb.append(BeanSQLUtils.toUnderscore(value));
                sb.append("`");
            });
            return sb.toString().replaceFirst(",", "");
        }
        Class<?> aClass = t.getClass();
        Field[] fields = aClass.getDeclaredFields();
        if (fields.length == 0)
            return null;

        for (Field field : fields) {
            sb.append(",`");
            sb.append(BeanSQLUtils.toUnderscore(field.getName()));
            sb.append("`");
        }
        return sb.toString().replaceFirst(",", "");
    }

    private String getColumnValue(T t) throws InvocationTargetException, IllegalAccessException {
        StringBuffer sb = new StringBuffer();
        if (columnValueList != null && columnValueList.size() > 0) {
            for (String value : columnValueList) {
                sb.append(",`");
                sb.append(value);
                sb.append("`");
            }
            return sb.toString().replaceFirst(",", "");
        }

        Class<?> aClass = t.getClass();
        Method[] methods = aClass.getDeclaredMethods();
        Field[] fields = aClass.getDeclaredFields();

        // 此处保证顺序
        for (Field field : fields) {
            System.out.println(field.getName());

            for (Method method : methods) {
                if (method.getName().contains("get") && method.getName().toLowerCase().substring(3).contains(field.getName().toLowerCase())) {
                    System.err.println(method.getName().toLowerCase().substring(3));
                    Object invoke = method.invoke(t);
                    columnValueList.add(formatValue(invoke));
                }
            }
        }
        for (String value : columnValueList) {
            sb.append(",`");
            sb.append(value);
            sb.append("`");
        }

        return sb.toString().replaceFirst(",", "");
    }

    private String formatValue(Object invoke) {
        if (invoke == null) {
            return "null";
        }
        if (invoke instanceof java.util.Date) return DateUtil.format((Date) invoke, "YYYY-MM-dd HH:mm:ss");
        if (invoke instanceof java.lang.String) return invoke.toString();
        if (invoke instanceof java.lang.Integer) return String.valueOf(invoke);
        if (invoke instanceof java.lang.Long) return String.valueOf(invoke);
        if (invoke instanceof java.lang.Double) return String.valueOf(invoke);
        if (invoke instanceof java.lang.Boolean) return (Boolean) invoke == true ? "1" : "0";
        if (invoke instanceof java.lang.Float) return String.valueOf(invoke);
        if (invoke instanceof java.math.BigDecimal) return invoke.toString();
        return "null";
    }


    @Override
    public boolean save(T t) throws InvocationTargetException, IllegalAccessException {
        String tableName = getTableName(t);
        String columnsName = getColumnName(t);
        String values = getColumnValue(t);
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("INSERT INTO `%s` ( %s ) ", tableName, columnsName));
        sb.append(String.format("VALUES ( %s ) ", values));
        log.warn("save :");
        log.warn(sb.toString());
        return false;
    }

    @Override
    public boolean update(T t) {
        return false;
    }

    @Override
    public boolean delete(Class<?> t,P p) {
        String format = String.format("DELETE FROM `%s` WHERE id = %s ", BeanSQLUtils.toUnderscore(t.getSimpleName()), p);
        log.warn(format);
        return false;
    }

    @Override
    public T selectByPrimaryKey(P p) {
        return null;
    }

    @Override
    public List<T> find(T t) throws InvocationTargetException, IllegalAccessException {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("SELECT %s FROM %s  ",getColumnName(t),getTableName(t)));
        String condition = parseCondition(t);
        if (condition.length() > 0) {
            sb.append(" WHERE ");
        }
        sb.append(condition);
        log.warn(sb.toString());
        return null;
    }

    private String parseCondition(T t) throws InvocationTargetException, IllegalAccessException {
        StringBuffer sb = new StringBuffer();
        Class<?> aClass = t.getClass();
        Method[] methods = aClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().contains("get")) {
                Object invoke = method.invoke(t);
                if (invoke != null) {
                    String name = method.getName().toLowerCase().substring(3);
                    sb.append(" AND ");
                    sb.append(name);
                    sb.append(" = ");
                    sb.append(invoke);
                }
            }
        }

        return sb.toString();
    }
}

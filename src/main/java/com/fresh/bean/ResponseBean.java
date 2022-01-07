package com.fresh.bean;

import java.util.Collection;

public final class ResponseBean<T> {
    // 自定义应用状态码（不是响应状态码）
    private int code;
    // 描述信息
    private String msg;
    // 附带数据
    private Collection<T> data;

    public static ResponseBean success() {
        ResponseBean bean = new ResponseBean();
        bean.code = 0;
        bean.msg = "success";
        return bean;
    }
    public static <V> ResponseBean success(Collection<V> data) {
        ResponseBean bean = new ResponseBean();
        bean.code = 0;
        bean.msg = "success";
        bean.data = data;
        return bean;
    }
    public static ResponseBean error(final int code,final String msg) {
        ResponseBean bean = new ResponseBean();
        bean.code = code;
        bean.msg = msg;
        return bean;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Collection<T> getData() {
        return data;
    }

    @Override
    public String toString() {
        return String.format("{code: %d,msg: %s,data: %s}", this.code, this.msg, this.data);
    }
}
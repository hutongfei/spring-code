package com.fresh.mvc;

import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * springMVc 前端数据转换处理器
 */
@Component
public class ConverterComponent implements Converter<String,Date> {

    @Override
    public Converter andThen(Converter after) {
        return Converter.super.andThen(after);
    }

    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    public Date convert(String  o) {
        if (o == null) return null;

        if (o instanceof String ) {
            try {
                return simpleDateFormat.parse(String.valueOf(o));
            } catch (ParseException e) {
                return null;
            }
        }
        return null;
    }
}

package com.fresh.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface Base<T, P> {

    boolean save(T t) throws InvocationTargetException, IllegalAccessException;

    boolean update(T t);

    boolean delete(Class<?> t,P p);

    T selectByPrimaryKey(P p);

    List<T> find(T t) throws InvocationTargetException, IllegalAccessException;
}

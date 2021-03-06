package com.moon.util.able;

import java.util.Iterator;

/**
 * @author benshaoye
 */
@FunctionalInterface
public interface IteratorAble<T> {
    /**
     * 获取一个迭代器
     *
     * @param t
     * @return
     */
    Iterator<T> iterator(T t);
}

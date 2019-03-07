package com.jhr.utils.base;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * <br>
 * 标题：
 * 描述：
 *
 * @author jhr
 * @create 2019/03/07 14:24
 */
public class BaseRsp<T> extends BaseRspHeader {
    private T data;

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}

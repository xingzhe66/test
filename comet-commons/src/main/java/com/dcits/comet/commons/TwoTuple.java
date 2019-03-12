package com.dcits.comet.commons;

/**
 * @Author guihj
 * @Description //定义一个二元组，用于返回检查信息
 * @Date 2019-03-05 11:14
 * @Version 1.0
 **/
public class TwoTuple<A, B> {
    public final A first;
    public final B second;

    public TwoTuple(A a, B b) {
        first = a;
        second = b;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}

package com.dcits.comet.commons.flow;

/**
 * @Author chengliang
 * @Description //TODO
 * @Date 2019-02-26 18:14
 * @Version 1.0
 **/
public interface IFlow<I, O> {

    O handle(I i);
}

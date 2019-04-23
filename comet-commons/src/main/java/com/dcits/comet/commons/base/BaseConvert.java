package com.dcits.comet.commons.base;

import com.dcits.comet.commons.Context;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @Author chengliang
 * @Description //TODO
 * @Date 2019-02-21 10:29
 * @Version 1.0
 **/
public interface BaseConvert<SOURCE, TARGET> {
    @Mappings({})
    @InheritConfiguration
    TARGET to(SOURCE var1) ;

    @InheritConfiguration
    List<TARGET> to(List<SOURCE> var1);

    @InheritInverseConfiguration
    SOURCE from(TARGET var1);

    @InheritInverseConfiguration
    List<SOURCE> from(List<TARGET> var1);
}

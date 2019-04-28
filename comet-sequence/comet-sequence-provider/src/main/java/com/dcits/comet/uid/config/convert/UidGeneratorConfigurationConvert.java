package com.dcits.comet.uid.config.convert;

import com.dcits.comet.commons.base.BaseConvert;
import com.dcits.comet.uid.config.UidGeneratorProperties;
import com.zaxxer.hikari.HikariDataSource;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @ClassName Demo
 * @Author leijian
 * @Date 2019/4/5 22:10
 * @Description TODO
 * @Version 1.0
 **/
@Mapper()
public interface UidGeneratorConfigurationConvert extends BaseConvert<UidGeneratorProperties, HikariDataSource> {

    UidGeneratorConfigurationConvert INSTANCE = Mappers.getMapper(UidGeneratorConfigurationConvert.class);
}

package com.dcits.comet.mybatis.generator.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
@Mapper
public interface CodeGeneratorMapper {
    /**
     * 获取列数据
     *
     * @param
     * @return
     */
    public List<Map> getListMap(Map<String, Object> params);
    /**
     * 获取所有的表名称和注释
     *
     * @return
     */
    public List<Map<String, Object>> getTablesList(Map<String, Object> params);
    /**
     * 获取指定表的注释
     *
     * @return
     */
    public String getTableComment(Map<String, Object> params);
    /**
     * 获取指定表的主键
     *
     * @return
     */
    public List<String>  getTableKeys(Map<String, Object> params);


}

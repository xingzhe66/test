<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${module.namespace}">
    <resultMap id="BaseResultMap" type="${module.namespace}">
        <#assign keys=module.propMap?keys>
        <#list keys as key>
            <#assign map=module.propMap[key]>
            <result column="${map.dbname}" property="${key}"  jdbcType="${map.type}" />
        </#list>
        <#--<result column="BANK_NO" property="bankNo" />-->
        <#--<result column="ABST_CODE" property="abstCode" />-->
        <#--<result column="ABST_NAME" property="abstName" />-->
        <#--<result column="ABST_KIND" property="abstKind" />-->
        <#--<result column="REMARK" property="remark" />-->
        <#--<result column="VALID_STAT" property="validStat" />-->
        <#--<result column="TXN_OPER_NO" property="txnOperNo" />-->
        <#--<result column="AUTH_OPER_NO" property="authOperNo" />-->
        <#--<result column="APPR_OPER_NO" property="apprOperNo" />-->
        <#--<result column="CREAT_TIME" property="creatTime" jdbcType="TIMESTAMP"  />-->
        <#--<result column="MODIFY_TIME" property="modifyTime" jdbcType="TIMESTAMP"  />-->
    </resultMap>

    <sql id="Table_Name">
        ${module.tableName}
    </sql>

    <sql id="Base_Column">
        ${module.tableColumns}
    </sql>

    <sql id="Base_Where">
        <trim suffixOverrides="AND">
            <#assign keys=module.propMap?keys>
            <#list keys as key>
                <if test="${key} != null and ${key} != ''">
                   ${module.propMap[key].dbname} = ${"#"}{${key}} AND
                </if>
            </#list>
        </trim>
    </sql>

    <sql id="Base_Select">
        select
        <include refid="Base_Column" />
        from
        <include refid="Table_Name" />
        <where>
            <include refid="Base_Where" />
        </where>
    </sql>



    <insert id="insert" >
        insert into
        <include refid="Table_Name" />
        <trim prefix="(" suffix=")" suffixOverrides=",">

            <#assign keys=module.propMap?keys>
            <#list keys as key>
                <if test="${key} != null">
                    ${module.propMap[key].dbname},
                </if>
            </#list>
        </trim>
        <trim prefix="values (" suffix=")" suffixOverrides=",">
            <#assign keys=module.propMap?keys>
            <#list keys as key>
                <if test="${key} != null">
                    ${"#"}{${key}},
                </if>
            </#list>
        </trim>
    </insert>

    <update id="update" >
        update
        <include refid="Table_Name" />
        <set>
            <#assign keys=module.propMap?keys>
            <#list keys as key>
                <if test="${key} != null">
                    ${module.propMap[key].dbname}=${"#"}{${key}},
                </if>
            </#list>
        </set>
        <where>
            <include refid="Base_Where" />
        </where>
    </update>

    <update id="updateByEntity" >
        UPDATE
        <include refid="Table_Name" />
        <set>
            <#assign keys=module.propMap?keys>
            <#list keys as key>
                <if test="s.${key} != null">
                    ${module.propMap[key].dbname}=${"#"}{s.${key}},
                </if>
            </#list>
        </set>
        <where>
            <trim prefix="(" suffix=")" suffixOverrides="AND">
                <#assign keys=module.propMap?keys>
                <#list keys as key>
                    <if test="w.${key} != null">
                        ${module.propMap[key].dbname}=${"#"}{w.${key}}
                        AND
                    </if>
                </#list>
            </trim>
        </where>
    </update>

    <delete id="delete" >
        delete from
        <include refid="Table_Name" />
        <where>
            <include refid="Base_Where" />
        </where>
    </delete>

    <select id="count" parameterType="java.util.Map" resultType="int">
        select count(1) from
        <include refid="Table_Name" />
        <where>
            <include refid="Base_Where" />
        </where>
    </select>

    <select id="selectOne"  resultMap="BaseResultMap">
        <include refid="Base_Select" />
    </select>

    <select id="selectList"  resultMap="BaseResultMap">
        <include refid="Base_Select" />
    </select>

    <select id="selectForUpdate" resultMap="BaseResultMap" useCache="false">
        <include refid="Base_Select" />
        for update
    </select>


</mapper>
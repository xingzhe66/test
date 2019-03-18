# 彗星
> 分布式核心应用框架

## comet-base
> 基础:包含classLoader和package扫描等底层功能。

## comet-batch
> 批量模块: 基于spring-batch封装。
- 批量调度与step分离。
- step实现与调起方式分离。
    - 定时任务调起。
    - eod调度平台调起。
    - 联机调起。
- step在一个类中实现，开发简单。
- 配置简单。使用@BatchConfig注解和配置文件方式，取消xml配置。

## comet-batch-service
> 批量服务模块: 提供调度平台，联机调起所需的对外集成服务接口。

## comet-boot-dependencies
> 依赖管理模块: 所有模块的根工程，管理依赖版本，插件版本。

## comet-cache
> 缓存模块

## comet-commons
> 公共模块: 技术，业务公共组件

## comet-dao
> 共用dao实现

## comet-dbsharding
> 数据库分片

## comet-gateway
> 网关模块

## comet-mq
> MQ模块

## comet-mybatis-generator
> mybatis代码生成模块

## comet-registry
> 服务注册: 封装eureka

## comet-rpc
> rpc模块: 封装spring-cloud-netflix和openfeign等

## comet-sequence
> 全局序列模块

## comet-starter
> 启动器模块

## comet-test
> 测试模块
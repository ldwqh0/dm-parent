## 版本说明

基于Spring-cloud Hoxton ,Spring boot 2.3.x版本构建 版本更新规则。

* 升级spring-boot/spring-cloud等依赖库的大中小版本号，大中小版本号+1。
* 增加小的功能模块时，中版本+1
* bug修复发布时，小版本+1

## 升级日志

1.2.3

* 升级spring boot 至 2.3.7.release
* 添加typescript-generator-maven-plugin
* 升级querydsl至2.4.0
* 升级hibernate至5.4.27

1.2.2

* 修改角色和菜单dto的定义，增加菜单初始化方法
* 添加DataForbiddenException,用户表示权限不足异常
* 升级spring-boot至2.3.6.RELEASE版本
* 升级spring-cloud至Hoxton.SR9版本 1.2.0
* 升级spring-boot至2.3.3.RELEASE版本
* 升级spring-cloud至Hoxton.SR8版本 1.1.0
* 重构oauth-server模块，添加openid实现
* 升级spring-boot至2.2.9.RELEASE
* 添加多数据源功能模块

## 版本对应规则

构建版本号|Spring Boot|Spring Cloud|
:-|:-|:-|:- 1.0.x|2.2.7.RELEASE|Hoxton.SR6|终止 1.1.x|2.2.x.RELEASE|Hoxton.SRx|持续中 1.1.0|2.2.9.RELEASE|Hoxton.SR6|终止
1.1.1|2.2.9.RELEASE|Hoxton.SR8|持续中

1.2.x|2.3.x.RELEASE|Hoxton.SR8|持续中 1.2.0|2.3.3.RELEASE|Hoxton.SR8|终止 1.2.1|2.3.4.RELEASE|Hoxton.SR8|终止
1.2.2|2.3.6.RELEASE|Hoxton.SR9|持续中

## 模块说明

* dm-authority-common

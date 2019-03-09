# base-mybatis-generate


1、不同项目目录修改 Constants 常量类中的 BASE_PACKAGE 常量值

2、修改包名，例如 com.tiger.rabbitmq --> com.xx.xx

3、修改 generatorConfig-log.xml 中的 <!-- 批量替换 【tiger.rabbitmq】 -->

4、修改generatorConfig-log.xml 数据源 【jdbcConnection】

5、手动创建一个目录结构，例如 【C:\AAAAA\base-mybatis-generator-out\src\main\java】

6、执行 LifeHallGenerator mian 函数两次，代码生成在手动创建的目录下


------------- 使用 generator 代码生成的项目注意修改的地方-------------
1、修改 yml 文件配置
mybatis:
        mapperLocations: classpath*:com/tiger/rabbitmq/**/dao/**/*Mapper.xml

2、修改 mybatis-config.xml 文件
<plugin interceptor="com.tiger.rabbitmq.mybatis.query.PageInterceptor"></plugin>

3、修改 QueryGenerateMapper.xml 文件
<mapper namespace="com.tiger.rabbitmq.mybatis.dao.mapper.QueryGenerateMapper">

4、修改 启动类注解信息：
@ComponentScan(basePackages = "com.tiger.rabbitmq")
@MapperScan(annotationClass = Repository.class, basePackages = { "com.tiger.rabbitmq"})

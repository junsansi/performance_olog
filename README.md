# performance_olog
Using Spring interceptor capture function execute infomation!

基于Spring拦截器捕获应用中方法的执行时间。

此项实现对于业务代码侵入性极小，无须应用端发动任何现有代码，只需要引入本工程，稍做配置，即可对现有应用方法的执行时间进行记录，后续即可通过这些统计信息，分析排查和诊断优化。

应用场景广泛，即可以视为是一个最简版的APM，也可以做为应用层的慢查询日志模块。


应用方式主要有下列四个步骤：

1、配置基础依赖

可以直接引用jss_perf_olog工程，或者将之打为jar包，保存至目标工程的lib目录。

2、将applicationContext-aop-perf-log.xml文件置于适当路径

配置您的工程引用Spring上下文配置文件路径，使其能够加载applicationContext-aop-perf-log.xml中的配置(参考example/src/main/resources/spring/applicationContext-aop-perf-log.xml文件)。
注意，对于applicationContext-aop-perf-log.xml文件来说，也需要进行少许的配置，重点关注pointcut中的配置，用于指定切入的方法，在本例中拦截包名以com.jss.test开头的所有controller。

3、配置logback，指定日志文件输出路径

只需要修改日志输出级别和日志文件的保存路径即可(参考example/src/main/resources/logback.xml文件)。

4、配置perf_log.properties文件

该文件主要用于指定日志输出的一些控制参数，包括是否启用、日志采集频率，日志输出路径等等。考虑到记录和分析的便利，目前也支持写向kafka队列或redis。


感兴趣的朋友可以参考example工程，将该目录同步到本地，导出为maven工程即可运行。



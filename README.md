### 一、Spark是什么

**Apache Spark™** is a unified analytics engine for large-scale data processing.

Spark是处理大规模数据的统一计算引擎。

1、速度快：Spark使用先进的DAG调度程序，在进行MR计算时，内存计算时hadoop的百倍，磁盘计算时的10倍，并可实现高性能的流数据处理能力。

2、易用性：提供了80+高级算子，易于构建并行App；支持Java，Scala，Python，R，SQL；spark-shell和spark-sql工具，方便入门和编程调试。

3、集成性：Spark可用于批处理（SparkCore），交互式查询（SparkSQL），实时流处理（SparkStreaming），机器学习（Spark MLlib），图计算（GraphX）。这些应用可在同一个应用中集成使用，形成一个统一的需求解决方案。减少开发成本，减小维护难度。

4、兼容性：Spark可运行在Yarn，Mesos，Standalone，Kubernetes，Cloud之上；Spark可直接操作Hadoop，Hive，Hbase，Kafka等中的数据，和大数据生态圈设计的技术组件无缝衔接使用。



### 二、Spark能做什么

1、Spark全面兼容Hadoop生态系统，包含Hive，Kafka，Hbase等

2、大规模数据做OLAP的理想工具

3、Spark支持SQL，支持Hive函数，支持udf

4、Spark可以做实时计算引擎

5、机器学习和图计算

6、Spark支持多种数据源，包括Hive，Jdbc，File，Kafka等



### 三、我们用Spark做什么

1、大规模数据ETL工具

2、大规模数据离线分析工具

3、实时数据分析处理工具



### 四、Spark各个模块

1、Spark Core

​	SparkCore实现了Spark的基本功能，包含任务调度，内存管理，错误恢复，与存储系统交互等基础核心模块。

2、Spark SQL

​	SparkSQL是Spark操作结构化数据的程序包，我们可以通过SQL来操作数据。SparkSQL支持多种数据源，包括Hive，Parquet，Json，Jdbc等。

3、Spark Streaming

​	SparkStreaming是对实时数据进行流式计算的组件。常与Kafka结合使用。

4、Spark MLlib

​	Spark关于机器学习的包，包含多种算法，如聚类，分类，回归，协同过滤等。

5、GraphX

​	进行图计算的组件。

6、集群管理器

​	常用的，如Standalone，Yarn，Mesos等。

![](.\spark基础-spark模块概览.png)


RUN apt update && apt install wget -y && \
    wget http://sd-127206.dedibox.fr/hagimont/software/hadoop-2.7.1.tar.gz && \
    wget http://sd-127206.dedibox.fr/hagimont/software/spark-2.4.3-bin-hadoop2.7.tgz && \
    tar xzf hadoop-2.7.1.tar.gz && \
    tar xzf spark-2.4.3-bin-hadoop2.7.tgz && \
    rm jdk-8u221-linux-x64.tar.gz hadoop-2.7.1.tar.gz spark-2.4.3-bin-hadoop2.7.tgz

ENV HADOOP_HOME=/ec2/hadoop-2.7.1
ENV SPARK_HOME=/ec2/spark-2.4.3-bin-hadoop2.7
ENV PATH=$PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin:$SPARK_HOME/bin:$SPARK_HOME/sbin



# [作者](https://github.com/CH00SE1/)  说明
# maven本地打包
```aidl
<dependency>
    <groupId>com.lshaox</groupId>
    <artifactId>m3u8</artifactId>
    <version>1.1.0</version>
</dependency>
```

# 本地maven打包
```aidl
mvn install:install-file -Dfile=m3u8Download.jar -DgroupId=org.lshaox -DartifactId=m3u8 -Dversion=1.1.0 -Dpackaging=jar
```
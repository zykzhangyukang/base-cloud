# base-cloud
微服务手脚架


#### 启动参数

##### gateway

```text
-Dspring.profiles.active=dev
-Dspring.application.name=base-gateway
-Ddomain=gateway
-Dlog.file=D:\log\springboot\gateway
-Dspring.cloud.nacos.discovery.server-addr=http://coderman.love:8848
-Dspring.cloud.nacos.discovery.namespace=c9d135e5-095d-43b7-b94a-2e40f79d82b1
-Dspring.cloud.nacos.config.server-addr=http://coderman.love:8848
-Dspring.cloud.nacos.config.file-extension=yaml
-Dspring.cloud.nacos.config.namespace=c9d135e5-095d-43b7-b94a-2e40f79d82b1
-Dspring.cloud.nacos.config.shared-configs[0]=base-common.yml
-Dspring.main.allow-bean-definition-overriding=true
```


##### Job

```text
-Dspring.profiles.active=dev
-Dspring.application.name=base-job
-Ddomain=job
-Dlog.file=D:\log\springboot\job
-Dspring.cloud.nacos.discovery.server-addr=http://coderman.love:8848
-Dspring.cloud.nacos.discovery.namespace=c9d135e5-095d-43b7-b94a-2e40f79d82b1
-Dspring.cloud.nacos.config.server-addr=http://coderman.love:8848
-Dspring.cloud.nacos.config.file-extension=yaml
-Dspring.cloud.nacos.config.namespace=c9d135e5-095d-43b7-b94a-2e40f79d82b1
-Dspring.cloud.nacos.config.shared-configs[0]=base-common.yml
-Dspring.main.allow-bean-definition-overriding=true
```

```shell script
java -Dspring.application.name=base-job -Dspring.profiles.active=dev -Ddomain=job -Dlog.file=/root/app/logs/job -Dspring.cloud.nacos.discovery.server-addr=http://coderman.love:8848 -Dspring.cloud.nacos.discovery.namespace=c9d135e5-095d-43b7-b94a-2e40f79d82b1 -Dspring.cloud.nacos.config.server-addr=http://coderman.love:8848 -Dspring.cloud.nacos.config.file-extension=yaml -Dspring.cloud.nacos.config.namespace=c9d135e5-095d-43b7-b94a-2e40f79d82b1 -Dspring.cloud.nacos.config.shared-configs[0]=base-common.yml -Dspring.main.allow-bean-definition-overriding=true -jar base-job-1.0.0-DEV-CLOUD-SNAPSHOT.jar
```

##### auth

```text
-Dspring.profiles.active=dev
-Dspring.application.name=base-auth
-Ddomain=auth
-Dlog.file=D:\log\springboot\auth
-Dspring.cloud.nacos.discovery.server-addr=http://coderman.love:8848
-Dspring.cloud.nacos.discovery.namespace=c9d135e5-095d-43b7-b94a-2e40f79d82b1
-Dspring.cloud.nacos.config.server-addr=http://coderman.love:8848
-Dspring.cloud.nacos.config.file-extension=yaml
-Dspring.cloud.nacos.config.namespace=c9d135e5-095d-43b7-b94a-2e40f79d82b1
-Dspring.cloud.nacos.config.shared-configs[0]=base-common.yml
-Dspring.main.allow-bean-definition-overriding=true
```

```shell script
java -Dspring.application.name=base-auth -Dspring.profiles.active=dev -Ddomain=auth -Dlog.file=/root/app/logs/auth -Dspring.cloud.nacos.discovery.server-addr=http://coderman.love:8848 -Dspring.cloud.nacos.discovery.namespace=c9d135e5-095d-43b7-b94a-2e40f79d82b1 -Dspring.cloud.nacos.config.server-addr=http://coderman.love:8848 -Dspring.cloud.nacos.config.file-extension=yaml -Dspring.cloud.nacos.config.namespace=c9d135e5-095d-43b7-b94a-2e40f79d82b1 -Dspring.cloud.nacos.config.shared-configs[0]=base-common.yml -Dspring.main.allow-bean-definition-overriding=true -jar base-auth-web-1.0.0-DEV-CLOUD-SNAPSHOT.jar
```


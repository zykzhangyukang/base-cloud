# base-cloud
微服务手脚架


#### 启动参数

##### gateway

```text
-Dspring.application.name=base-gateway
-Dserver.port=9999
-Dspring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
-Dspring.cloud.nacos.discovery.namespace=b50c7b27-3ad4-4ce2-8157-4b1b8b1e8222
-Dspring.cloud.nacos.config.server-addr=127.0.0.1:8848
-Dspring.cloud.nacos.config.file-extension=yaml
-Dspring.cloud.nacos.config.namespace=b50c7b27-3ad4-4ce2-8157-4b1b8b1e8222
-Dspring.cloud.nacos.config.shared-configs[0]=base-common.yaml
-Dspring.main.allow-bean-definition-overriding=true
```


#### Job

```text
-Dspring.application.name=base-job
-Dserver.port=8057
-Dserver.servlet.context-path=/xxl-job-admin
-Dspring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
-Dspring.cloud.nacos.discovery.metadata.management.context-path=/xxl-job-admin/actuator
-Dspring.cloud.nacos.discovery.namespace=b50c7b27-3ad4-4ce2-8157-4b1b8b1e8222
-Dspring.cloud.nacos.config.server-addr=127.0.0.1:8848
-Dspring.cloud.nacos.config.file-extension=yaml
-Dspring.cloud.nacos.config.namespace=b50c7b27-3ad4-4ce2-8157-4b1b8b1e8222
-Dspring.main.allow-bean-definition-overriding=true
```

#### auth

```text
-Dspring.application.name=base-auth
-Dserver.port=8989
-Dspring.profiles.active=dev
-Ddomain=auth
-Dlog.file=D:\log\springboot\auth
-Dspring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
-Dspring.cloud.nacos.discovery.namespace=b50c7b27-3ad4-4ce2-8157-4b1b8b1e8222
-Dspring.cloud.nacos.config.server-addr=127.0.0.1:8848
-Dspring.cloud.nacos.config.file-extension=yaml
-Dspring.cloud.nacos.config.namespace=b50c7b27-3ad4-4ce2-8157-4b1b8b1e8222
-Dspring.cloud.nacos.config.shared-configs[0]=base-common.yml
-Dspring.main.allow-bean-definition-overriding=true
```
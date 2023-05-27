# base-cloud
微服务手脚架


#### 启动参数

##### gateway

```text
 -Dspring.application.name=base-gateway
 -Dserver.port=9999
 -Dspring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
 -Dspring.cloud.nacos.discovery.namespace=fb7c24ee-5491-4ef5-9423-ecb04ce83c27
 -Dspring.cloud.nacos.config.server-addr=127.0.0.1:8848
 -Dspring.cloud.nacos.config.file-extension=yml
 -Dspring.cloud.nacos.config.namespace=fb7c24ee-5491-4ef5-9423-ecb04ce83c27
 -Dspring.cloud.nacos.config.shared-configs[0]=base-common.yml
```


#### Job

```text
  -Dspring.application.name=base-job
  -Dserver.port=8057
  -Dserver.servlet.context-path=/xxl-job-admin
  -Dspring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
  -Dspring.cloud.nacos.discovery.metadata.management.context-path=/xxl-job-admin/actuator
  -Dspring.cloud.nacos.discovery.namespace=fb7c24ee-5491-4ef5-9423-ecb04ce83c27
  -Dspring.cloud.nacos.config.server-addr=127.0.0.1:8848
  -Dspring.cloud.nacos.config.file-extension=yml
  -Dspring.cloud.nacos.config.namespace=fb7c24ee-5491-4ef5-9423-ecb04ce83c27
```

#### auth

```text
-Dspring.application.name=base-auth
-Dserver.port=9999
-Dspring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
-Dspring.cloud.nacos.discovery.namespace=fb7c24ee-5491-4ef5-9423-ecb04ce83c27
-Dspring.cloud.nacos.config.server-addr=127.0.0.1:8848
-Dspring.cloud.nacos.config.file-extension=yml
-Dspring.cloud.nacos.config.namespace=fb7c24ee-5491-4ef5-9423-ecb04ce83c27
-Dspring.cloud.nacos.config.shared-configs[0]=base-common.yml
```
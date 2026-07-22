# 已完成任务

- 2026-07-22：初始化 docs/task.md、done.md、todo.md，建立进度同步机制。
- 2026-07-22：创建 Maven / Spring Boot 2.7.18 骨架（pom.xml、CensorApplication、application.properties），绑定 0.0.0.0:8080，启动时打印局域网访问地址。
- 2026-07-22：实现 censor.properties 默认映射、CensorConfig（@PropertySource + 长词优先）、CensorService.censor()。
- 2026-07-22：实现 CensorController（GET /、POST /censor）与 index.html「输入 → 和谐一下 → 展示」闭环。
- 2026-07-22：本地验证通过——`mvn package` 成功，服务监听 0.0.0.0:8080；样例「你妈的…傻逼…卧槽牛逼」和谐为「宁马的…沙雕…我焯牛福」。
- 2026-07-22：新增 README.md（营销向介绍 + 使用方式 + 部署/词库扩展说明）。
- 2026-07-22：前端 index.html 升级为营销风——痛点文案、前后对比、品牌标题与轻动效，保留「和谐一下」闭环。
- 2026-07-22：排查前端未更新——旧进程仍服务 target 旧模板；已重启并同步最新 index.html。

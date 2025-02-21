# auto-fishpi-active

> 该项目只是无聊产物，仅供学习

主要围绕着鱼排的一些接口和触发一些活跃条件，省去每日的一些繁杂事项, 帮你每日签到

## 使用方法

1. clone当前项目(`git clone` this repo)
2. `git pull -f` 更新

### 直接运行方式

打开idea或者其他ide工具运行`MainKt`

### 扩展运行方式

1. 打包 `mvn clean package -DskipTests`
2. 进入打包目录 `cd target` （下面任选其一）
   1. `console`模式运行
      1. shell 执行 `java -jar auto-fishpi-active-1.0-jar-with-dependencies.jar -console`
      2. 根据提示输入账号密码
   2. `command`模式运行
      1. shell 执行 `java -jar auto-fishpi-active-1.0-jar-with-dependencies.jar -username:[username] -password:[password] -mfaCode:[mfaCode]` （托管即可）
   3. `init` 模式运行
      1. shell 执行 `java -jar auto-fishpi-active-1.0-jar-with-dependencies.jar -init` 
         1. 打开执行的目录下生成一个`setting.yml`文件
         2. 编辑`fishpi-client`配置项下的`username`, `password`, `mfaCode`
      2. shell 执行 `java -jar auto-fishpi-active-1.0-jar-with-dependencies.jar` 运行

## 它能帮你干什么

1. 自动评论 参考`setting.yml`中的`community.watch-tags`、 `community.comment-texts`和 `jobs[name='auto-comment-article-job']` 配置项
2. 领取昨日活跃度奖励 参考`setting.yml`中的`jobs[name='receive-rewards-daily-job']` 配置项
3. 聊天室发送消息 **此配置默认并未配置（谨慎配置）** 参考`setting.yml`中的`jobs[name='auto-send-message-job']` 配置项`
4. 自动去阅读文章 参考`setting.yml`中的`jobs[name='auto-read-article-job']` 配置项
5. 自动点赞文章 参考`setting.yml`中的`jobs[name='auto-like-article-job']` 配置项
6. 聊天室指令
   1. `发红包 用户ID [红包金额(可选)] [消息(可选)]`
   2. `发猜拳 猜拳次数 [红包金额(可选)] [消息(可选)]`
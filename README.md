# auto-fishpi-active

> 该项目只是无聊产物，仅供学习

主要围绕着鱼排的一些接口和触发一些活跃条件，省去每日的一些繁杂事项

## 使用方法

1. clone当前项目(`git clone` this repo)
2. git pull 更新
3. 自定义`src/main/resources/setting.yml`(里面有详细注释, 又可以不修改)
   1. 如果是直接下载的jar包可用zip压缩工具打开找到`setting.yml`文件编辑后再放回去

### 直接运行法

打开idea或者其他ide工具

### 服务器部署法 (Linux)

1. 打包 `mvn clean package`
2. 进入打包目录 `cd target`
3. 开一个后台`screen -R auto-fishpi-active`
4. 运行`java -jar auto-fishpi-active-1.0-jar-with-dependencies.jar`
5. 输入正确账号密码后即可 `ctrl A + D` 切回

## 它能帮你干什么

1. 自动评论 (目前只是监听 新人报到 词条) 参考`setting.yml`中的`community.newcomer-welcome-messages`和 `jobs[name='auto-comment-article-job']` 配置项
2. 领取昨日活跃度奖励 参考`setting.yml`中的`jobs[name='receive-rewards-daily-job']` 配置项
3. 聊天室发送消息 **此配置默认并未配置（谨慎配置）** 参考`setting.yml`中的`jobs[name='auto-send-message-job']` 配置项`
4. 自动去阅读文章 参考`setting.yml`中的`jobs[name='auto-read-article-job']` 配置项
5. 自动点赞文章 参考`setting.yml`中的`jobs[name='auto-like-article-job']` 配置项
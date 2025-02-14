# auto-fishpi-active

> 该项目只是无聊产物，仅供学习

主要围绕着鱼排的一些接口和触发一些活跃条件，省去每日的一些繁杂事项

## 使用方法

1. clone当前项目(`git clone` this repo)
2. git pull 更新
3. 自定义`src/main/resources/setting.yml`(里面有详细注释, 又可以不修改)

### 直接运行法

打开idea或者其他ide工具

### 服务器部署法 (Linux)

1. 打包 `mvn clean package`
2. 进入打包目录 `cd target`
3. 开一个后台`screen -R auto-fishpi-active`
4. 运行`java -jar auto-fishpi-active-1.0-jar-with-dependencies.jar`
5. 输入正确账号密码后即可 `ctrl A + D` 切回

## 它能帮你干什么

1. 抢红包 发专属指令红包 (发红包 name money message)
2. 领取昨日活跃度
3. 触发一些机器人的奖励
4. 去阅读文章的活跃度 (某种意义上变相的每日签到)
5. 其他
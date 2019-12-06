### 发布
优化了一下发布
```shell script
ssh root@62.234.134.210
Seu@Zimotion6102
cd /root/dev/alumni_server/
#测试环境
sh start.sh
#生产环境
sh start-prd.sh

```
// TODO CICD

### 备忘

```shell script
#查看日志 /root/dev/alumni_server/
tail -f nohup.out
```

```查看nginx配置
vim /etc/nginx/conf.d/weapp.conf

```

服务器域名：https://www.seuclab.cn

### 代码交接

```
@RequestMapping("/test")



```
- 好友处理的逻辑




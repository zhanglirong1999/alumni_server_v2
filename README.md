### 发布
优化了一下发布
```shell script
ssh ubuntu@152.136.34.23
wzh@112386
cd /home/ubuntu/dev/alumni_server

git checkout develop

#测试环境
sh start.sh
#生产环境
sh start-prd.sh

```
// TODO CICD

### 备忘

```shell script
#查看日志 
tail -f nohup.out
``` 

```查看nginx配置
less /etc/nginx/conf.d/alumni.conf

```

服务器域名：https://www.seuclab.cn

#项目资料

UI：https://lanhuapp.com/url/hdUEK-VtbqL
接口文档：https://nei.netease.com/dashboard/

生产环境：https://www.seuclab.cn/v2/，数据库：alumnidb_prd;
开发&测试环境：https://www.seuclab.cn/test/v2/，数据库：alumnidb_dev


#实体表规范，参照阿里巴巴java开发手册

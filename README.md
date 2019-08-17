
### mybatis gennerator
执行 maven plugin ：mybatis-generator:gennerate,会直接生成dao层代码
这个不要再执行了，不然有些后来的改动就没了。。
//TODO 但是不会自动生成@Id,insertable = false等注解

### 发布
优化了一下发布
```
ssh root@62.234.134.210
Seu@Zimotion6102
cd /root/dev/alumni_server/
sh start.sh
```
// TODO CICD


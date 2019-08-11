
# mybatis gennerator
执行 maven plugin ：mybatis-generator:gennerate,会直接生成dao层代码
这个不要再执行了，不然有些后来的改动就没了。。
//TODO 但是不会自动生成@Id,insertable = false等注解

#发布
目前手动发布，本地 maven package 打成jar包，scp到root@62.234.134.210:/root/dev，ssh上去手动启动。

maven package
scp jar包 root@62.234.134.210:/root/dev
ssh root@62.234.134.210
kill 应用pid
java -jar alumni_server-0.0.1-SNAPSHOT.jar &
exit // 注意不要直接ctrl c，会停掉应用

// TODO CICD


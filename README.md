
#mybatis gennerator
执行 maven plugin ：

#发布
目前手动发布，本地 maven package 打成jar包，scp到root@62.234.134.210:/root/dev，ssh上去手动启动。

maven package
scp jar包 root@62.234.134.210:/root/dev
ssh root@62.234.134.210
kill 应用pid
java -jar alumni_server-0.0.1-SNAPSHOT.jar &
exit // 注意不要直接ctrl c，会停掉应用


package cn.edu.seu.alumni_server.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 用来表明如下一个接口都要按照 WebResponse 来返回
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface WebResponseAPIMethod {

}

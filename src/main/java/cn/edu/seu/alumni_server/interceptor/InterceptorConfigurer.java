package cn.edu.seu.alumni_server.interceptor;

import cn.edu.seu.alumni_server.interceptor.registration.RegistrationRequiredInterceptor;
import cn.edu.seu.alumni_server.interceptor.token.TokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class InterceptorConfigurer implements WebMvcConfigurer {

    @Bean
    RegistrationRequiredInterceptor registrationRequiredInterceptor() {
        return new RegistrationRequiredInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(this.registrationRequiredInterceptor()).addPathPatterns("/**");
    }
}

package com.polarplus.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.polarplus.infra.security.EmpresaInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final EmpresaInterceptor empresaInterceptor;
    private SecurityConfig securityConfig;

    public WebConfig(EmpresaInterceptor empresaInterceptor) {
        this.empresaInterceptor = empresaInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(empresaInterceptor).excludePathPatterns(securityConfig.getPublicRoutes())
                .addPathPatterns("/**");
    }

}

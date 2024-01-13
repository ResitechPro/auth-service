    package com.taskflow.config.web;

    import com.taskflow.config.intercepter.TenantInterceptor;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
    import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

    @Configuration
    public class WebConfig implements WebMvcConfigurer {

        private final TenantInterceptor tenantInterceptor;

        public WebConfig(TenantInterceptor tenantInterceptor) {
            this.tenantInterceptor = tenantInterceptor;
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(tenantInterceptor);
        }
    }

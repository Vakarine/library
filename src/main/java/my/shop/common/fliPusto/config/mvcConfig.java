package my.shop.common.fliPusto.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;


@Configuration
@Import({dbConfig.class})
@EnableWebMvc
@EnableSpringDataWebSupport
@ComponentScan("my.shop.common.fliPusto")
@PropertySource("classpath:properties/resources.properties")
public class mvcConfig implements WebMvcConfigurer {

    @Value("${uploadBook.path}")
    private String bookPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.setOrder(0);
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:/" + bookPath + "/images/");
        registry.addResourceHandler("/scripts/**")
                .addResourceLocations("file:/" + bookPath + "/scripts/");
        registry.addResourceHandler("/styles/**")
                .addResourceLocations("file:/" + bookPath + "/styles/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
    }

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setTemplateLoaderPath("classpath:freemarkerTemplates/");
        configurer.setDefaultEncoding("UTF-8");
        return configurer;
    }

    @Bean
    public FreeMarkerViewResolver freeMarkerViewResolver() {
        FreeMarkerViewResolver freeMarkerViewResolver = new FreeMarkerViewResolver();
        freeMarkerViewResolver.setCache(false);
        freeMarkerViewResolver.setPrefix("");
        freeMarkerViewResolver.setSuffix(".ftl");
        freeMarkerViewResolver.setContentType("text/html; charset=UTF-8");

        return freeMarkerViewResolver;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}

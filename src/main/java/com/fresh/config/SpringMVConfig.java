package com.fresh.config;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fresh.bean.CustomInterceptor;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

//@Configuration
public class SpringMVConfig implements WebMvcConfigurer {

    /**
     * 类型转换 三种方式之一
     *  1 通过Convert 接口实现
     *  2 通过注解  @DateTimeFormat(pattern = “yyyy-MM-dd”)
     *  3 
     * @param registry
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
//        registry.addConverter(new CustomConverter());// 消息转换器
        WebMvcConfigurer.super.addFormatters(registry);
    }

    /**
     * 拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new BasePathInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new CustomInterceptor()).addPathPatterns("/**");
        WebMvcConfigurer.super.addInterceptors(registry);
    }

    /**
     * 消息转换器
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.TEXT_HTML);
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteDateUseDateFormat);
        SerializeConfig serializeConfig = SerializeConfig.globalInstance;
        serializeConfig.put(BigInteger.class, ToStringSerializer.instance);
        serializeConfig.put(Long.class, ToStringSerializer.instance);
        serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
        fastJsonConfig.setSerializeConfig(serializeConfig);
        fastConverter.setFastJsonConfig(fastJsonConfig);
//        converters.add(fastConverter);
        WebMvcConfigurer.super.configureMessageConverters(converters);
    }

    /**
     * 异步支持
     * @param configurer
     */
    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
        configurer.setDefaultTimeout(1000l);
//        configurer.setTaskExecutor(new AsyncTaskExecutor()) // 线程池
        WebMvcConfigurer.super.configureAsyncSupport(configurer);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        WebMvcConfigurer.super.configureDefaultServletHandling(configurer);
    }

    /**
     * 可添加 自定义controller控制器 配置路由跳转（无需写控制器）
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/toLogin").setViewName("login");
        WebMvcConfigurer.super.addViewControllers(registry);
    }

    /**
     * 视图解析器 添加视图用
     * @param registry
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        WebMvcConfigurer.super.configureViewResolvers(registry);
    }

    /**
     * 静态资源处理
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/");
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    /**
     * controller 层返回数据处理器
     * @param handlers
     */
    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        WebMvcConfigurer.super.addReturnValueHandlers(handlers);
    }

    /**
     * controller 层上方法参数 处理器
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }

    /**
     * 程序异常解析器
     * @param resolvers
     */
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        WebMvcConfigurer.super.configureHandlerExceptionResolvers(resolvers);
    }

    /**
     * 跨域处理
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/index/postFile");
        WebMvcConfigurer.super.addCorsMappings(registry);
        registry.addMapping("/index/postFile")
                .allowCredentials(true)   // 允许带cookie访问
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedOrigins("**")
                .maxAge(3600);

    }
}

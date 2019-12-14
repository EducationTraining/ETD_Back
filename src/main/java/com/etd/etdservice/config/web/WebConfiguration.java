package com.etd.etdservice.config.web;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer{
	@Bean
	public ConfigurableServletWebServerFactory webServerFactory() {
	    TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
	    factory.addConnectorCustomizers(new TomcatConnectorCustomizer() {
	        @Override
	        public void customize(Connector connector) {
	            connector.setProperty("relaxedQueryChars", "|{}[]");
	        }
	    });
	    return factory;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// 图片存放路径
		registry.addResourceHandler("/images/avatars/**").addResourceLocations("file:/home/webapp/EducationTraining/images/avatars/");
		registry.addResourceHandler("/images/others/**").addResourceLocations("file:/home/webapp/EducationTraining/images/others/");
		WebMvcConfigurer.super.addResourceHandlers(registry);
	}

}

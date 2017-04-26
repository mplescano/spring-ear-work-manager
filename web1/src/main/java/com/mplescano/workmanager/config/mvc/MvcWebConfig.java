package com.mplescano.workmanager.config.mvc;

import java.nio.charset.Charset;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "com.mplescano.workmanager.controllers" })
public class MvcWebConfig extends WebMvcConfigurerAdapter {
	
	/*@Autowired
	public ObjectMapper jacksonObjectMapper;*/
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		//converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
		//converters.add(new FormHttpMessageConverter());
		//converters.add(new MappingJackson2HttpMessageConverter(jacksonObjectMapper));
	}
}
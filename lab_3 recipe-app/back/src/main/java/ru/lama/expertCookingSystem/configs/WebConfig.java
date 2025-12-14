package ru.lama.expertCookingSystem.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Value("${file.upload-dir:uploads}")
  private String uploadDir;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/uploads/images/**")
        .addResourceLocations("file:" + uploadDir);

    System.out.println("Resource handler mapped to: file:" + uploadDir + "/images/");
  }
}
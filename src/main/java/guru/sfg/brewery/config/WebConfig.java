package guru.sfg.brewery.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {

//    This is the palce to generically handle the CrossOrigin
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedMethods("GET", "POST", "PUT") // we have not covered delete since we can handle it from controller level by using @CrossOrigin
//                .allowedOrigins("*") // from where the request is coming, this is default
//        ;
//    }
}

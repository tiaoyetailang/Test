package com.zuul.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class MyCorsConfiguration {

    @Bean
    public CorsFilter corsFilter(){
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.addAllowedOrigin("http://manage.leyou.com");
//        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.addAllowedMethod("*");
//        corsConfiguration.addAllowedHeader("*");
//        UrlBasedCorsConfigurationSource con = new UrlBasedCorsConfigurationSource();
//        con.registerCorsConfiguration("/**",corsConfiguration);
//        return new CorsFilter(con);

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://manage.leyou.com");
        configuration.addAllowedOrigin("http://www.leyou.com");
        configuration.setAllowCredentials(true);
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource con = new UrlBasedCorsConfigurationSource();
        con.registerCorsConfiguration("/**",configuration);
        return  new CorsFilter(con);


    }

}

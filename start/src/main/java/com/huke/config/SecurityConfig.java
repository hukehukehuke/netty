package com.huke.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.val;
import org.assertj.core.util.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.data.util.Pair;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import java.util.*;

/**
 * @author huke
 * @date 2022/08/25/下午6:12
 */
@EnableWebSecurity(debug = true)
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final ObjectMapper objectMapper;
    private final SecurityProblemSupport securityProblemSupport;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .formLogin(Customizer.withDefaults())
//                .authorizeRequests(req -> req.antMatchers("/api/greeting")
//                        .authenticated());
//        http.authorizeRequests()
//                .anyRequest().authenticated() //任何请求都会进行认证
//                .and()
//                .formLogin() //启用内建的登陆界面
//                .and()
//                .httpBasic(); //使用Http Basic Auth认证
        http.authorizeRequests(req -> req.antMatchers("/api/**").authenticated())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .csrf(Customizer.withDefaults())
                .logout(logout -> logout.invalidateHttpSession(true))
                .rememberMe(rememberMe ->
                        rememberMe.tokenValiditySeconds(30 * 24 * 3600).rememberMeCookieName("cookieName"));


    }

    @Override
    public void init(WebSecurity web) throws Exception {
        web.ignoring().mvcMatchers("/public/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user")
                .password(passwordEncoder().encode("12345678"))
                .roles("USER", "ADMIN");
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        val idForDefault = "bcrypt";
        val encoders = Map.of(idForDefault, new BCryptPasswordEncoder(),
                "SHA-1", new MessageDigestPasswordEncoder(""));
        return new DelegatingPasswordEncoder(idForDefault, encoders);
    }

    private final Environment environment;

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        if (environment.acceptsProfiles(Profiles.of("dev"))) {
            corsConfiguration.addAllowedHeader("http");
        } else {
            corsConfiguration.addAllowedHeader("");
        }
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELET"));
        corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
        corsConfiguration.addExposedHeader("X-Authenticate");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

    public static void main(String[] args) {
        //张三打印 456，李四大打印
        List<List<Integer>> list = new ArrayList<>(Lists.newArrayList(
                Lists.newArrayList(1,2,3),
                Lists.newArrayList(4,5,6)
        ));
        list.stream()
                .map(it -> {
                     return Pair.of("张三",it);
                })
                .forEach(pair ->{
                    System.out.println(pair.getFirst()+"："+pair.getSecond());
                });
    }
}

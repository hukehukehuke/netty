package com.huke.jwt;

import com.huke.config.AppProperties;
import com.huke.util.CollectionUtil;
import com.huke.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author huke
 * @date 2022/08/29/下午2:30
 */
@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final AppProperties appProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (checkJwtToken(request)) {
            //为空
            validateToken(request)
                    .filter(claims -> claims.get("authorities") != null)
                    .ifPresentOrElse(claims -> { //有值
                        val rawlList = CollectionUtil.convertObjectToList(claims.get("authorities"));
                        val authorites = rawlList.stream()
                                .map(String::valueOf)
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList());
                        val authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorites);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }, SecurityContextHolder::clearContext);
        }
        filterChain.doFilter(request, response);
    }

    private Optional<Claims> validateToken(HttpServletRequest request) {
        String jwtToken = request.getHeader(appProperties.getJwt().getHeader())
                .replace(appProperties.getJwt().getPrefix(), "");

        try {
            //  return (Optional<Claims>) Optional.of(Jwts.parserBuilder().setSigningKey(JwtUtil.key).build().parse(jwtToken).getBody());
            return null;
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * 检查JWT Token时是否存在HTTP中
     *
     * @param request http
     * @return 是否有JWT Token
     */
    private boolean checkJwtToken(HttpServletRequest request) {
        String authenticationHeader = request.getHeader(appProperties.getJwt().getHeader());
        return authenticationHeader != null && authenticationHeader.startsWith(appProperties.getJwt().getPrefix());
    }


}

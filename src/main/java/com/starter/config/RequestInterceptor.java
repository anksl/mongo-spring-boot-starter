package com.starter.config;

import com.starter.model.Request;
import com.starter.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class RequestInterceptor implements HandlerInterceptor {

    private final RequestRepository requestRepository;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        CachedBodyHttpServletRequest cachedBodyHttpServletRequest = new CachedBodyHttpServletRequest(request);
        Request newRequest = new Request();
        newRequest.setUrl(String.valueOf(cachedBodyHttpServletRequest.getRequestURL()));
        newRequest.setUser(cachedBodyHttpServletRequest.getRemoteUser());
        newRequest.setBody(cachedBodyHttpServletRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator())));
        newRequest.setContentType(cachedBodyHttpServletRequest.getContentType());
        requestRepository.save(newRequest);
    }
}

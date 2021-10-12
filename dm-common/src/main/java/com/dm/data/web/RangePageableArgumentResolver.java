package com.dm.data.web;

import com.dm.data.domain.RangePageable;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public interface RangePageableArgumentResolver extends HandlerMethodArgumentResolver {

    @Override
    RangePageable<?> resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
                                     NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory);
}

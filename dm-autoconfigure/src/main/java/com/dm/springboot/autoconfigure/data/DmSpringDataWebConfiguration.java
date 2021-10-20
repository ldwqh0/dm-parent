package com.dm.springboot.autoconfigure.data;

import com.dm.data.domain.RangePageable;
import com.dm.data.web.RangePageableArgumentResolver;
import com.dm.data.web.RangePageableHandlerMethodArgumentResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@ConditionalOnClass(RangePageable.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class DmSpringDataWebConfiguration implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(rangePageableArgumentResolver());
    }

    public RangePageableArgumentResolver rangePageableArgumentResolver() {
        return new RangePageableHandlerMethodArgumentResolver();
    }
}

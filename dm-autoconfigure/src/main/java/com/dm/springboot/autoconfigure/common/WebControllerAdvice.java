package com.dm.springboot.autoconfigure.common;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@AutoConfigureBefore({ ErrorMvcAutoConfiguration.class, ErrorWebFluxAutoConfiguration.class })
@Import({ DmErrorMvcAutoConfiguration.class, DmErrorWebFluxAutoConfiguration.class })
public class WebControllerAdvice {

}

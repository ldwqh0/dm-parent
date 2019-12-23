package com.dm.springboot.autoconfigure.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(DmErrorMvcAutoConfiguration.class)
public class WebControllerAdvice {

}

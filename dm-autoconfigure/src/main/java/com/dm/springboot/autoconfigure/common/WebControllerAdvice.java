package com.dm.springboot.autoconfigure.common;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.dm.common.exception.DmRuntimeException;

@Configuration
@ControllerAdvice
public class WebControllerAdvice {

	@ExceptionHandler(DmRuntimeException.class)
	@ResponseBody
	public Map<String, Object> dmRuntimeExceptionHandler(DmRuntimeException e, HttpServletRequest request,
			HttpServletResponse response) {
		ResponseStatus status = AnnotatedElementUtils.findMergedAnnotation(e.getClass(), ResponseStatus.class);
		if (!Objects.isNull(status)) {
			int code = status.code().value();
			String reason = status.reason();
			response.setStatus(code);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("path", request.getRequestURI());
			result.put("error", HttpStatus.valueOf(code).getReasonPhrase());
			result.put("reason", reason);
			result.put("message", e.getMessage());
			result.put("status", code);
			result.put("timestamp", ZonedDateTime.now());
			return result;
		} else {
			return null;
		}
	}
}

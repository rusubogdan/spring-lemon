package com.naturalprogrammer.spring.lemon.exceptions;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class LemonErrorController extends BasicErrorController {
	
    private static final Log log = LogFactory.getLog(LemonErrorController.class);

    public LemonErrorController(ErrorAttributes errorAttributes,
			ServerProperties serverProperties,
			List<ErrorViewResolver> errorViewResolvers) {
		
		super(errorAttributes, serverProperties.getError(), errorViewResolvers);
		log.info("Created");
	}

	@Override	
	public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
		
		Map<String, Object> body = getErrorAttributes(request,
			isIncludeStackTrace(request, MediaType.ALL));
		
		// if a status was put in LemonErrorAttributes, fetch that
		HttpStatus status =	(HttpStatus) body.get(LemonErrorAttributes.HTTP_STATUS_KEY);

		if (status == null)                // if not put,
			status = getStatus(request);   // let the superclass make the status
		else
			body.remove(LemonErrorAttributes.HTTP_STATUS_KEY); // clean the status from the map

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

		return new ResponseEntity<Map<String, Object>>(body, headers, status);
	}
}

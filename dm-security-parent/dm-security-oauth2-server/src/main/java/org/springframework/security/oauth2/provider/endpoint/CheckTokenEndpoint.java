/*
 * Copyright 2009-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.security.oauth2.provider.endpoint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Controller which decodes access tokens for clients who are not able to do so
 * (or where opaque token values are used).
 *
 * <p>
 * See the <a href=
 * "https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide">OAuth
 * 2.0 Migration Guide</a> for Spring Security 5.
 *
 * @author Luke Taylor
 * @author Joel D'sa
 */
@FrameworkEndpoint
public class CheckTokenEndpoint {

    private ResourceServerTokenServices resourceServerTokenServices;

    private AccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();

    protected final Log logger = LogFactory.getLog(getClass());

    private WebResponseExceptionTranslator<OAuth2Exception> exceptionTranslator = new DefaultWebResponseExceptionTranslator();

    public CheckTokenEndpoint(ResourceServerTokenServices resourceServerTokenServices) {
        this.resourceServerTokenServices = resourceServerTokenServices;
    }

    /**
     * @param exceptionTranslator the exception translator to set
     */
    public void setExceptionTranslator(WebResponseExceptionTranslator<OAuth2Exception> exceptionTranslator) {
        this.exceptionTranslator = exceptionTranslator;
    }

    /**
     * @param accessTokenConverter the accessTokenConverter to set
     */
    public void setAccessTokenConverter(AccessTokenConverter accessTokenConverter) {
        this.accessTokenConverter = accessTokenConverter;
    }

    @RequestMapping(value = "/oauth/check_token", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, ?> checkToken(@RequestParam("token") String value) {

        OAuth2AccessToken token = resourceServerTokenServices.readAccessToken(value);
        if (token == null) {
            throw new InvalidTokenException("Token was not recognised");
        }

        if (token.isExpired()) {
            throw new InvalidTokenException("Token has expired");
        }

        OAuth2Authentication authentication = resourceServerTokenServices.loadAuthentication(token.getValue());

        Map<String, Object> response = (Map<String, Object>) accessTokenConverter.convertAccessToken(token,
                authentication);

        // gh-1070
        response.put("active", true); // Always true if token exists and not expired

        return response;
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<OAuth2Exception> handleException(Exception e) throws Exception {
        logger.info("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage());
        // This isn't an oauth resource, so we don't want to send an
        // unauthorized code here. The client has already authenticated
        // successfully with basic auth and should just
        // get back the invalid token error.
        @SuppressWarnings("serial")
        InvalidTokenException e400 = new InvalidTokenException(e.getMessage()) {
            @Override
            public int getHttpErrorCode() {
                return 400;
            }
        };
        return exceptionTranslator.translate(e400);
    }

}

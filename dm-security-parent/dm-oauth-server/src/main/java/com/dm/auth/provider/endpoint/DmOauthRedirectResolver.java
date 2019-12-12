package com.dm.auth.provider.endpoint;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.RedirectMismatchException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.endpoint.RedirectResolver;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 自定义授权跳转逻辑
 * 
 * 验证主机地址，和地址前导
 * 
 * @author ldwqh0@outlook.com
 *
 */
public class DmOauthRedirectResolver implements RedirectResolver {

    private Collection<String> redirectGrantTypes = Arrays.asList("implicit", "authorization_code");

    @Override
    public String resolveRedirect(String requestedRedirect, ClientDetails client) throws OAuth2Exception {
        Set<String> authorizedGrantTypes = client.getAuthorizedGrantTypes();
        if (authorizedGrantTypes.isEmpty()) {
            throw new InvalidGrantException("A client must have at least one authorized grant type.");
        }
        if (!containsRedirectGrantType(authorizedGrantTypes)) {
            throw new InvalidGrantException(
                    "A redirect_uri can only be used by implicit or authorization_code grant types.");
        }

        Set<String> registeredRedirectUris = client.getRegisteredRedirectUri();
        if (registeredRedirectUris == null || registeredRedirectUris.isEmpty()) {
            throw new InvalidRequestException("At least one redirect_uri must be registered with the client.");
        }
        return obtainMatchingRedirect(registeredRedirectUris, requestedRedirect);
    }

    /**
     * @param grantTypes some grant types
     * @return true if the supplied grant types includes one or more of the redirect
     *         types
     */
    private boolean containsRedirectGrantType(Set<String> grantTypes) {
        for (String type : grantTypes) {
            if (redirectGrantTypes.contains(type)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Attempt to match one of the registered URIs to the that of the requested one.
     * 
     * @param redirectUris      the set of the registered URIs to try and find a
     *                          match. This cannot be null or empty.
     * @param requestedRedirect the URI used as part of the request
     * @return redirect uri
     * @throws RedirectMismatchException if no match was found
     */
    private String obtainMatchingRedirect(Set<String> redirectUris, String requestedRedirect) {
        Assert.notEmpty(redirectUris, "Redirect URIs cannot be empty");

        if (redirectUris.size() == 1 && requestedRedirect == null) {
            return redirectUris.iterator().next();
        }

        for (String redirectUri : redirectUris) {
            if (requestedRedirect != null && redirectMatches(requestedRedirect, redirectUri)) {
                // Initialize with the registered redirect-uri
//				UriComponentsBuilder redirectUriBuilder = UriComponentsBuilder.fromUriString(redirectUri);

                UriComponents requestedRedirectUri = UriComponentsBuilder.fromUriString(requestedRedirect).build();

//				if (this.matchSubdomains) {
//					redirectUriBuilder.host(requestedRedirectUri.getHost());
//				}
//				if (!this.matchPorts) {
//					redirectUriBuilder.port(requestedRedirectUri.getPort());
//				}
//				redirectUriBuilder.replaceQuery(requestedRedirectUri.getQuery()); // retain additional params (if any)
//				redirectUriBuilder.fragment(null);
                return requestedRedirectUri.toUriString();
            }
        }

        throw new RedirectMismatchException(
                "Invalid redirect: " + requestedRedirect + " does not match one of the registered values.");
    }

    protected boolean redirectMatches(String requestedRedirect, String redirectUri) {
        UriComponents requestedRedirectUri = UriComponentsBuilder.fromUriString(requestedRedirect).build();
        UriComponents registeredRedirectUri = UriComponentsBuilder.fromUriString(redirectUri).build();
        boolean schemeMatch = isEqualAndNotEmpty(registeredRedirectUri.getScheme(), requestedRedirectUri.getScheme());
        boolean hostMatch = isEqualAndNotEmpty(registeredRedirectUri.getHost(), requestedRedirectUri.getHost());
        boolean portMatch = registeredRedirectUri.getPort() == requestedRedirectUri.getPort();
        boolean userInfoMatch = StringUtils.equals(registeredRedirectUri.getUserInfo(),
                requestedRedirectUri.getUserInfo());
        return schemeMatch && hostMatch && portMatch && userInfoMatch
                && StringUtils.startsWith(requestedRedirect, redirectUri);
    }

    private boolean isEqualAndNotEmpty(String str1, String str2) {
        return StringUtils.isNotEmpty(str1) && StringUtils.equals(str1, str2);
    }

}

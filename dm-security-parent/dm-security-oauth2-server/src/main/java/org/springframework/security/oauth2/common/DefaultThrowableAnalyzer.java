package org.springframework.security.oauth2.common;

import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.security.web.util.ThrowableCauseExtractor;

import javax.servlet.ServletException;

/**
 * Default implementation of <code>ThrowableAnalyzer</code> which is capable of
 * also unwrapping <code>ServletException</code>s.
 *
 * <p>
 * See the <a href=
 * "https://github.com/spring-projects/spring-security/wiki/OAuth-2.0-Migration-Guide">OAuth
 * 2.0 Migration Guide</a> for Spring Security 5.
 */
public final class DefaultThrowableAnalyzer extends ThrowableAnalyzer {
    /**
     * @see org.springframework.security.web.util.ThrowableAnalyzer#initExtractorMap()
     */
    @Override
    protected void initExtractorMap() {
        super.initExtractorMap();

        registerExtractor(ServletException.class, new ThrowableCauseExtractor() {
            @Override
            public Throwable extractCause(Throwable throwable) {
                ThrowableAnalyzer.verifyThrowableHierarchy(throwable, ServletException.class);
                return ((ServletException) throwable).getRootCause();
            }
        });
    }
}

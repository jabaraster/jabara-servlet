/**
 * 
 */
package jabara.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

/**
 * @author jabaraster
 */
public class RequestDumpFilter implements Filter {

    /**
     * このクラスの中で使っている{@link Logger}の名前. <br>
     * このクラスが出力するログは全て、このLoggerの{@link Logger#trace(Object)}で出力します. <br>
     */
    public static final String  LOGGER_NAME = "jabara.servlet.RequestDumpFilter"; //$NON-NLS-1$

    private static final Logger _logger     = Logger.getLogger(LOGGER_NAME);

    /**
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        //
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(final ServletRequest pRequest, final ServletResponse pResponse, final FilterChain pChain) throws IOException,
            ServletException {
        final HttpServletRequest request = (HttpServletRequest) pRequest;

        if (!_logger.isTraceEnabled()) {
            pChain.doFilter(pRequest, pResponse);
            return;
        }

        _logger.trace("-------------- " + request.getRequestURI() + "(Request)(" + request.getMethod() + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        dumpQueryString(request);
        dumpCookie(request);
        dumpRequestHeaders(request);
        dumpRequestParameter(request);

        if (ServletUtil.isMultipartRequest(request)) {
            // s(request);
        }

        pChain.doFilter(pRequest, pResponse);
    }

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(@SuppressWarnings("unused") final FilterConfig pFilterConfig) {
        //
    }

    private static void dumpCookie(final HttpServletRequest pRequest) {
        _logger.trace("  -- Cookies"); //$NON-NLS-1$
        final Cookie[] cookies = pRequest.getCookies();
        if (cookies != null) {
            for (final Cookie cookie : cookies) {
                _logger.trace("    " + cookie.getName() + ": " + cookie.getValue()); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
    }

    private static void dumpQueryString(final HttpServletRequest pRequest) {
        _logger.trace("  -- QueryString"); //$NON-NLS-1$
        _logger.trace("    " + pRequest.getQueryString()); //$NON-NLS-1$
    }

    private static void dumpRequestHeaders(final HttpServletRequest pRequest) {
        _logger.trace("  -- Request headers"); //$NON-NLS-1$
        for (final Enumeration<String> headerNames = pRequest.getHeaderNames(); headerNames.hasMoreElements();) {
            final String headerName = headerNames.nextElement();
            _logger.trace("    " + headerName + ": " + toList(pRequest.getHeaders(headerName))); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    private static void dumpRequestParameter(final HttpServletRequest pRequest) {
        _logger.trace("  -- Request Parameters"); //$NON-NLS-1$

        if (ServletUtil.isMultipartRequest(pRequest)) {
            _logger.trace("    Multipart Request!"); //$NON-NLS-1$
        } else {
            for (final Map.Entry<String, String[]> parameter : pRequest.getParameterMap().entrySet()) {
                _logger.trace("    " + parameter.getKey() + ": " + Arrays.asList(parameter.getValue())); //$NON-NLS-1$//$NON-NLS-2$
            }
        }
    }

    private static List<Object> toList(final Enumeration<?> pEnumeration) {
        final List<Object> list = new ArrayList<Object>();
        while (pEnumeration.hasMoreElements()) {
            list.add(pEnumeration.nextElement());
        }
        return list;
    }
}

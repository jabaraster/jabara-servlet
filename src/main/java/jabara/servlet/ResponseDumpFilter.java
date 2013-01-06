/**
 * 
 */
package jabara.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

/**
 * @author jabaraster
 */
public class ResponseDumpFilter implements Filter {

    /**
     * このクラスの中で使っている{@link Logger}の名前. <br>
     * このクラスが出力するログは全て、このLoggerの{@link Logger#trace(Object)}で出力します. <br>
     */
    public static final String  LOGGER_NAME = "jabara.servlet.ResponseDumpFilter"; //$NON-NLS-1$

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

        pChain.doFilter(pRequest, pResponse);

        if (!_logger.isTraceEnabled()) {
            return;
        }

        final HttpServletRequest request = (HttpServletRequest) pRequest;
        _logger.trace("-------------- " + request.getRequestURI() + "(Response)"); //$NON-NLS-1$ //$NON-NLS-2$
        final HttpServletResponse response = (HttpServletResponse) pResponse;
        dumpStatus(response);
        dumpResponseHeaders(response);
    }

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(@SuppressWarnings("unused") final FilterConfig pFilterConfig) {
        //
    }

    private static void dumpResponseHeaders(final HttpServletResponse pResponse) {
        _logger.trace("  -- Response headers"); //$NON-NLS-1$
        for (final String headerName : pResponse.getHeaderNames()) {
            _logger.trace("    " + headerName + ": " + new ArrayList<String>(pResponse.getHeaders(headerName))); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    private static void dumpStatus(final HttpServletResponse pResponse) {
        _logger.trace("  -- Response status"); //$NON-NLS-1$
        _logger.trace("    " + pResponse.getStatus()); //$NON-NLS-1$ 
    }
}

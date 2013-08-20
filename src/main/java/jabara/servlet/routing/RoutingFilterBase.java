/**
 * 
 */
package jabara.servlet.routing;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jabaraster
 */
public abstract class RoutingFilterBase implements Filter {

    /**
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        // 処理なし
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(final ServletRequest pRequest, final ServletResponse pResponse, final FilterChain pChain) throws IOException,
            ServletException {
        try {
            final HttpServletRequest request = (HttpServletRequest) pRequest;
            final HttpServletResponse response = (HttpServletResponse) pResponse;

            createRouter(request, response).routing();
            pChain.doFilter(pRequest, pResponse);

        } catch (final RequestInterrupted e) {
            return;
        } catch (final IOException e) {
            throw e;
        } catch (final ServletException e) {
            throw e;
        } catch (final Exception e) {
            throw rethrow(e);
        }
    }

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(@SuppressWarnings("unused") final FilterConfig pFilterConfig) {
        // 処理なし
    }

    /**
     * @param pRequest -
     * @param pResponse -
     * @return -
     */
    protected abstract IRouter createRouter(final HttpServletRequest pRequest, final HttpServletResponse pResponse);

    private static RuntimeException rethrow(final Throwable pCause) {
        if (pCause instanceof RuntimeException) {
            throw (RuntimeException) pCause;
        }
        if (pCause instanceof Error) {
            throw (Error) pCause;
        }
        if (InvocationTargetException.class.equals(pCause)) {
            return rethrow(((InvocationTargetException) pCause).getTargetException());
        }
        if (ExecutionException.class.equals(pCause)) {
            return rethrow(((ExecutionException) pCause).getCause());
        }
        throw new IllegalStateException(pCause);
    }
}

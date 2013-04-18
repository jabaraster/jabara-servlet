/**
 * 
 */
package jabara.servlet;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 * @author jabaraster
 */
@RunWith(Enclosed.class)
@SuppressWarnings({ "static-method", "nls" })
public class ServletUtilTest {

    @SuppressWarnings("javadoc")
    public static class omitContextPathFromRequestUri {

        /**
         * 
         */
        @Test
        public void コンテキストパスが空でないときはそれが除外されたパスを返す() {
            final HttpServletRequest req = RequestHandler.wrap("/a", "/a/root");
            final String actual = ServletUtil.omitContextPathFromRequestUri(req);
            assertThat(actual, is("/root"));
        }

        @Test
        public void コンテキストパスが空のときはRequestURIそのものを返す() {
            final HttpServletRequest req = RequestHandler.wrap("", "/a/root");
            final String actual = ServletUtil.omitContextPathFromRequestUri(req);
            assertThat(actual, is("/a/root"));
        }
    }

    private static class RequestHandler implements InvocationHandler {

        private final String contextPath;
        private final String requestUri;

        RequestHandler(final String pContextPath, final String pRequestUri) {
            this.contextPath = pContextPath;
            this.requestUri = pRequestUri;
        }

        @Override
        public Object invoke(final Object pProxy, final Method pMethod, final Object[] pArgs) throws Throwable {
            if (pMethod.getName().equals("getContextPath")) {
                return this.contextPath;
            }
            if (pMethod.getName().equals("getRequestURI")) {
                return this.requestUri;
            }
            return pMethod.invoke(pProxy, pArgs);
        }

        static HttpServletRequest wrap(final String pContextPath, final String pRequestUri) {
            return (HttpServletRequest) Proxy.newProxyInstance(RequestHandler.class.getClassLoader(), new Class<?>[] { HttpServletRequest.class },
                    new RequestHandler(pContextPath, pRequestUri));
        }
    }
}

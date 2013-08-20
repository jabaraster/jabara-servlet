package jabara.servlet.routing;

import jabara.servlet.ServletUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jabaraster
 */
public abstract class RouterBase implements IRouter {
    /**
     * 
     */
    protected final HttpServletRequest  request;

    /**
     * 
     */
    protected final HttpServletResponse response;

    /**
     * @param pRequest -
     * @param pResponse -
     */
    public RouterBase(final HttpServletRequest pRequest, final HttpServletResponse pResponse) {
        checkNull(pRequest, "pRequest"); //$NON-NLS-1$
        checkNull(pResponse, "pResponse"); //$NON-NLS-1$
        this.request = pRequest;
        this.response = pResponse;
    }

    /**
     * @throws Exception -
     */
    @Override
    public void routing() throws Exception {
        redirectIfUrlContainsSessionId(getTopPagePath());
        routingCore();
    }

    /**
     * @param pPathWithoutContextPath -
     * @return -
     */
    protected boolean equalsPath(final String pPathWithoutContextPath) {
        return ServletUtil.omitContextPathFromRequestUri(this.request).equals(pPathWithoutContextPath);
    }

    /**
     * @param pRequestPath -
     * @param pRedirectPath -
     * @throws RequestInterrupted -
     * @throws IOException -
     * @throws ServletException -
     */
    protected void forwardIfMatch(final String pRequestPath, final String pRedirectPath) throws RequestInterrupted, IOException, ServletException {
        if (equalsPath(pRequestPath)) {
            forward(pRedirectPath, this.request, this.response);
        }
    }

    /**
     * {@link #redirectIfUrlContainsSessionId(String)}にてリダイレクトする先のURL.
     * 
     * @return -
     */
    protected abstract String getTopPagePath();

    /**
     * @param pRequestPath -
     * @param pRedirectPath -
     * @throws RequestInterrupted -
     * @throws IOException -
     */
    protected void redirectIfMatch(final String pRequestPath, final String pRedirectPath) throws RequestInterrupted, IOException {
        if (equalsPath(pRequestPath)) {
            redirect(pRedirectPath, this.request, this.response);
        }
    }

    /**
     * セッションIDを含むURLをクライアントのアドレス欄に晒さないようにするため、リダイレクトする.
     * 
     * @param pRedirectPath -
     * @throws IOException -
     * @throws RequestInterrupted -
     */
    protected void redirectIfUrlContainsSessionId(final String pRedirectPath) throws IOException, RequestInterrupted {
        final String sessionId = this.request.getRequestedSessionId();
        if (sessionId == null) {
            return;
        }

        if (this.request.getRequestURI().contains(sessionId)) {
            redirect(pRedirectPath, this.request, this.response);
        }
    }

    /**
     * @throws Exception -
     */
    protected abstract void routingCore() throws Exception;

    private static void checkNull(final Object pArgumentValue, final String pArgumentName) {
        if (pArgumentValue == null) {
            throw new IllegalArgumentException("引数'" + pArgumentName + "'にnullを渡すことはできません。"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    private static void forward( //
            final String pPath //
            , final HttpServletRequest pRequest //
            , final HttpServletResponse pResponse //
    ) throws IOException, RequestInterrupted, ServletException {
        pRequest.getRequestDispatcher(pPath).forward(pRequest, pResponse);
        throw RequestInterrupted.GLOBAL;
    }

    private static void redirect( //
            final String pPath //
            , final HttpServletRequest pRequest //
            , final HttpServletResponse pResponse //
    ) throws IOException, RequestInterrupted {
        pResponse.sendRedirect(pRequest.getContextPath() + pPath);
        throw RequestInterrupted.GLOBAL;
    }
}
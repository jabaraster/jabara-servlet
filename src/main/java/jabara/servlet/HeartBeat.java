/**
 * 
 */
package jabara.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jabaraster
 * 
 */
public class HeartBeat extends HttpServlet {
    private static final long serialVersionUID = -2346359934876872559L;

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(@SuppressWarnings("unused") final HttpServletRequest pReq, final HttpServletResponse pResp) throws IOException {
        pResp.setContentType("text/plain"); //$NON-NLS-1$
        pResp.getWriter().append("OK"); //$NON-NLS-1$
    }

}

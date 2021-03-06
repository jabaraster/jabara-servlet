/**
 * 
 */
package jabara.servlet;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jabaraster
 */
public final class ServletUtil {

    private ServletUtil() {
        // 処理なし
    }

    /**
     * @param pRequest -
     * @return -
     */
    public static boolean isMultipartRequest(final HttpServletRequest pRequest) {
        if (pRequest == null) {
            throw new IllegalArgumentException();
        }

        final String contentType = pRequest.getContentType();
        if (contentType == null) {
            return false;
        }
        return contentType.startsWith("multipart/form-data"); //$NON-NLS-1$
    }

    /**
     * {@link HttpServletRequest#getRequestURI()}が返すパスからコンテキストパス部分を除きます. <br>
     * 
     * @param pRequest リクエスト.
     * @return 先頭には / が付いています.
     */
    public static String omitContextPathFromRequestUri(final HttpServletRequest pRequest) {
        if (pRequest == null) {
            throw new IllegalArgumentException();
        }

        final String contextPath = pRequest.getContextPath();
        if (contextPath == null || contextPath.length() == 0) {
            return pRequest.getRequestURI();
        }

        return pRequest.getRequestURI().substring(contextPath.length());
    }
}

package jabara.servlet.routing;

/**
 * @author jabaraster
 */
public final class RequestInterrupted extends Exception {
    private static final long serialVersionUID = 7136696838961800917L;

    /**
     * 
     */
    public static final RequestInterrupted  GLOBAL         = new RequestInterrupted();

    private RequestInterrupted() {
        // 処理なし
    }
}
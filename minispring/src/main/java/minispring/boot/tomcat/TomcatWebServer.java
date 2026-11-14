package minispring.boot.tomcat;

import jakarta.servlet.Servlet;
import java.io.IOException;
import java.nio.file.Files;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

/**
 * Tomcat に Servlet を載せて動かすサーバ。
 */
public class TomcatWebServer {
    private static final String SERVLET_NAME = "dispatcherServlet";

    private final Tomcat tomcat;

    /**
     * Tomcat を作り、Servlet を {@code /} に載せる。
     *
     * @param port 待ち受けるポート番号
     * @param servlet すべてのリクエストを受ける Servlet
     * @throws IOException Tomcat の作業用のディレクトリを作れなかったとき
     */
    public TomcatWebServer(int port, Servlet servlet) throws IOException {
        this.tomcat = new Tomcat();
        this.tomcat.setBaseDir(Files.createTempDirectory("tomcat.").toString());
        this.tomcat.setPort(port);
        this.tomcat.getConnector();
        Context context = this.tomcat.addContext("", null);
        Tomcat.addServlet(context, SERVLET_NAME, servlet);
        context.addServletMappingDecoded("/", SERVLET_NAME);
    }

    /**
     * Tomcat を起動し、プログラムが終わらないようにリクエストを待ち続けるスレッドを動かす。
     *
     * @throws LifecycleException Tomcat を起動できなかったとき
     */
    public void start() throws LifecycleException {
        this.tomcat.start();
        startNonDaemonAwaitThread();
        System.out.println("Tomcat started on port " + this.tomcat.getConnector().getLocalPort());
    }

    /**
     * Tomcat のスレッドはすべてデーモンなので、main が終わってもプログラムが終わらないよう、
     * デーモンでないスレッドで Tomcat の終了を待つ。
     */
    private void startNonDaemonAwaitThread() {
        Thread awaitThread = new Thread(() -> this.tomcat.getServer().await(), "container-0");
        awaitThread.setDaemon(false);
        awaitThread.start();
    }
}

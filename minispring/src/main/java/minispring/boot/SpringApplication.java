package minispring.boot;

import minispring.boot.tomcat.TomcatWebServer;
import minispring.context.ApplicationContext;
import minispring.web.servlet.DispatcherServlet;

/**
 * Spring Boot のアプリを起動するクラス。
 */
public class SpringApplication {
    /**
     * 起動するクラスのパッケージをスキャンして Bean をすべて作り、Tomcat を起動する。
     *
     * @param primarySource 起動するクラス
     * @param args 起動するときの引数
     * @return Bean をすべて作り終えた {@link ApplicationContext}
     * @throws Exception Tomcat を起動できなかったとき
     */
    public static ApplicationContext run(Class<?> primarySource, String... args) throws Exception {
        ApplicationContext context = new ApplicationContext(primarySource.getPackageName());
        new TomcatWebServer(8080, new DispatcherServlet(context)).start();
        return context;
    }
}

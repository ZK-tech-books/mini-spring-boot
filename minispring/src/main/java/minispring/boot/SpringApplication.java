package minispring.boot;

import java.io.IOException;
import minispring.boot.web.server.WebServer;
import minispring.context.ApplicationContext;
import minispring.web.servlet.DispatcherServlet;

public class SpringApplication {
    public static ApplicationContext run(Class<?> primarySource, String... args) throws IOException {
        ApplicationContext context = new ApplicationContext(primarySource.getPackageName());
        new WebServer(8080, new DispatcherServlet(context)).start();
        return context;
    }
}

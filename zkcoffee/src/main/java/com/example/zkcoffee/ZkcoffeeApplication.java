package com.example.zkcoffee;

import minispring.boot.web.server.WebServer;
import minispring.context.ApplicationContext;
import minispring.web.servlet.DispatcherServlet;

public class ZkcoffeeApplication {
    public static void main(String[] args) throws Exception {
        ApplicationContext context = new ApplicationContext("com.example.zkcoffee");
        new WebServer(8080, new DispatcherServlet(context)).start();
    }
}

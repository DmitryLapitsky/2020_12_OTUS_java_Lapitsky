package ru.otus.server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.hibernate.crm.service.DbServiceClientImpl;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.UserAuthService;
import ru.otus.servlet.*;


import java.util.Arrays;

public class WebServerWithSecurity implements UsersWebServer {

    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";


    private final UserAuthService authService;
    private final DbServiceClientImpl userDao;
    protected final TemplateProcessor templateProcessor;
    private final Server server;

    public WebServerWithSecurity(int port,
                                 UserAuthService authService,
                                 DbServiceClientImpl userDao,
                                 TemplateProcessor templateProcessor) {
        this.userDao = userDao;
        this.templateProcessor = templateProcessor;
        server = new Server(port);
        this.authService = authService;
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().length == 0) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private void initContext() {
        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        HandlerList handlers = new HandlerList();
        handlers.addHandler(resourceHandler);
        handlers.addHandler(applySecurity(servletContextHandler, "/clients","/api/v1/client"));

        server.setHandler(handlers);
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
        resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
//        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/index");
        servletContextHandler.addServlet(new ServletHolder(new ClientsServlet(templateProcessor, userDao)), "/clients");
        servletContextHandler.addServlet(new ServletHolder(new NewClientServlet(templateProcessor, userDao)), "/api/v1/client");
        return servletContextHandler;
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/index");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths).forEachOrdered(path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }
}

package ru.otus.servlet;


import ru.otus.hibernate.crm.service.DbServiceClientImpl;
import ru.otus.services.TemplateProcessor;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ClientsServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "clients.html";
    private static final String CLIENTS = "allClients";
    private static final String PHONES = "allPhones";
    private static final String ADDRESSES = "allAddresses";

    private final DbServiceClientImpl userDao;
    private final TemplateProcessor templateProcessor;

    public ClientsServlet(TemplateProcessor templateProcessor, DbServiceClientImpl userDao) {
        this.templateProcessor = templateProcessor;
        this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> clientsMap = new HashMap<>();
        clientsMap.put(CLIENTS, userDao.getAll());
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, clientsMap));
    }
}

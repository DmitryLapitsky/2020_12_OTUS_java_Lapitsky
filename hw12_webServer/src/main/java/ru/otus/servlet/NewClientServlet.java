package ru.otus.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.hibernate.crm.model.Address;
import ru.otus.hibernate.crm.model.Client;
import ru.otus.hibernate.crm.model.PhoneDataSet;
import ru.otus.hibernate.crm.service.DbServiceClientImpl;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class NewClientServlet extends HttpServlet {

    private static final String USERS_PAGE_TEMPLATE = "addClient.html";

    private final DbServiceClientImpl userDao;
    private final TemplateProcessor templateProcessor;

    public NewClientServlet(TemplateProcessor templateProcessor, DbServiceClientImpl userDao) {
        this.templateProcessor = templateProcessor;
        this.userDao = userDao;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, Collections.emptyMap()));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Client client = new Client(request.getParameter("name"));
        Address address = new Address(request.getParameter("address"));
        address.setClient(client);
        client.setAddress(address);
        PhoneDataSet phone1 = new PhoneDataSet(request.getParameter("phone"));
        PhoneDataSet phone2 = new PhoneDataSet(request.getParameter("additionalPhone"));
        phone1.setClient(client);
        phone2.setClient(client);
        client.setPhones(List.of(phone1,phone2));
        userDao.saveClient(client);
        response.sendRedirect("/clients");
    }

}

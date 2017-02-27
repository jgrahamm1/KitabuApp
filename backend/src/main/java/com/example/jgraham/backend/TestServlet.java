package com.example.jgraham.backend;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by prashant on 2/27/17.
 */

public class TestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
//        int id = Integer.parseInt(req.getParameter("id"));
//        MessagingEndpoint messagingEndpoint = new MessagingEndpoint();
//        messagingEndpoint.sendMessage(String.valueOf(id));
//        resp.sendRedirect("/query");
    }
}

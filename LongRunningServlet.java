package com.example.threaddemo;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class LongRunningServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {

            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String message = "Processed by: " + Thread.currentThread().getName();
        System.out.println("Responding with: " + message);
        response.setContentType("text/plain");
        response.getWriter().write("Processed by: " + Thread.currentThread().getName());
        response.getWriter().flush();

    }
}

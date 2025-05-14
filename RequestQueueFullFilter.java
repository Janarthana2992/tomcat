package com.example.threaddemo;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.concurrent.Semaphore;

public class RequestQueueFullFilter implements Filter {

    // Adjust this value based on maxThreads + acceptCount
    private static final int MAX_ACTIVE = 103;
    private static final Semaphore semaphore = new Semaphore(MAX_ACTIVE);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        boolean acquired = semaphore.tryAcquire();

        if (!acquired) {
            // Immediately respond with 503 if no permit is available
            httpResponse.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            httpResponse.setContentType("text/plain");
            httpResponse.getWriter().write("503 Service Unavailable: Server is too busy.");
            httpResponse.getWriter().flush();
            return;
        }

        try {
            // Proceed with the request if permit is acquired
            chain.doFilter(request, response);
        } finally {
            // Always release the permit after request processing
            semaphore.release();
        }
    }

    @Override
    public void destroy() {
        // Cleanup if needed
    }
}

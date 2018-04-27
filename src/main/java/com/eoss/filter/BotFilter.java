package com.eoss.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequestWrapper;

public class BotFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
	/*	if (!req.isSecure()) {
			String uri = "https://" +   // "http" + "://
		             req.getServerName() +       // "myhost"
		             req.getRequestURI() +       // "/people"
		            (req.getQueryString() != null ? "?" +
		             req.getQueryString() : ""); // "?" + "lastname=Fox&age=30"
			
			resp.sendRedirect(uri);
			return;
		}*/
		
		HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper((HttpServletRequest) request) {

			@Override
			public String getParameter(String name) {
				String value = super.getParameter(name);
				if (value==null) return null;
				return value.replace("<", "&lt;").replace(">", "&gt;");
			}
			
		};
		
		chain.doFilter(requestWrapper, response);
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}

}

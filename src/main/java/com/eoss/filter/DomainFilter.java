package com.eoss.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import com.eoss.appengine.bean.AccessLog;
import com.eoss.appengine.dao.AccessLogDAO;
import com.eoss.appengine.helper.SetReqParam;

public class DomainFilter implements Filter{
	private SetReqParam srq = new SetReqParam();
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		//HttpServletResponse resp = (HttpServletResponse) response;
		
		AccessLogDAO accesslogDao = new AccessLogDAO();
		AccessLog accesslog = new AccessLog();
        // Get client's IP address
        String reqIp = req.getRemoteAddr(); // ip
    
        // Get client's hostname
        String reqHost = req.getRemoteHost(); // hostname
        
        Date timeStamp = srq.getCurrentDate();
        
        // Get client's req path
        String reqPath = null;
        String reqQueryString = null;
        if (request instanceof HttpServletRequest) {
        	reqPath = ((HttpServletRequest)request).getRequestURL().toString();
        	reqQueryString = ((HttpServletRequest)request).getQueryString();
        }
        
        accesslog.setReqHost(reqHost);
        accesslog.setReqIp(reqIp);
        accesslog.setReqPath(reqPath);
        accesslog.setReqQueryString(reqQueryString);
        accesslog.setTimeStamp(timeStamp);
        
        accesslogDao.addAccessLog(accesslog);
        
		/*
		String origin = req.getHeader("");
		
		if (origin==null || !origin.equals(domain)) {
			resp.sendRedirect(domain);
			return;
		}
		*/
		chain.doFilter(request, response);	
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}

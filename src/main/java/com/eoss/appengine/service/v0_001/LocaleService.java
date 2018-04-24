package com.eoss.appengine.service.v0_001;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eoss.appengine.bean.Locale;
import com.eoss.appengine.dao.LocaleDAO;
import com.eoss.appengine.helper.SetRespPram;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class LocaleService extends HttpServlet{
	SetRespPram srp = new SetRespPram();
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Locale locale = new Locale();
		LocaleDAO localeDao = new LocaleDAO();
		String localeName = req.getParameter("locale");
		
		locale.setLocale(localeName);
		locale.setLocaleId(Long.toString(System.nanoTime()));
		
		String status = localeDao.addLocale(locale);
		String json = srp.parseJsonStatus("addlocale", status, "");
		
		resp = srp.setRespHead(resp);
		resp.getWriter().write(json);
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Locale locale = new Locale();
		LocaleDAO localeDao = new LocaleDAO();
		List<Entity> le = localeDao.getLocaleList(locale);
		String json = srp.parseJsonEntityList(le);
		resp = srp.setRespHead(resp);
		resp.getWriter().write(json);
	}
}

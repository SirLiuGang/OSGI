package com.zhbr.translateclient.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.zhbr.translateasslt.service.TranslateService;

public class TranslateServiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private BundleContext context;

	public TranslateServiceServlet(BundleContext bundleContext) {
		this.context = bundleContext;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		// 1、取得用户传来的英文单词
		String queryWord = req.getParameter("query_word");

		// 2、调用方法查找中文含义
		// 获取服务
		TranslateService translateService = null;
		ServiceReference serviceRef = context.getServiceReference(TranslateService.class.getName());
		if (null != serviceRef) {
			translateService = (TranslateService) context.getService(serviceRef);
		}

		// 3、返回结果给用户
		resp.setContentType("text/html;charset=GBK");
		PrintWriter writer = resp.getWriter();
		if (translateService == null) {
			writer.println("没有开放翻译服务！");
			writer.close();
			return;
		}

		String result = translateService.translate(queryWord);
		writer.println("结果" + result);
		writer.close();
		return;

	}

}

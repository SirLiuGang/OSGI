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

		// 1��ȡ���û�������Ӣ�ĵ���
		String queryWord = req.getParameter("query_word");

		// 2�����÷����������ĺ���
		// ��ȡ����
		TranslateService translateService = null;
		ServiceReference serviceRef = context.getServiceReference(TranslateService.class.getName());
		if (null != serviceRef) {
			translateService = (TranslateService) context.getService(serviceRef);
		}

		// 3�����ؽ�����û�
		resp.setContentType("text/html;charset=GBK");
		PrintWriter writer = resp.getWriter();
		if (translateService == null) {
			writer.println("û�п��ŷ������");
			writer.close();
			return;
		}

		String result = translateService.translate(queryWord);
		writer.println("���" + result);
		writer.close();
		return;

	}

}

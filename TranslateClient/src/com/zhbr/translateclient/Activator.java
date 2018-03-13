package com.zhbr.translateclient;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import com.zhbr.translateclient.servlet.TranslateServiceServlet;

public class Activator implements BundleActivator, ServiceListener {

	private static BundleContext context;

	private ServiceReference serviceReference;
	private Servlet servlet;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.
	 * BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;

		// ��TranslateServiceServlet�д���һ�����췽������bundleContext����ȥ
		servlet = new TranslateServiceServlet(bundleContext);
		// ע��Servlet
		registerServlet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		// ע��Servlet����Դ
		unRegisterServlet();
		serviceReference = null;
		Activator.context = null;
	}

	/**
	 * ע��Servlet
	 */
	private void registerServlet() {
		if (serviceReference == null) {
			// ͨ�������Ļ�ȡ�������ġ����á�(��Ҫͨ��MANIFEST.MF����org.osgi.service.http��)
			serviceReference = context.getServiceReference(HttpService.class);
		}

		if (serviceReference != null) {
			// �õ�http�������
			HttpService httpService = (HttpService) context.getService(serviceReference);
			if (httpService != null) {
				try {
					// ע��Servlet
					// �ĸ������ֱ���:(ӳ���ַ,Servlet�౾��,Dictionary,������)
					httpService.registerServlet("/servlet/translateServlet", servlet, null, null);
					// ע�ᾲ̬��Դ(html���ļ�)λ��
					// �ĸ������ֱ���(���ʵ�ַ,��̬�ļ�����·��,������)
					httpService.registerResources("/page", "face", null);//http://localhost:8010/page/translate.html
//					httpService.registerResources("/page", "src", null);//http://localhost:8010/page/pages/translate.html
//					httpService.registerResources("/page", "src/pages", null);//http://localhost:8010/page/translate.html
					System.out.println("�������ַ����������ɹ�����ͨ��/page/translate.html����!");
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (NamespaceException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * ע��Servlet
	 */
	private void unRegisterServlet() {
		if (serviceReference != null) {
			// �õ�http�������
			HttpService httpService = (HttpService) context.getService(serviceReference);
			if (httpService != null) {
				try {
					// ע��Servlet
					// �ĸ������ֱ���:ӳ���ַ
					httpService.unregister("/servlet/translateServlet");
					// ע����̬��Դ(html���ļ�)λ��
					// �����Ƿ��ʵ�ַ
					httpService.unregister("/pages");
					System.out.println("�������ַ�����ͣ�óɹ���ллʹ�ã�");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void serviceChanged(ServiceEvent event) {
		switch (event.getType()) {  
        case ServiceEvent.REGISTERED:  
            registerServlet();
            break;  
        case ServiceEvent.UNREGISTERING:  
            unRegisterServlet();
            break;  
        default:  
            break;  
        }  
	}

}

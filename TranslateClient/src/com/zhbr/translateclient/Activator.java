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

		// 在TranslateServiceServlet中创建一个构造方法，将bundleContext传进去
		servlet = new TranslateServiceServlet(bundleContext);
		// 注册Servlet
		registerServlet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		// 注销Servlet等资源
		unRegisterServlet();
		serviceReference = null;
		Activator.context = null;
	}

	/**
	 * 注册Servlet
	 */
	private void registerServlet() {
		if (serviceReference == null) {
			// 通过上下文获取服务对象的“引用”(需要通过MANIFEST.MF引入org.osgi.service.http包)
			serviceReference = context.getServiceReference(HttpService.class);
		}

		if (serviceReference != null) {
			// 得到http服务对象
			HttpService httpService = (HttpService) context.getService(serviceReference);
			if (httpService != null) {
				try {
					// 注册Servlet
					// 四个参数分别是:(映射地址,Servlet类本身,Dictionary,上下文)
					httpService.registerServlet("/servlet/translateServlet", servlet, null, null);
					// 注册静态资源(html等文件)位置
					// 四个参数分别是(访问地址,静态文件所在路径,上下文)
					httpService.registerResources("/page", "face", null);//http://localhost:8010/page/translate.html
//					httpService.registerResources("/page", "src", null);//http://localhost:8010/page/pages/translate.html
//					httpService.registerResources("/page", "src/pages", null);//http://localhost:8010/page/translate.html
					System.out.println("翻译助手服务已启动成功，请通过/page/translate.html访问!");
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (NamespaceException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 注销Servlet
	 */
	private void unRegisterServlet() {
		if (serviceReference != null) {
			// 得到http服务对象
			HttpService httpService = (HttpService) context.getService(serviceReference);
			if (httpService != null) {
				try {
					// 注销Servlet
					// 四个参数分别是:映射地址
					httpService.unregister("/servlet/translateServlet");
					// 注销静态资源(html等文件)位置
					// 参数是访问地址
					httpService.unregister("/pages");
					System.out.println("翻译助手服务已停用成功，谢谢使用！");
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

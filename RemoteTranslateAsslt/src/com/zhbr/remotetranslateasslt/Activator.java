package com.zhbr.remotetranslateasslt;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import com.zhbr.remotetranslateasslt.impls.TranslateServiceRemoteImpl;
import com.zhbr.translateasslt.service.TranslateService;

public class Activator implements BundleActivator {

	private static BundleContext context;

	private ServiceRegistration<TranslateService> sr;

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
		// ע��Service����
		sr = bundleContext.registerService(TranslateService.class, new TranslateServiceRemoteImpl(), null);
		System.out.println("Զ�̲�ѯ������������");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		ServiceReference serviceRef = context.getServiceReference(TranslateService.class.getName());
		bundleContext.ungetService(serviceRef);
		System.out.println("Զ�̲�ѯ������ֹͣ��");
		Activator.context = null;
	}

}

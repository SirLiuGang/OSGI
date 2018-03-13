package com.zhbr.remotetranslateasslt.impls;

import java.util.concurrent.ConcurrentHashMap;

import com.zhbr.translateasslt.service.TranslateService;

/**
 * 远程翻译助手，实现TranslateService接口
 */
public class TranslateServiceRemoteImpl implements TranslateService{

	private static final ConcurrentHashMap<String, String> dictonary = new ConcurrentHashMap<String, String>();  
	static{  
        dictonary.put("cat", "小猫");  
        dictonary.put("dog", "小狗");  
    } 
	
	@Override
	public String translate(String word) {
		System.out.println("RemoteTranslateAsslt Service---------->");  
        String result = dictonary.get(word);  
        if(null == result){  
            result = "未找到您所查单词意义，请检查单词是否正确!";  
        }  
        return result;  
	}

}

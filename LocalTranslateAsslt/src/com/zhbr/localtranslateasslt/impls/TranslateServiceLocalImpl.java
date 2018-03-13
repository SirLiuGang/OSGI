package com.zhbr.localtranslateasslt.impls;

import java.util.concurrent.ConcurrentHashMap;

import com.zhbr.translateasslt.service.TranslateService;

/**
 * 本地翻译助手，实现TranslateService接口
 */
public class TranslateServiceLocalImpl implements TranslateService{

	private static final ConcurrentHashMap <String, String> dictonary = new ConcurrentHashMap<String, String>();  
    static{  
        dictonary.put("CHINA", "中国");  
        dictonary.put("USA", "美国");  
    } 
	
	@Override
	public String translate(String word) {
		System.out.println("LocalTranslateAsslt Service---------->");  
        String result = dictonary.get(word);  
        if(null == result){  
            result = "未找到您所查单词意义，请检查单词是否正确!";  
        }  
        return result;
	}

}

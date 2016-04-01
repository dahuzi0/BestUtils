package com.htjx.sdk.net;


/**
 * 解析工具抽象类
 * @author fada
 *
 * @param <T> 返回的对象类型
 */
public abstract class BaseParser<T> {
	/**
	 * 
	 * @param str json字符串
	 * @return 返回对象
	 */
	 public abstract T parseJSON(String str);
	 
	
}

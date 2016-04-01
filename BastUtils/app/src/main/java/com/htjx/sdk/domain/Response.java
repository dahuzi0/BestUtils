package com.htjx.sdk.domain;

import java.io.Serializable;
import java.util.List;
/**
 * 返回数据对象
 * @author fada
 *
 * @param <T> 对象类型
 */
public class Response<T> implements Serializable{
	
	private static final long serialVersionUID = 8271393381357325531L;
	private int code;//回执代码
	private String interfaceName;//接口名
	private List<T> result;//返回结果集
	private T resultSingle;//返回单个基本数据类型结果
	private String size;//更新内容大小
	/**
	 * 网络返回对象构造方法
	 * @param code 响应码
	 * @param interfaceName 接口名
	 * @param result 结果集
	 */
	public Response(int code, String interfaceName, List<T> result) {
		super();
		this.code = code;
		this.interfaceName = interfaceName;
		this.result = result;
	}
	/**
	 * 网络返回对象构造方法
	 * @param code 响应码
	 * @param interfaceName 接口名
	 * @param resultSingle 结果字符串
	 */
	public Response(int code, String interfaceName, T resultSingle) {
		super();
		this.code = code;
		this.interfaceName = interfaceName;
		this.resultSingle = resultSingle;
	}
	
	/**
	 * 网络返回对象构造方法
	 * @param code 响应码
	 * @param interfaceName 接口名
	 * @param result 结果集
	 * @param size 内容大小
	 */
	public Response(int code, String interfaceName, List<T> result,
			String size) {
		super();
		this.code = code;
		this.interfaceName = interfaceName;
		this.result = result;
		this.size = size;
	}
	/**
	 * 网络返回对象构造方法
	 * @param code 响应码
	 * @param interfaceName 接口名
	 * @param result 结果字符串
	 * @param size 内容大小
	 */
	public Response(int code, String interfaceName, T resultSingle, String size) {
		super();
		this.code = code;
		this.interfaceName = interfaceName;
		this.resultSingle = resultSingle;
		this.size = size;
	}


	public Response() {
		super();
	}
	/**
	 * 
	 * @return 响应码
	 */
	public int getCode() {
		return code;
	}
	/**
	 * 数据大小
	 * @return 数据大小
	 */
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
	/**
	 * 
	 * @return 结果集合List
	 */
	public List<T> getResult() {
		return result;
	}
	public void setResult(List<T> result) {
		this.result = result;
	}
	/**
	 * 
	 * @return 返回单个基本数据类型
	 */
	public T getResultSingle() {
		return resultSingle;
	}
	public void setResultSingle(T resultSingle) {
		this.resultSingle = resultSingle;
	}
	/**
	 * 
	 * @return 接口名
	 */
	public String getInterfaceName() {
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	@Override
	public String toString() {
		return "Response [code=" + code + ", interfaceName=" + interfaceName
				+ ", resultSingle=" + resultSingle + ", result=" + result
				+ ", size=" + size + "]";
	}



	  
	
}

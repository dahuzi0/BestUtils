package com.htjx.sdk.utils;

import java.net.URLEncoder;

public class StringUtils {
	public static String getUrlEncodePath(String path){
		try {
			String substring1 = path.substring(0, path.lastIndexOf("/") + 1);
			String substring = path.substring(path.lastIndexOf("/") + 1);
			path = substring1 + URLEncoder.encode(substring, "utf-8");
			return path;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String getFileType(String path){
		try {
			String substring = path.substring(path.lastIndexOf("."));
			return substring;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static String getDislodgeSuffix(String name){
		try {
			if(name.lastIndexOf(".")!=-1){
				String substring = name.substring(0,name.lastIndexOf("."));
				return substring;
			}else{
				return name;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

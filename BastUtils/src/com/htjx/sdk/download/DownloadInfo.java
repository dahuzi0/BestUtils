package com.htjx.sdk.download;

import java.io.Serializable;
/**
 * 下载对象
 * @author fada
 *
 */
public class DownloadInfo implements Serializable{
	/**
	 * name,path,iconpath,thid,done,totalsize
	 */
	private static final long serialVersionUID = 3694599642896659486L;
	private String name; //文件名
	private String packagename;//软件包名
	private String path; //文件下载路径
	private String iconpath; //图标下载路径
	private String thid; //文件id
	private long done; //已下载大小
	private long totalsize;//文件总大小
	private String info;//文件描述
	public String getName() {
		return name;
	}
	
	public String getPackagename() {
		return packagename;
	}

	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}

	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 
	 * @return 获取下载url
	 */
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getIconpath() {
		return iconpath;
	}
	public void setIconpath(String iconpath) {
		this.iconpath = iconpath;
	}
	public String getThid() {
		return thid;
	}
	public void setThid(String thid) {
		this.thid = thid;
	}
	public long getDone() {
		return done;
	}
	public void setDone(long done) {
		this.done = done;
	}
	public long getTotalsize() {
		return totalsize;
	}
	public void setTotalsize(long totalsize) {
		this.totalsize = totalsize;
	}
	
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * 
	 * @param name 软件名
	 * @param path 软件下载路径
	 * @param iconpath 图标路径
	 * @param thid id 
	 * @param done 已下多少
	 * @param totalsize 总大小
	 */
	public DownloadInfo(String name, String path, String iconpath, String thid,
			long done, long totalsize) {
		super();
		this.name = name;
		this.path = path;
		this.iconpath = iconpath;
		this.thid = thid;
		this.done = done;
		this.totalsize = totalsize;
	}
	public DownloadInfo() {
		super();
	}
	/**
	 * 
	 * @param name 软件名
	 * @param path 软件下载路径
	 * @param iconpath 软件图标路径
	 * @param thid 软件id
	 */
	public DownloadInfo(String name, String path, String iconpath,String thid) {
		super();
		this.name = name;
		this.path = path;
		this.iconpath = iconpath;
		this.thid = thid;
	}
	/**
	 * 
	 * @param name 软件名
	 * @param packagename 软件包名
	 * @param path 下载地址
	 * @param iconpath 图标地址
	 * @param thid 唯一标识
	 * @param done  文件已下载量
	 * @param totalsize 文件大小
	 * @param info 文件描述
	 */
	public DownloadInfo(String name, String packagename, String path,
			String iconpath, String thid, long done, long totalsize, String info) {
		super();
		this.name = name;
		this.packagename = packagename;
		this.path = path;
		this.iconpath = iconpath;
		this.thid = thid;
		this.done = done;
		this.totalsize = totalsize;
		this.info = info;
	}

	/**
	 * 
	 * @param name 软件名
	 * @param path 软件下载路径
	 * @param iconpath 图标路径
	 * @param thid id
	 * @param done 已下多少
	 */
	public DownloadInfo(String name, String path, String iconpath, String thid,
			long done) {
		super();
		this.name = name;
		this.path = path;
		this.iconpath = iconpath;
		this.thid = thid;
		this.done = done;
	}
	
	/**
	 * 
	 * @param name 软件名
	 * @param path 软件下载路径
	 * @param thid id
	 */
	public DownloadInfo(String name, String path, String thid) {
		super();
		this.name = name;
		this.path = path;
		this.thid = thid;
	}

	@Override
	public String toString() {
		return "DownloadInfo [name=" + name + ", path=" + path + ", iconpath="
				+ iconpath + ", thid=" + thid + ", done=" + done
				+ ", totalsize=" + totalsize + "]";
	}
	
}

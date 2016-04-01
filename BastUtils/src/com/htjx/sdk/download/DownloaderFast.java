package com.htjx.sdk.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.htjx.sdk.utils.ApkUtils;
import com.htjx.sdk.utils.LogUtils;
import com.htjx.sdk.utils.NetUtil;
import com.htjx.sdk.utils.StringUtils;

/**
 * 下载工具类
 * 
 * @author fada
 * 
 */
public class DownloaderFast {
	private static final int BUFFER_SIZE = 1024;
	private boolean isPause = false;
	private boolean isCancel;
	private Context context;
	public DownloadInfo downloadInfo;
	private Handler handler;
	private File file;
	private NetStateReceiver netStateReceiver;
	private SharedPreferences sp;
	private Editor edit;
	private String id;
	private int installStyle;

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            上下文
	 * @param downloadInfo
	 *            下载对象
	 * @param installStyle
	 *            下载后处理方式(0:表示什么都不做 1.静默安装 2.提示安装)
	 */
	public DownloaderFast(Context context, DownloadInfo downloadInfo,
			int installStyle) {
		this.downloadInfo = downloadInfo;
		this.installStyle = installStyle;
		this.id = downloadInfo.getThid();
		this.context = context;
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		edit = sp.edit();
		netStateReceiver = new NetStateReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.htjx.network");
		context.registerReceiver(netStateReceiver, filter);
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	/**
	 * 
	 * @param fileDir
	 *            本地存储目录 为null时是默认SD卡目录
	 * @throws Exception
	 */
	public void download(File fileDir) throws Exception {
		if (fileDir == null) {
			fileDir = new File(Environment.getExternalStorageDirectory()
					+ "/htjxsdk/");
		}
		if (NetUtil.hasConnectedNetwork(context)) {
			URL url = new URL(downloadInfo.getPath());
			if (!fileDir.exists()) {
				fileDir.mkdir();
			}
			file = new File(fileDir, StringUtils.getDislodgeSuffix(downloadInfo
					.getName()));
			if (!file.exists()) {
				new DownloadThread(url, file).start();
			} else {
				LogUtils.d("" + file.getName() + "文件已存在");
			}
		}
	}

	private final class DownloadThread extends Thread {
		private URL url;
		private File file, localFile;
		private InputStream in;
		private BufferedInputStream bis = null;
		private RandomAccessFile fos = null;
		private URLConnection con = null;
		private OutputStream os = null;
		private int endPosition;

		public DownloadThread(URL url, File file) {
			this.url = url;
			this.file = file;
			localFile = new File(file.getAbsolutePath()
					+ StringUtils.getFileType(url.toString()));
		}

		@Override
		public void run() {
			if (!manageFile()) {
				long curPosition = sp.getLong(id, 0);// 查询记录当中没有下完的任务
				try {

					byte[] buf = new byte[BUFFER_SIZE];
					con = url.openConnection();
					con.setAllowUserInteraction(true);
					endPosition = con.getContentLength();
					if (curPosition > 0) {
						// 设置当前线程下载的起点，终点
						con.setRequestProperty("Range", "bytes=" + curPosition
								+ "-" + endPosition);
						// 使用java中的RandomAccessFile 对文件进行随机读写操作
						fos = new RandomAccessFile(file, "rw");
						// 设置开始写文件的位置
						fos.seek(curPosition);
						bis = new BufferedInputStream(con.getInputStream());
						// 开始循环以流的形式读写文件
						while (curPosition < endPosition) {
							int len = bis.read(buf, 0, BUFFER_SIZE);
							if (len == -1) {
								break;
							}
							fos.write(buf, 0, len);
							curPosition = curPosition + len;
							downloadInfo.setDone(curPosition);
							edit.putLong(id, curPosition);
							edit.commit();
							if (handler != null) {
								Message msg = Message.obtain();
								msg.what = 1;
								msg.getData().putLong("done", curPosition);
								msg.getData().putLong("fileLen", endPosition);
								handler.sendMessage(msg);
							}
						}
					} else {
						int len;
						in = con.getInputStream();
						os = new FileOutputStream(file);
						while ((len = in.read(buf)) != -1) {
							os.write(buf, 0, len);
							curPosition = curPosition + len;
							downloadInfo.setDone(curPosition);
							edit.putLong(id, curPosition);
							edit.commit();
							if (handler != null) {
								Message msg = Message.obtain();
								msg.what = 1;
								msg.getData().putLong("done", curPosition);
								msg.getData().putLong("fileLen", endPosition);
								handler.sendMessage(msg);
							}
						}

					}
					if (isPause) {
						synchronized (DownloaderFast.this) {
							try {
								DownloaderFast.this.wait();// 暂停时该线程进入等待状态，并释放dao的锁
								if (!isCancel) {
									con = url.openConnection();
									con.setAllowUserInteraction(true);
									endPosition = con.getContentLength();
									if (curPosition > 0) {
										// 设置当前线程下载的起点，终点
										con.setRequestProperty("Range",
												"bytes=" + curPosition + "-"
														+ endPosition);
										// 使用java中的RandomAccessFile 对文件进行随机读写操作
										fos = new RandomAccessFile(file, "rw");
										// 设置开始写文件的位置
										fos.seek(curPosition);
										bis = new BufferedInputStream(
												con.getInputStream());
									}
								}
							} catch (Exception e) {
								removeJob();
								e.printStackTrace();
							}
						}
					}

					if (netStateReceiver != null) {
						context.unregisterReceiver(netStateReceiver);
						netStateReceiver = null;
					}
					// 删除下载记录
					if (curPosition >= downloadInfo.getTotalsize()) {
						LogUtils.d("下载文件完成:" + downloadInfo + "url=:"
								+ url.toString());
						edit.putLong(id, 0);
						edit.commit();
						Intent downloadover = new Intent(
								"com.htjx.downloadover");
						downloadover.putExtra("name", downloadInfo.getName());
						downloadover.putExtra("id", downloadInfo.getThid());
						if (downloadInfo.getPackagename() != null) {
							downloadover.putExtra("pkName",
									downloadInfo.getPackagename());
						}
						context.sendBroadcast(downloadover);
						if (localFile.exists()) {
							localFile.delete();
						}
						file.renameTo(localFile);
						manageFile();
					}

				} catch (IOException e) {
					removeJob();
					e.printStackTrace();
				} finally {
					try {
						if (netStateReceiver != null) {
							context.unregisterReceiver(netStateReceiver);
							netStateReceiver = null;
						}
						handler = null;
						if (in != null) {
							in.close();
						}
						if (fos != null) {
							fos.close();
						}
						if (bis != null) {
							bis.close();
						}
						if (os != null) {
							os.close();
						}
					} catch (Exception e2) {
						if (netStateReceiver != null) {
							context.unregisterReceiver(netStateReceiver);
							netStateReceiver = null;
						}
						e2.printStackTrace();
					}
				}
			}
		}

		/**
		 * 处理文件
		 * 
		 * @return 文件存在与否
		 */
		private boolean manageFile() {
			if (localFile != null && localFile.exists()) {
				switch (installStyle) {
				case 1:// 静默
					ApkUtils.slientInstall(localFile);
					break;
				case 2:// 普通
					ApkUtils.InstallApk(context, localFile.getAbsolutePath());// 安装应用
					break;
				}
				return true;
			}
			return false;
		}

		private void removeJob() {
			if (handler != null) {
				Message msg = Message.obtain();
				msg.what = -1;
				handler.sendMessage(msg);
			}
		}
	}

	/**
	 * 暂停下载
	 */
	public void pause() {
		isPause = true;
	}

	/**
	 * 停止下载
	 */
	public void cancel() {
		isPause = false;
		isCancel = true;
		// 恢复所有线程
		synchronized (this) {
			this.notifyAll();
		}
	}

	/**
	 * 继续下载
	 */
	public void resumeDownload() {
		isPause = false;
		// 恢复所有线程
		synchronized (this) {
			this.notifyAll();
		}
	}

	/**
	 * 删除下载
	 * 
	 * @param id
	 */
	public void delete(String id) {
		isPause = false;
		isCancel = true;
		// 恢复所有线程
		synchronized (this) {
			this.notifyAll();
		}

	}

	class NetStateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			boolean network = intent.getBooleanExtra("network", true);
			LogUtils.d("收到网络状态广播了:=" + network);
			if (handler != null && !network) {
				handler.sendEmptyMessage(-1);
			}

		}

	}
}
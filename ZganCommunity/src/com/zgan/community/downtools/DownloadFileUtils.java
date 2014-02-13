package com.zgan.community.downtools;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.util.Log;

/**
 * 多线程文件下载工具类
 * @author 
 *
 */
public class DownloadFileUtils {
	private final String TAG = "DownloadFileUtils"; 
	private String url;//下载地址
	private long fileSize;//下载的文件大小
	private long totalReadSize;//已读取的文件大小
	private long block;//每条线程下载的长度
	private int threadCount;//下载的线程数
	private final int threadPoolNum = 5;//线程池的大小
	private final int bufferSize = 1024 * 100;//缓冲区大小
	private String fileName;//存储在本地的文件名称
	private String filePath;//存储的路径
	private HttpURLConnection urlConnection;
	private RandomAccessFile randomAccessFile;//根据指定位置写入数据
	private URL uri;
	private DownloadFileCallback callback;//下载的回调接口
	private ExecutorService executorService;//固定大小的线程池
	private volatile boolean error = false;//全局变量，使用volatile同步，下载产生异常时改变
	private File[] tempFiles;//保存thread的下载进度缓存文件的集合
	public DownloadFileUtils(String url,String filePath,String fileName,int threadCount,DownloadFileCallback callback){
		this.url = url;
		this.filePath = filePath;
		this.fileName = fileName;
		this.threadCount = threadCount;
		this.callback = callback;
		tempFiles = new File[threadCount];
	}
	
	public long getFileSize() {
		return fileSize;
	}
	public long getTotalReadSize() {
		return totalReadSize;
	}
	/**
	 * 文件下载
	 * @return true 下载成功 false 下载失败
	 */
	public boolean downloadFile(){
		try {
			uri = new URL(url);
			urlConnection = (HttpURLConnection) uri.openConnection();
			urlConnection.setRequestMethod("GET");
			if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
				fileSize = urlConnection.getContentLength();//获取文件的长度
				block = fileSize / threadCount + 1;//为了避免文件长度缺失每条线程下载长度增加1
				File file = new File(filePath,fileName);
				if(!file.getParentFile().exists())
					file.getParentFile().mkdirs();
				executorService = Executors.newFixedThreadPool(threadPoolNum);
				CountDownLatch countDownLatch = new CountDownLatch(threadCount);//线程计数器
				for(int i = 0; i < threadCount; i++){
					long startPosition = i * block;//每条线程的开始读取位置
					long endPosition = (i+1) * block - 1;//每条线程的读取结束位置
					randomAccessFile = new RandomAccessFile(file, "rwd");
					executorService.execute(new DownloadThread(i+1, startPosition, endPosition, randomAccessFile,countDownLatch));
				}
				countDownLatch.await();//阻塞线程,直到countDownLatch线程数为零
				for(int i = 0; i < threadCount; i++){
					if(tempFiles[i] != null && tempFiles[i].exists())
						tempFiles[i].delete();
				}
				executorService.shutdown();
				callback.downloadSuccess(null);//下载成功时的回调
				Log.i(TAG, "下载成功。。。");
				return true;
			}
		} catch (Exception e) {
			callback.downloadError(e, "");//下载失败的回调
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	class DownloadThread implements Runnable{
		private int threadId;
		private long startPosition;
		private long endPosition;
		private RandomAccessFile randomAccessFile;
		private CountDownLatch countDownLatch;
		private boolean isFirst = true;
		private long[] startPositions;
		private long[] endPositions;
		private File tempFile;
		public DownloadThread(int threadId,long startPosition,long endPosition,RandomAccessFile randomAccessFile,CountDownLatch countDownLatch){
			this.threadId = threadId;
			this.startPosition = startPosition;
			this.endPosition = endPosition;
			this.randomAccessFile = randomAccessFile;
			this.countDownLatch = countDownLatch;
			tempFile = new File(filePath+"/thread"+threadId,fileName.replaceAll(".apk", ".position"));
			tempFiles[threadId - 1] = tempFile;
			if(tempFile.exists()){
				isFirst = false;
				readPositionInfo();
			}else{
				tempFile.getParentFile().mkdirs();
				startPositions = new long[threadCount];
				endPositions = new long[threadCount];
			}
		}
		@Override
		public void run() {
			try {
				HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
				connection.setRequestMethod("GET");// 以GET方式连接
				connection.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
				connection.setConnectTimeout(5 * 60 * 1000);// 设置连接超时
				connection.setReadTimeout(60 * 1000);//设置数据读取超时
				connection.setAllowUserInteraction(true);// 允许用户交互
				if (isFirst) {
					randomAccessFile.seek(startPosition);
				}else{
					startPosition = startPositions[threadId - 1];
					endPosition = endPositions[threadId - 1];
					randomAccessFile.seek(startPosition);
				}
				connection.setRequestProperty("Range", "bytes=" + startPosition + "-" + endPosition);// 设置每条线程开始下载的位置
				InputStream inputStream = new BufferedInputStream(connection.getInputStream(), bufferSize);// 使用缓冲区读取文件
				byte[] b = new byte[bufferSize];
				int len = 0;
				long readSize = startPosition;
				while (!error && (len = inputStream.read(b)) != -1) {
					randomAccessFile.write(b, 0, len);
					totalReadSize += len;
					readSize += len;
					savePositionInfo(readSize, endPosition,totalReadSize);
				}
				if (!error)
					Log.d(TAG, "线程" + threadId + "下载完成。。。");
				else
					Log.e(TAG, "线程" + threadId + "下载失败。。。");
				inputStream.close();
				randomAccessFile.close();
				connection.disconnect();
				countDownLatch.countDown();// 每条线程执行完之后减一
			} catch (Exception e) {
				Log.e(TAG, "线程" + threadId + "下载失败。。。");
				error = true;
				e.printStackTrace();
				callback.downloadError(e, "");// 下载失败的回调
			}
		}
		/**
		 * 将每条线程下载的开始和结束位置写入到临时文件中
		 */
		private void savePositionInfo(long startPosition,long endPosition,long totalReadSize){
			try {
				DataOutputStream outputStream = new DataOutputStream(new FileOutputStream(tempFile));
				outputStream.writeInt(startPositions.length);
				outputStream.writeLong(totalReadSize);
				for(int i = 0; i < startPositions.length; i++){
					outputStream.writeLong(startPosition);
					outputStream.writeLong(endPosition);
				}
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		/**
		 * 读取临时文件
		 */
		private void readPositionInfo(){
			try {
				DataInputStream inputStream = new DataInputStream(new FileInputStream(tempFile));
				int startPositionLength = inputStream.readInt();
				totalReadSize = inputStream.readLong();
				startPositions = new long[startPositionLength];
				endPositions = new long[startPositionLength];
				for(int i = 0; i < startPositionLength; i++){
					startPositions[i] = inputStream.readLong();
					endPositions[i] = inputStream.readLong();
				}
				inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
}

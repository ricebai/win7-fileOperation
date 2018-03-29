package com.swing.win.event;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileProcessorSource {
	long length;	// 总长度
	long handleLength;// 已处理文件总大小 
	long count;		// 文件总数
	long directorys;// 目录总数
	long finishs;	// 完成文件数
	long errors;	// 异常文件数
	long startTime;	//
	long tempCount;
	
	File file;		// 正在执行的文件
	File save;		// 保存目录

	File[] files;	// 源文件
	Map<File, String> dirMap; // 目录集合
	Map<File, String> fileMap; // 文件集合
	
	public FileProcessorSource(File[] files) {
		fileMap = new HashMap<File, String>();
		dirMap = new HashMap<File, String>();
		this.files = files;
	}
	
	public long getHandleLength() {
		return handleLength;
	}

	public long getLength() {
		return length;
	}

	public long getCount() {
		return count;
	}

	public long getDirectorys() {
		return directorys;
	}

	public long getFinishs() {
		return finishs;
	}

	public long getErrors() {
		return errors;
	}

	public File getFile() {
		return file;
	}

	public File getSave() {
		return save;
	}

	public File[] getFiles() {
		return files;
	}
	
	public long getStartTime() {
		return startTime;
	}
	
	/**
	 * 调用获得执行时间段的处理数据长度
	 * 可用于计算每秒的执行速度(m/s)
	 * @return long
	 */
	public long getPointLength(){
		long temp = handleLength - tempCount;
		tempCount = handleLength;
		return temp;
	}
}
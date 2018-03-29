package com.swing.win.tools;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;

@SuppressWarnings("deprecation")
public class FileTool {
	public static String[] FORMATE_SIZE = new String[] { "b", "kb", "mb", "gb",
			"tb" };

	/**
	 * 获得目录中的文件 URL
	 * 
	 * @param dirPath
	 *            文件目录
	 * @return URL[]
	 * @throws MalformedURLException
	 */
	public static URL[] getURLs(String dirPath) throws MalformedURLException {
		return getURLs(dirPath, null);
	}

	/**
	 * 获得目录中的文件 URL
	 * 
	 * @param dirPath
	 *            文件目录
	 * @param filter
	 *            过滤器
	 * @return URL[]
	 * @throws MalformedURLException
	 */
	public static URL[] getURLs(String dirPath, FileFilter filter)
			throws MalformedURLException {
		URL[] urls = null;
		if (!Assist.isEmpty(dirPath)) {
			File file = new File(dirPath);
			if (file.isDirectory()) {
				File files[] = file.listFiles(filter);
				if (files == null || files.length < 1)
					return null;
				urls = new URL[files.length];
				int i = files.length;
				while (--i != -1)
					urls[i] = files[i].toURL();
			} else if (file.isFile()) {
				urls = new URL[1];
				urls[0] = file.toURL();
			}
		}
		return urls;
	}

	/**
	 * 将File.length处理成带有容量单位的字符串 (字节~TB)
	 * 
	 * @param length
	 *            容量大小
	 * @param residue
	 *            保留小数点后的位数
	 * @return
	 */
	public static String toCapacity(double length, int residue) {
		return toCapacity(length, 0, residue);
	}

	/**
	 * 内部运算方法
	 * 
	 * @param size
	 *            大小
	 * @param formatIndex
	 *            格式化下表
	 * @param residue
	 *            小数点
	 * @return
	 */
	private static String toCapacity(double size, int formatIndex, int residue) {
		String ret = null;
		if (size > 1024 && formatIndex < FORMATE_SIZE.length) {
			ret = toCapacity(size / 1024d, ++formatIndex, residue);
		} else {
			String l = String.valueOf(size);
			int ind = l.indexOf(".");
			if (residue <= 0)
				residue--;
			else
				ind += (residue + 1);
			if (ind < l.length()) {
				l = l.substring(0, ind);
			}
			ret = l + " " + FORMATE_SIZE[formatIndex];
		}
		return ret;
	}

	/**
	 * 获得后缀
	 * 
	 * @parme fileName 文件名称(name.txt)
	 * @return 获得后缀(.txt),没有.则返回null.
	 */
	public static String suffix(String fileName) {
		int end = fileName.lastIndexOf(".");
		if (end != -1) {
			return fileName.substring(end);
		}
		return null;
	}

	/**
	 * 获得前缀
	 * 
	 * @parme fileName 文件名称(name.txt)
	 * @return 获得.以前的名称
	 */
	public static String prefix(String fileName) {
		int end = fileName.lastIndexOf(".");
		if (end != -1) {
			return fileName.substring(0, end);
		}
		return fileName;
	}
	
}
package com.swing.win;

import java.io.File;

import javax.swing.UIManager;

public class DemoMain {

	public static void main(String[] args) throws Exception {
		// 系统皮肤
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		final JFileProcessor f =  new JFileProcessor();
		f.setVisible(true);
		new Thread(new Runnable() {
			
			@SuppressWarnings("static-access")
			@Override
			public void run() {
				try {
					// 启动1秒延迟
					Thread.currentThread().sleep(1000);
					
					// 拷贝
					f.showCopy(
							// 拷贝到指定目录
							new File("D:\\abc"),
							
							// 多个文件参数
							new File("D:\\test\\a.exe"),
							new File("D:\\test\\b.rar"),
							new File("D:\\test\\c.txt"));
					
					// 剪切
//					f.showCut(
//							new File("D:\\abc"),
//							new File("D:\\def\\a.dmg"),
//							new File("D:\\def\\b.dmg"),
//							new File("D:\\def\\c.dmg"));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}).start();
		
	}
}

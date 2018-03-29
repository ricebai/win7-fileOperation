package com.swing.win.tools;

public class DateTools {

	
	public static String toDateString(long m){
		int ss = (int) (m % 60);
		int mm = (int) (m / 60 % 60);
		int hh = (int) (m / 60 / 60 % 24);
		int dd = (int) (m / 60 / 60 / 24);
		
		StringBuffer sb = new StringBuffer();
		if(dd > 0){
			sb.append(dd);
			sb.append("天");
		}
		if(hh > 0){
			sb.append(hh);
			sb.append("时");
		}
		if(mm > 0){
			sb.append(mm);
			sb.append("分");
		}
		if(ss > 0){
			sb.append(ss);
			sb.append("秒");
		}
		
		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(DateTools.toDateString(24*60*60 + 10000));
	}
}

package com.swing.win.event;


public interface FileProcessorListener {

	public void check(FileProcessorEvent e);
	
	public void change(FileProcessorEvent e);
	
	public void length(FileProcessorEvent e);
	
	public void status(FileProcessorEvent e);
	
}

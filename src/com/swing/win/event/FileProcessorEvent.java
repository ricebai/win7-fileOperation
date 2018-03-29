package com.swing.win.event;

import java.io.File;

public class FileProcessorEvent {

	public static int 
	CHECK		=-1,
	CHANGE		= 0,
	LENGTH		= 1,
	SUSPEND		= 2,
	RESUME		= 3,
	ERROR		= 4,
	INTERRUPT	= 5,
	START		= 6,
	FINISH		= 7;
	
	private int id ;
	
	private FileProcessorSource data;
	
	// current file
	private File file;
	
	private Exception error;
	
	public FileProcessorEvent(int status, FileProcessorSource data, Exception e){
		this.id = status;
		this.data = data;
		if(data != null)
		this.file = data.file;
		this.error = e;
	}
	
	public int getID() {
		return id;
	}
	public File getFile() {
		return file;
	}
	public FileProcessorSource getData() {
		return data;
	}
	public Exception getError() {
		return error;
	}

	@Override
	public String toString() {
		return "FileEvent [id=" + id + ", data=" + data + ", file=" + file
				+ ", error=" + error + "]";
	}
}

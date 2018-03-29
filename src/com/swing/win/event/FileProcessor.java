package com.swing.win.event;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * file processor.
 * 
 * @author tomorrow
 */
public class FileProcessor {

	/** The file listener set. */
	private Set<FileProcessorListener> l;
	/** The overall execute thread. */
	private FileProcessorThread executeThread;
	private FileProcessorSource source;

	/** dynamic bit max length. */
	private long maxBuffer = 1024 * 1024;

	/**
	 * The execute status. (0=>copy, 1=>cut, -1=>delete)
	 */
	int status;

	public FileProcessor() {
		l = new HashSet<FileProcessorListener>();
		executeThread = new FileProcessorThread(this);
	}

	/**
	 * The new file.
	 * @param file
	 * @param path
	 * @return
	 */
	File newFile(File file, String path) {
		File newFile = null;
		if (path == null) {
			newFile = new File(source.save.getPath() + File.separator
					+ file.getName());
		} else {
			newFile = new File(file.getPath().replace(path,
					source.save.getPath() + File.separator));
		}
		return newFile;
	}

	/**
	 * check file.
	 */
	void check() {
		for (File f : source.files) {
			source.file = f;
			fireChange(FileProcessorEvent.CHECK);
			if (f.isDirectory()) {
				++source.directorys;
				check0(f, f.getParent());
			} else {
				++source.count;
				source.length += f.length();
				source.fileMap.put(f, null);
			}
		}
	}
	
	private void check0(File file, String dirPath) {
		for (File f : file.listFiles()) {
			source.file = f;
			fireChange(FileProcessorEvent.CHECK);
			if (f.isDirectory()) {
				++source.directorys;
				check0(f, dirPath);
				source.dirMap.put(f, dirPath);
			} else {
				++source.count;
				source.length += f.length();
				source.fileMap.put(f, dirPath);
			}
		}
	}

	/**
	 * The get file source.
	 * @return SmartFileData
	 */
	public FileProcessorSource getSource() {
		return source;
	}
	
	/**
	 * The buffer length.
	 * @param file
	 * @return
	 */
	public int buffer(File file){
		return (int) (file.length() > maxBuffer ? maxBuffer : file.length());
	}
	
	/**
	 * The fire interrupt.
	 * @return 
	 */
	public void interrupt() {
		if(executeThread.interrupt()){
			fireChange(FileProcessorEvent.INTERRUPT);
		}
	}

	/**
	 * The suspend file thread.
	 */
	public void suspend() {
		executeThread.suspend();
		fireChange(FileProcessorEvent.SUSPEND);
	}

	/**
	 * The resume file thread.
	 */
	public void resume() {
		executeThread.resume();
		fireChange(FileProcessorEvent.RESUME);
	}

	/**
	 * The execute.
	 * @param dir
	 * @param files
	 */
	private void execute(File dir,File... files) {
		if(executeThread != null && executeThread.isAlive())
			new Exception("Please stop the thread");
		
		source = new FileProcessorSource(files);
		source.save = dir;
		executeThread.start();
	}

	/**
	 * The delete files.
	 * @param files	delete files
	 */
	public void delete(File... files) {
		status = -1;
		execute(null, files);
	}

	/**
	 * The files copy to directory. 
	 * @param dir	directory
	 * @param files	files
	 */
	public void copy(File dir, File... files) {
		status = 0;
		execute(dir, files);
	}

	/**
	 * The files curt to directory.
	 * @param dir	directory
	 * @param files	cut files
	 */
	public void cut(File dir, File... files) {
		status = 1;
		execute(dir, files);
	}

	/**
	 * The add file listener.
	 * @param fl	listener
	 */
	public void addFileListener(FileProcessorListener fl) {
		l.add(fl);
	}

	/**
	 * The remove file listener.
	 * @param fl	listener
	 */
	public void removeFileListener(FileProcessorListener fl) {
		l.remove(fl);
	}

	/**
	 * The get file listener.
	 * @return	Set<FileListener> set
	 */
	public Set<FileProcessorListener> getFileListener() {
		return l;
	}

	/**
	 * The fire event.
	 * @param status	event status
	 */
	void fireChange(int status) {
		fireChange(status, null);
	}
	/**
	 * The fire exception
	 * @param e	exception
	 */
	void fireChange(Exception e) {
		fireChange(FileProcessorEvent.ERROR, e);
	}
	
	/**
	 * The overall event.
	 * @param status
	 * @param e
	 */
	private void fireChange(int status, Exception e) {
		// error count;
		if(source != null && status == FileProcessorEvent.ERROR)
			++ source.errors;
		
		if(l.size() < 1)
			return;
		FileProcessorEvent ev = new FileProcessorEvent(status, source, e);
		Iterator<FileProcessorListener> iter = l.iterator();
		while (iter.hasNext()) {
			switch (status) {
			case -1:
				iter.next().check(ev);
				break;
			case 0:
				iter.next().change(ev);
				break;
			case 1:
				iter.next().length(ev);
				break;
			default:
				iter.next().status(ev);
				break;
			}
		}
	}
	public int getId() {
		System.out.println(status);
		return status;
	}
	/**
	 * The use max buffer length.
	 * @param maxBuffer
	 */
	public void setMaxBuffer(long maxBuffer) {
		this.maxBuffer = maxBuffer;
	}
	
	/**
	 * The max bits length.
	 * @return long
	 */
	public long getMaxBuffer() {
		return maxBuffer;
	}
	
	public boolean isSuspend() {
		return executeThread.isSuspend();
	}
	
	public boolean isAlive(){
		return executeThread.isAlive();
	}
}

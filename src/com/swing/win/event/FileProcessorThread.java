package com.swing.win.event;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import com.swing.win.Threading;

class FileProcessorThread extends Threading {
	
	private InputStream in;
	private OutputStream out;
	private byte[] bits;
	private int end;
	private FileProcessorSource data;

	private FileProcessor manager;
	
	public FileProcessorThread(FileProcessor manager) {
		this.manager = manager;
	}

	@Override
	public void run() {
		data = manager.getSource();
		data.startTime = System.currentTimeMillis();
		
		// check files
		manager.check();
		manager.fireChange(FileProcessorEvent.START);
		// create directory
		if(manager.status != -1){
			if(data.save == null){
				new Exception("save file path is null");
			}else{
				try {
					if(!data.save.isDirectory())
						data.save.mkdirs();
				} catch (Exception e) {
					new Exception("save file is not directory path!");
				}
			}
			
			for(File file : data.dirMap.keySet()){
				manager.newFile(file, data.dirMap.get(file)).mkdirs();
			}
		}
		
		Iterator<File> iter = data.fileMap.keySet().iterator();
		while(iter.hasNext() && !isInterrupt()){
			data.file = iter.next();
			manager.fireChange(FileProcessorEvent.CHANGE);
			switch (manager.status) {
			case -1:
				delete();
				break;
			case 0:
				copy();
				break;
			case 1:
				cut();
				break;
			}
		}
		
		// delete all directory
		if(manager.status == -1 || manager.status == 1){
			for(File f : data.files){
				if(f == null)
					continue;
				deleteDir(f.listFiles());
				data.file = f;
				manager.fireChange(FileProcessorEvent.CHANGE);
				if(!f.delete()){
					manager.fireChange(FileProcessorEvent.ERROR);
				}
			}
		}
		manager.fireChange(FileProcessorEvent.FINISH);
	}

	@Override
	public boolean runner() {
		try {
			if ((end = in.read(bits)) != -1) {
				out.write(bits, 0, end);
				out.flush();
				// handle file length count;
				data.handleLength += end;
				manager.fireChange(FileProcessorEvent.LENGTH);
			}
		} catch (IOException e) {
			manager.fireChange(FileProcessorEvent.ERROR);
			end = -1;
		}
		return end > 0;
	}
	
	private void deleteDir(File[] fs){
		if(fs == null)
			return;
		if(fs.length > 0)
		for(File f : fs){
			deleteDir(f.listFiles());
			data.file = f;
			manager.fireChange(FileProcessorEvent.CHANGE);
			if(!f.delete()){
				manager.fireChange(FileProcessorEvent.ERROR);
			}
		}
	}
	private void delete(){
		data.handleLength += data.file.length();
		if(data.file.delete()){
			// finish count;
			++ data.finishs;
		}else{
			// error count;
			++ data.errors;
		}
	}
	private void copy(){
		if(data.file.length() < 1){
			try {
				manager.newFile(data.file, data.fileMap.get(data.file)).createNewFile();
				++ data.finishs;
			} catch (IOException e) {
				manager.fireChange(e);
			}
			return;
		}
		
		bits = new byte[manager.buffer(data.file)];
		try {
			in = new FileInputStream(data.file);
			out = new FileOutputStream(manager.newFile(data.file, data.fileMap.get(data.file)));
			super.run();
			if(isInterrupt()){
				manager.fireChange(FileProcessorEvent.INTERRUPT);
			}
			// finish count;
			++ data.finishs;
		} catch (IOException e) {
			manager.fireChange(e);
		} finally{
			closeStream();
		}
	}
	
	private void cut(){
		// file length < define bit length.
		if(data.file.length() <= manager.getMaxBuffer()){
			data.handleLength += data.file.length();
			// can use system cut method.
			if(!data.file.renameTo(manager.newFile(data.file, data.fileMap.get(data.file)))){
				manager.fireChange(FileProcessorEvent.ERROR);
			}else{
				++ data.finishs;
			}
			manager.fireChange(FileProcessorEvent.LENGTH);
			return;
		}
		
		// be use for smart calculate bit length.
		bits = new byte[manager.buffer(data.file)];
		try {
			in = new FileInputStream(data.file);
			out = new FileOutputStream(manager.newFile(data.file, data.fileMap.get(data.file)));
			super.run();
			if(isInterrupt()){
				manager.fireChange(FileProcessorEvent.INTERRUPT);
			}
			// finish count;
			++ data.finishs;
		} catch (IOException e) {
			manager.fireChange(e);
		} finally{
			closeStream();
		}
		
		// cut after delete source file.
		if(!data.file.delete()){
			manager.fireChange(FileProcessorEvent.ERROR);
		}
	}

	/**
	 * The close stream
	 */
	private void closeStream(){
		try {
			if (out != null)
				out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (in != null)
				in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// reset bits.
		bits = null;
	}
}
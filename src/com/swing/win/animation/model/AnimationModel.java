package com.swing.win.animation.model;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public abstract class AnimationModel {

	private BufferedImage currImg, nextImg;
	
	private boolean forward;
	
	// 运行总数
	public abstract int runCount();
	
	// 停顿时间
	public abstract long sleepTime();
	
	public abstract String getName();

	public void screenshot(Component current, Component next, boolean forward, boolean isOpaque){
		currImg = screenshot0(current, isOpaque);
 		nextImg = screenshot0(next, isOpaque);
 		this.forward = forward;
	}
	
	public void clearBuffer(){
		currImg= null;
		nextImg = null;
		System.gc();
	}
	
	private BufferedImage screenshot0(Component current, boolean isOpaque) {
		BufferedImage image =(BufferedImage) current.createImage(current.getWidth(), current.getHeight());
 		current.paint(image.getGraphics());
		return image;
	}

	protected BufferedImage getCurrentImage(){
		return currImg;
	}
	protected BufferedImage getNextImage(){
		return nextImg;
	}
	
	public void setRenderingHint(Graphics2D g){
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}
	public AlphaComposite getComposite(int rc){
		return AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)rc/runCount());
	}
	
	public boolean isForward() {
		return forward;
	}

	public abstract void draw(Graphics2D g, Rectangle bounds, int runCount, ImageObserver observer);
}
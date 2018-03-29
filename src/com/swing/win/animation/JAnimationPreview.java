package com.swing.win.animation;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import com.swing.win.animation.model.AnimationModel;

@SuppressWarnings("serial")
public class JAnimationPreview extends JComponent{
	
	BufferedImage currImg, nextImg;
	
	JAnimationThread thread;
	JAnimationPanel panel;
	
	public JAnimationPreview(JAnimationPanel panel) {
		this.panel = panel;
		thread = new JAnimationThread(this);
		setOpaque(true);
		setVisible(false);
	}
	
	public void fireAnimation(Component current, Component next, boolean forward){
		setVisible(true);
		AnimationModel model = panel.getAnimationModel();
		model.screenshot(current, next, forward, this.isOpaque());
		thread.start(model, forward);
	}

	
	@Override
	public void paint(Graphics g) {
		if(thread.isStart()){
			if(this.isOpaque()){
				g.setColor(panel.getBackground() == null ?Color.black:panel.getBackground());
				g.fillRect(0, 0, getWidth(), getHeight());
			}
			panel.getAnimationModel().draw((Graphics2D) g, panel.getBounds(), thread.runCount(), this);
		}
	}
	
}

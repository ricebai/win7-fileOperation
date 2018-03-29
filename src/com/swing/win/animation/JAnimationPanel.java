package com.swing.win.animation;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;

import com.swing.win.animation.model.AnimationModel;

@SuppressWarnings("serial")
public class JAnimationPanel extends JComponent{

	private static final Integer UP_LAYER = new Integer(1);
	
	JLayeredPane layer;
	JAnimationPreview preview;
	AnimationModel model;
	
	int viewIndex = 0;
	List<Component> cs ;
	
	public JAnimationPanel() {
		cs = new ArrayList<Component>();
		setLayout(new BorderLayout());
		super.addImpl(layer = new JLayeredPane(){
			@Override
			public void paint(Graphics g) {
				synchronized (this.getTreeLock()) {
					for(Component c: getComponents()){
						c.setBounds(0, 0, getWidth(), getHeight());
					}
				}
				super.paint(g);
			}
		}, null, 0);
		layer.add(preview = new JAnimationPreview(this), new Integer(50));
	}
	
	public JLayeredPane getLayer() {
		return layer;
	}
	
	@Override
	protected void addImpl(Component comp, Object constraints, int index) {
		cs.add(comp);
		layer.add(comp, cs.size() > 0 ? JLayeredPane.DEFAULT_LAYER : UP_LAYER, index);
	}

	
	public void switchComponent(boolean b) {
		Component current = cs.get(viewIndex);
		
		Component next = cs.get(b ? (viewIndex+1>= cs.size()?viewIndex=0:++viewIndex):
			(viewIndex-1<0?viewIndex=cs.size()-1:--viewIndex));
		preview.fireAnimation(current, next, b);
		layer.setLayer(current, JLayeredPane.DEFAULT_LAYER);
		layer.setLayer(next, UP_LAYER);
	}

	public AnimationModel getAnimationModel() {
		return model;
	}
	
	public void setAnimationModel(AnimationModel model) {
		this.model = model;
	}

	public void setJAnimationPreviewOpaque(boolean isOpaque){
		preview.setOpaque(isOpaque);
	}
	
	public boolean getJAnimationPreviewOpaque(){
		return preview.isOpaque();
	}
	
	
}

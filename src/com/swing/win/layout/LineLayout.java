package com.swing.win.layout;

import java.awt.Component;
import java.awt.Container;

/**
 * The line layout.
 * 
 * @author tomorrow
 * @version 1.0
 * @time	2014-12-13 23:58:15
 */
@SuppressWarnings("serial")
public class LineLayout extends BasicLayout {
	private int h, vh ,y;
	private boolean hor;
	
	public LineLayout(int h, int vh) {
		this(h, vh, true);
	}
	
	public LineLayout(int h, int vh, boolean hor) {
		this.h = h;
		this.vh = vh;
		this.hor = hor;
	}

	@Override
	public void layoutContainer(Container parent) {
		if(parent.getComponentCount() > 0)
		synchronized (parent.getTreeLock()) {
			y = vh;
			for (Component c : parent.getComponents()) {
				if(!c.isVisible())
					continue;
				if(hor)
					c.setBounds(y, vh, h - vh * 2, parent.getHeight() - vh * 2);
				else
					c.setBounds(vh, y, parent.getWidth() - vh * 2, h - vh * 2);
				y += h - vh;
			}
//			parent.setPreferredSize(new Dimension(hor ? y : 1, hor ? 1 : y));
		}
	}
}

package com.swing.win.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;

/**
 * 
 * @author tomorrow
 * @version	1.0
 * @time	2014-12-13 23:57:06
 */
@SuppressWarnings("serial")
public class BasicLayout implements LayoutManager2, java.io.Serializable {
	
	@Override
	public void addLayoutComponent(Component comp, Object c) {
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		return minimumLayoutSize(target);
	}

	@Override
	public void addLayoutComponent(String name, Component comp) {
		addLayoutComponent(comp, null);
	}

	@Override
	public void layoutContainer(Container parent) {
		if(parent.getComponentCount() > 0){
			
			synchronized (parent.getTreeLock()) {
				Insets ins = parent.getInsets();
				for (Component c : parent.getComponents()) {
					c.setBounds(ins.left, ins.top, 
							parent.getWidth() - ins.left - ins.right, 
							parent.getHeight() - ins.top - ins.bottom);
				}
			}
		}
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return new Dimension(100,50);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return minimumLayoutSize(parent);
	}

	@Override
	public void removeLayoutComponent(Component comp) {
	}
}
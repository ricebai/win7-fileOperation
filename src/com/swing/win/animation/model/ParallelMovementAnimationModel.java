package com.swing.win.animation.model;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;


public class ParallelMovementAnimationModel extends AnimationModel{

	@Override
	public int runCount() {
		return 50;
	}

	@Override
	public long sleepTime() {
		return 10;
	}

	@Override
	public String getName() {
		return "ParallelMovement";
	}

	@Override
	public void draw(Graphics2D g, Rectangle bounds, int runCount,
			ImageObserver observer) {
		
		setRenderingHint(g);

		Point p = new Point();
		p.x = bounds.x;
		p.y = bounds.y;
		
		// first
		
		if(isForward()){
			p.x = bounds.x - (bounds.width / runCount() * runCount);
		}else{
			p.x = bounds.x + (bounds.width / runCount() * runCount);
		}
		
		g.setComposite(getComposite(runCount() - runCount));
		
		g.drawImage(getCurrentImage(), p.x, p.y, observer);
		
		// next
		
		if(isForward()){
			p.x = (bounds.x + bounds.width) - (bounds.width / runCount() * runCount);
		}else{
			p.x = (bounds.x - bounds.width) + (bounds.width / runCount() * runCount);
		}
		
		g.setComposite(getComposite(runCount));
		
		g.drawImage(getNextImage(), p.x, p.y, observer);
	}


}

package com.swing.win.animation;

import com.swing.win.Threading;
import com.swing.win.animation.model.AnimationModel;

public class JAnimationThread {

	Threading th;
	AnimationModel model;
	JAnimationPreview preview;
	boolean forward;
	boolean start;
	int count;
	
	public JAnimationThread(JAnimationPreview p) {
		preview = p;
		
		th = new Threading() {
			
			@Override
			public void run() {
				if(forward){
					if(count < 0){
						count = 0;
					}else if(count >= model.runCount()){
						count = 0;
					}
				}else{
					if(count > model.runCount()){
						count = model.runCount();
					}else if(count <= 0){
						count = model.runCount();
					}
				}
				
				super.run();
				start = false;
				preview.setVisible(false);
			}
			
			@Override
			public boolean runner() throws InterruptedException {
				
				preview.repaint();
				
				sleep(model.sleepTime());
				System.out.println(count +", " + forward +", " +(forward ? ++count <= model.runCount() : --count >= 0) +"," + model.sleepTime());
				return forward ? ++count <= model.runCount() : --count >= 0;
			}
		};
	}

	public void start(AnimationModel model, boolean forward) {
		this.model = model;
		this.forward = forward;
		th.start();
		start = true;
	}
	
	public int runCount() {
		return count;
	}
	
	public boolean isStart(){
		return start;
	}
	
}

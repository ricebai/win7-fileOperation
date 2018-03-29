package com.swing.win;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

import com.swing.win.layout.BasicLayout;

public class JLinkButton extends JComponent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int TYPE_OPTIONS = 0;	// options
	public static final int TYPE_LT = 1;	// <
	public static final int TYPE_GT = 2;	// >

	private List<ActionListener> ls;
	
	private String text;
	private Insets insets;
	
	private Integer type;
	
	private int plan = -1;
	
	public JLinkButton() {
		this(null);
	}
	
	public JLinkButton(String text) {
		setText(text);
		ls = new ArrayList<ActionListener>();
		setUI(new JLinkButtonUI());
		setLayout(new JLinkButtonLayout());
	}
	
	public JLinkButton(int type) {
		this();
		setType(type);
	}
	
	public void setType(Integer type) {
		this.type = type;
	}
	
	public Integer getType() {
		return type;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public void setInsets(Insets insets) {
		this.insets = insets;
	}
	
	@Override
	public Insets getInsets() {
		return insets;
	}
	
	public void addActionListener(ActionListener action){
		ls.add(action);
	}
	
	protected void fireAction(){
		if(ls != null && ls.size() > 0){
			Iterator<ActionListener> iter = ls.iterator();
			ActionEvent event = new ActionEvent(JLinkButton.this, ActionEvent.ACTION_FIRST, null);
			while(iter.hasNext()){
				iter.next().actionPerformed(event);
			}
		}
	}

	public void getDimension(Dimension d, boolean insFlag) {
		if(getText() != null){
			Graphics g = getGraphics();
			java.awt.FontMetrics fs = g.getFontMetrics(getFont());
			d.width = fs.stringWidth(getText());
			d.height = fs.getHeight();
		}
		
		if(insFlag){
			Insets ins = getInsets();
			if(ins == null){
				ins = new Insets(0, 0, 0, 0);
			}
			d.width += insets.left+insets.right;
			d.height += insets.top + insets.bottom;
		}
	}

	public void setPlan(int plan) {
		this.plan = plan;
	}
	
	public int getPlan() {
		return plan;
	}
}

class JLinkButtonLayout extends BasicLayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		Dimension d = new Dimension();
		
		JLinkButton but = (JLinkButton) parent;
		but.getDimension(d, true);
		return d;
	}
	
	
}

class JLinkButtonUI extends ComponentUI{
	
	protected JLinkButton button; 
	
	private Color Ent = new Color(162, 162, 162);
	private Color Exi = new Color(204, 41, 90);
	
	private Color Pre = new Color(116, 116, 116);
	
	private Color planC = new Color(66, 66, 64, 100);
	
	private Color background = new Color(12, 12, 12, 200) ;
	
	private boolean ent, pre;
	
	@Override
	public void installUI(JComponent c) {
		button = (JLinkButton) c;
		button.setInsets(new Insets(5, 10, 5, 10));
		button.setForeground(Exi);
		button.setFont(new Font("微软雅黑", 0, 15));
		button.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				pre = true;
				button.setForeground(Pre);
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				button.setForeground(ent ? Ent : Exi);
				if(pre && ent){
					button.setForeground(Exi);
					button.fireAction();
				}
				pre = false;
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				ent =true;
				button.setForeground(Ent);
				button.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				ent = false;
				button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				button.setForeground(Exi);
				button.repaint();
			}
		});
	}
	
	@Override
	public void paint(Graphics g, JComponent c) {
		
		if(button.getPlan() >= 1){
			g.setColor(background);
			g.fillRect(0, 0, c.getWidth()-1, c.getHeight()-1);
		}
		
		if(button.getPlan() >= 0 && ent){
			g.setColor(planC);
			g.fillRect(0, 0, c.getWidth()-1 ,c.getHeight()-1);
		}
		
		
		if(button.getType() != null){
			paintType(g, c);
		}else 
		
		if(button.getText() != null){
			
			Insets insets = button.getInsets();
			if(insets == null){
				insets = new Insets(0, 0, 0, 0);
			}
			
			((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING , RenderingHints.VALUE_ANTIALIAS_ON);
			
			Dimension d = new Dimension();
			button.getDimension(d, false);
			
			java.awt.Point p = new java.awt.Point();
			
			if(d.width >= c.getWidth()){
				p.x = insets.left;
			}else{
				p.x = c.getWidth()/2 - d.width/2;
			}
			p.y = c.getHeight()/2 + d.height/2;
			
			g.setColor(button.getForeground());;
			g.drawString(button.getText(), p.x, p.y);
		}
	}

	private void paintType(Graphics g, JComponent c) {
		
		if(button.getType() == JLinkButton.TYPE_OPTIONS){
			
		}else if(button.getType() == JLinkButton.TYPE_GT){
			paintGtorLt(true, c.getSize(), g);
		}else if(button.getType() == JLinkButton.TYPE_LT){
			paintGtorLt(false, c.getSize(), g);
		}
		
	}
	
	void paintGtorLt(boolean gt, Dimension size, Graphics g){
		
		Rectangle rec = new Rectangle();
		rec.x = size.width / 2 - 4;
		rec.y = size.height / 2 - 7;
		rec.width = 8;
		rec.height = 14;
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2));
		g2.setColor(new Color(130,130,130));
		if(gt){
			g2.drawLine(rec.x + 0, rec.y + 0, rec.x + 8, rec.y + 7);
			g2.drawLine(rec.x+8,  rec.y+7,  rec.x+0, rec.y + 14);
		}else{
			g2.drawLine(rec.x+8, rec.y + 0, rec.x + 0, rec.y + 7);
			g2.drawLine(rec.x+0, rec.y + 7,rec.x + 8, rec.y + 14);
		}
		
	}
}

package com.swing.win;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.swing.win.tools.Assist;
import com.swing.win.tools.FileTool;
import com.swing.win.tools.droptarget.DropFileListener;
import com.swing.win.tools.droptarget.DropTargetTools;

@SuppressWarnings("serial")
public class JPictureViewPanel extends JComponent{

	private File view;
	private File[] files;
	private int index;
	private JButton text;
	private JLabel la;
	
	private List<String> suffixs;

	public JPictureViewPanel(String path) {
		this(path != null ? new File(path) :null);
	}
	

	public JPictureViewPanel(File view) {
		suffixs = new ArrayList<String>();
		suffixs.add(".png");
		suffixs.add(".gif");
		suffixs.add(".jpg");
		if (view != null) {
			if (!view.isFile()) {
				view = null;
			}
		}
		setLayout(new BorderLayout());
		add(getJMenuBar(), BorderLayout.NORTH);
		add(getComponent(), BorderLayout.CENTER);
		add(getBottom(), BorderLayout.SOUTH);
	}


	private JMenuBar getJMenuBar() {
		JMenuBar bar = new JMenuBar();
		JMenu menu = new JMenu("File");
		JMenuItem item = new JMenuItem("Open File");
		item.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setMultiSelectionEnabled(false);
				if(fc.showOpenDialog(JPictureViewPanel.this) == 0){
					refushView(fc.getSelectedFile());
				}
			}
		});
		menu.add(item);
		bar.add(menu);
		bar.setBorder(null);
		return bar;
	}

	private Component getBottom() {
		return new JComponent() {
			{
				setLayout(new BorderLayout());
				JButton left = new JButton("previous");
				JButton right = new JButton("next");
				left.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						switchView(false);
					}
				});
				
				right.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						switchView(true);
					}
				});
				add(left, BorderLayout.WEST);
				add(right, BorderLayout.EAST);
				add(text = new JButton());
				text.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(view != null && view.isFile()){
							try {
								Assist.openFile(null, view.getPath(), true);
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				});
			}
		};
	}

	private Component getComponent() {
		JPanel pane = new JPanel() {
			@Override
			public void paint(Graphics g) {
				int y = getParent().getHeight() / 2 - la.getHeight() / 2;
				if (y > 0) {
					la.setLocation(la.getX(), y);
				}
				super.paint(g);
			}
		};
		pane.setBackground(Color.black);
		pane.setLayout(new FlowLayout());
		pane.add(la = new JLabel());
		pane.setDropTarget(DropTargetTools.getDropTarget(pane, new DropFileListener() {
			@Override
			public void dropFile(List<File> fileList) {
				if(fileList.size() > 0)
					refushView(fileList.get(0));
			}
		}));
		return new JScrollPane(pane) {
			{
				setBorder(null);
			}
		};
	}
	
	private void switchView(boolean next){
		if(files != null && files.length > 1){
			File temp = null;
//			!".gif".equalsIgnoreCase())
			int temp_count = 0;
			while((!Assist.contains(suffixs, FileTool.suffix((temp = files[next?(index+1>=files.length? index=0:++index):
				(index -1 < 0 ? index =files.length-1:--index)]).getName())) && suffixs.size() > 0) && temp_count <= files.length){
				++temp_count;}
			if(temp_count <= files.length && !temp.getPath().equals(view.getPath()))
				refushView(temp);
			else{
				JOptionPane.showMessageDialog(this, "note search file!");
			}
		}
	}
	
	public String[] getSuffixs() {
		return suffixs.toArray(new String[suffixs.size()]);
	}
	
	public void clearSuffixs(){
		this.suffixs.clear();
	}
	
	public void setSuffixs(String suffix) {
		this.clearSuffixs();
		this.suffixs.add(suffix);
	}
	public void setSuffixs(List<String> suffixs) {
		this.suffixs = suffixs;
	}
	public void addSuffixs(String suffix) {
		this.suffixs.add(suffix);
	}
	
	public void refushView(String path) {
		refushView(path != null ? new File(path):null);
	}
	@SuppressWarnings("deprecation")
	public void refushView(File file) {
		view = file;
		try {
			if(view != null){
				la.setIcon(new ImageIcon(view.toURL()));
				files = view.getParentFile().listFiles();
				index = 0;
				if(files != null && files.length > 1)
				for(File f : files){
					if(f.toPath().equals(view.toPath())){
						break;
					}else{
						index++;
					}
				}
				text.setText(view.getPath());
			}else{
				la.setIcon(null);
				text.setText("");
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

package com.swing.win;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;

import com.swing.win.event.FileProcessor;
import com.swing.win.event.FileProcessorAdpter;
import com.swing.win.event.FileProcessorEvent;
import com.swing.win.event.FileProcessorSource;
import com.swing.win.tools.DateTools;
import com.swing.win.tools.FileTool;

@SuppressWarnings("serial")
public class JFileProcessor extends JDialog{

	private FileProcessor processor;
	
	private JProgressBar bar;
	private JButton close;
	private JLabel tb, tit;
	private JTextArea ta;
	
	private int space = 0;
	
	public JFileProcessor() {
		processor = new FileProcessor();
		processor.addFileListener(new FileProcessorAdpter() {
			
			@Override
			public void status(FileProcessorEvent e) {
				if(e.getID() == FileProcessorEvent.FINISH){
					flag = false;
					JFileProcessor.this.dispose();
				}else if(e.getID() == FileProcessorEvent.START){
					FileProcessorSource d = e.getData();
					String text = d.getCount()+" 个项目("+(FileTool.toCapacity(d.getLength(), 2).toUpperCase())+")";
					
					String opt = "复制";
					if(e.getID() == 1){
						opt = "剪切";
					}
					
					setTitle("正在"+opt+text);
					tit.setText("正在"+opt+text);
					
					int t = (d.getLength()+"").length()-9;
					if(t > 0){
						space = 1;
						for(int i=0;i<t ; i++){
							space *= 10;
						}
						t = (int)(d.getLength()/ space);
					}else{
						t = (int)(d.getLength());
					}
					
					bar.setModel(new DefaultBoundedRangeModel(0, 0, 0, t));
					ta.setText("从 "+e.getFile().getParentFile().getName()+" ("+e.getFile().getPath().substring(0, 1)+":) 到 "+d.getSave().getName()+" ("+d.getSave().getPath().substring(0, 1)+":)\n已发现 "+text);
				}else if(e.getID() == FileProcessorEvent.INTERRUPT){
					flag = false;
					JFileProcessor.this.dispose();
				}
			}
			
			@Override
			public void length(FileProcessorEvent e) {
				// TODO Auto-generated method stub
				FileProcessorSource d = e.getData();
				
				int t = 0;
				if(space > 1){
					for(int i=0;i<t ; i++){
						space *= 10;
					}
					t = (int)(d.getHandleLength()/ space);
				}else{
					t = (int)(e.getData().getHandleLength());
				}
				
				bar.setValue(t);
//				System.out.println(t+";val="+FileTool.toCapacity(e.getData().getHandleLength(), 2));
			}
			
			@Override
			public void check(FileProcessorEvent e) {
				ta.setText("从 "+e.getFile().getParentFile().getName()+" ("+e.getFile().getPath().substring(0, 1)+":) 到 "+e.getData().getSave().getName()+" ("+e.getData().getSave().getPath().substring(0, 1)+":)\n正在验证...");
			}
		});
		setAlwaysOnTop(true);
		setLayout(new BorderLayout());
		setTitle("正在验证...");
		getContentPane().setBackground(Color.white);
		
		
		add(new JLabel(){
			{
				try {
					setIcon(new ImageIcon(ImageIO.read(JFileProcessor.class.getResourceAsStream("bg.jpg"))));
				} catch (IOException e) {
					e.printStackTrace();
				}
				setPreferredSize(new Dimension(1,40));
				setLayout(new BorderLayout(25,25));
				add(tit = new JLabel("正在验证..."), BorderLayout.CENTER);
				tit.setFont(new Font("微软雅黑", 0, 17));
				tit.setBorder(BorderFactory.createEmptyBorder(10,25,10,25));
			}
		}, BorderLayout.NORTH);
		
		add(new JPanel(){
			{
				setBackground(Color.white);
				setLayout(new BorderLayout());
				add(ta = new JTextArea(){
					{
						setFont(new Font("微软雅黑", 0, 12));
//						setEnabled(false);
						setEditable(false);
					}
				});
				add(bar = new JProgressBar(), BorderLayout.SOUTH);
				setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
			}
		}, BorderLayout.CENTER);
		bar.setPreferredSize(new Dimension(1, 18));
		bar.setValue(100);
		
		JPanel south = new JPanel();
		setBackground(Color.white);
		south.setLayout(new BorderLayout());
		
		south.add(tb = new JLabel("详细信息"));
		try {
			tb.setIcon(new ImageIcon(ImageIO.read(JFileProcessor.class.getResourceAsStream("t.jpg"))));
		} catch (IOException e) {
			e.printStackTrace();
		}
		tb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					if("详细信息".equals(tb.getText())){
						tb.setText("简略信息");
						tb.setIcon(new ImageIcon(ImageIO.read(JFileProcessor.class.getResourceAsStream("s.jpg"))));
						JFileProcessor.this.setSize(405, 270);
					}else{
						tb.setText("详细信息");
						tb.setIcon(new ImageIcon(ImageIO.read(JFileProcessor.class.getResourceAsStream("t.jpg"))));
						JFileProcessor.this.setSize(405, 195);
					}
					ta.setText("");
				} catch (IOException s) {
					s.printStackTrace();
				}
			}
		});
		south.add(close = new JButton("取消"), BorderLayout.EAST);
		south.setPreferredSize(new Dimension(1, 45));
		south.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
		close.setPreferredSize(new Dimension(73, 21));
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				flag = false;
				processor.interrupt();
			}
		});
		add(south, BorderLayout.SOUTH);
		
		setSize(405, 195);
		setLocation(10, 10);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		
		th = new Threading() {
			
			@Override
			public boolean runner() throws InterruptedException {
				FileProcessorSource d = processor.getSource();
				tt= d.getPointLength();
				if(tt <= 0){
					tt = 1;
				}
				if(!"详细信息".equals(tb.getText())){
					StringBuffer buf = new StringBuffer("");
					buf.append("名称："+d.getFile().getName());
					buf.append("\n从："+d.getFile().getParentFile().getName()+"("+d.getFile().getPath().substring(0, 1)+":)");
					buf.append("\n到："+d.getSave().getName()+"("+d.getSave().getPath().substring(0, 1)+":)");
					buf.append("\n剩余时间："+(DateTools.toDateString((d.getLength()-d.getHandleLength())/tt)));
					buf.append("\n剩余项："+(d.getCount()-d.getFinishs())+"("+(FileTool.toCapacity(d.getLength()-d.getHandleLength(), 2).toUpperCase())+")");
					buf.append("\n速度："+(FileTool.toCapacity(tt, 2).toUpperCase())+"/秒");
					
					ta.setText(buf.toString());
				}else{
					String text = (d.getCount()-d.getFinishs())+" 个项目("+(FileTool.toCapacity(d.getLength()-d.getHandleLength(), 2).toUpperCase())+")";
					String opt = "复制";
					if(processor.getId() == 1){
						opt = "剪切";
					}
					setTitle("正在"+opt+text);
					tit.setText("正在 "+opt+text);
					ta.setText("从 "+d.getFile().getParentFile().getName()+" ("+d.getFile().getPath().substring(0, 1)+":) 到 "+d.getSave().getName()+" ("+d.getSave().getPath().substring(0, 1)+":)\n已发现 "+text);
				}
				sleep(1000);
				return flag;
			}
		};
	}

	public void showCut(String endDir, String... startPath){
		showCut(new File(endDir), toFiles(startPath));
	}
	
	public void showCut(File end, File... start){
		setVisible(true);
		processor.cut(end, start);
	}
	
	public void showDelete(String... del){
		showDelete(toFiles(del));
	}
	
	public void showDelete(File... del){
		setVisible(true);
		processor.delete(del);
	}

	public void showCopy(File dir, String...files) {
		showCopy(dir, toFiles(files));
	}
	
	boolean flag = true;
	long tt = 1;
	Threading th ;
	
	public void showCopy(File dir, File...files) {
		setVisible(true);
		processor.copy(dir, files);
		th.start();
	}
	
	private File[] toFiles(String... path){
		if(path == null || path.length < 1){
			return new File[0];
		}
		File[] files = new File[path.length];
		for(int i=0;i<files.length;i++){
			files[i] = new File(path[i]);
		}
		return files;
	}
	
}

package me.lichee.mlook;

import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import net.spy.memcached.MemcachedClient;
import me.lichee.mlook.core.MTool;

public class MFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private JTextField ipTextField;
	private JButton refreshBtn;
	
	private String[] keys;
	
	private JList<String> keyList;
	
	private MTool mtool = null;
	
	public MFrame(){
		
		this.setTitle("MTool-memcached");
		this.setSize(300, 350);
        Container contentPane=this.getContentPane();
        GridBagLayout gl = new GridBagLayout();
        contentPane.setLayout(gl);
        
		ipTextField = new JTextField();
		ipTextField.setFont(new Font("微软雅黑", 0, 20));
		ipTextField.setText("192.168.0.161");
		refreshBtn = new JButton();
		refreshBtn.setText("刷新");
		refreshBtn.addActionListener(refreshKeys());
		keyList = new JList<String>();
		keyList.addKeyListener(deleteKeys());
		JScrollPane listScroll = new JScrollPane(keyList);
		contentPane.add(ipTextField);
		contentPane.add(refreshBtn);
		contentPane.add(listScroll);
		//gridbaglayout
		GridBagConstraints s= new GridBagConstraints();//定义一个GridBagConstraints，
        //是用来控制添加进的组件的显示位置
        s.fill = GridBagConstraints.BOTH;
        s.gridwidth=0;
        s.weightx = 1;
        s.weighty=0;
        gl.setConstraints(ipTextField, s);
        s.weightx = 1;
        s.gridwidth=0;
        gl.setConstraints(refreshBtn, s);
        s.gridwidth=0;
        s.weightx = 1;
        s.weighty = 1;
        gl.setConstraints(listScroll, s);
	}
	
	/**
	 * 删除memcache缓存
	 * @return
	 */
	private KeyListener deleteKeys() {
		return new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				System.out.println("111");
				if(e.getKeyCode()!=KeyEvent.VK_DELETE) return;
				List<String> selected = keyList.getSelectedValuesList();
				if(selected.isEmpty()){
					return;
				}
				String ip = ipTextField.getText();
				int port = 11211;
				if(ip.contains(":")){
					String[] info = ip.split(":");
					ip = info[0];
					port = Integer.valueOf(info[1]);
				}
				try {
					mtool = new MTool(ip, port);
					for(String k : selected){
						mtool.getClient().delete(k);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					return;
				}finally{
					keys = MTool.allkeys(ip, port).split("\n");
					makeLists();
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		};
	}

	private ActionListener refreshKeys() {
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String ip = ipTextField.getText();
				int port = 11211;
				if(ip.contains(":")){
					String[] info = ip.split(":");
					ip = info[0];
					port = Integer.valueOf(info[1]);
				}
				keys = MTool.allkeys(ip, port).split("\n");
				makeLists();
			}
		};
	}
	
	private void makeLists(){
		keyList.setListData(keys);
	}
	
}

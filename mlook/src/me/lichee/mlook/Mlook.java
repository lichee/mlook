package me.lichee.mlook;

import java.net.SocketAddress;
import java.util.Map;

import me.lichee.mlook.core.MTool;

public class Mlook {
	
	public static void main(String[] args) throws Exception{
		/*MTool mt = new MTool("192.168.0.161",11211);*/
		/*String[] keys = MTool.allkeys("192.168.0.161",11211).split("\n");
		for(String key : keys){
			System.out.println(key);
		}*/
		MFrame mf = new MFrame();
		mf.setVisible(true);
	}
	
}

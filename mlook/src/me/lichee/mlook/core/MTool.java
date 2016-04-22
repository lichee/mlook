package me.lichee.mlook.core;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

import net.spy.memcached.MemcachedClient;

public class MTool {
	
	private MemcachedClient client;
	
	public MTool(String host, int port) throws Exception{
		this.client = new MemcachedClient(new InetSocketAddress(host, port));
	}

	public MemcachedClient getClient() {
		return client;
	}

	public void setClient(MemcachedClient client) {
		this.client = client;
	}
	

    public static String allkeys(String host, int port){
        StringBuffer r = new StringBuffer();
        try {
            Socket socket = new Socket(host, port);
            PrintWriter os = new PrintWriter(socket.getOutputStream());
            BufferedReader is = new BufferedReader(new InputStreamReader( socket.getInputStream()));
            os.println("stats items");
            os.flush();
            String l ;
            while (!(l = is.readLine()).equals("END")) {
                r.append(l).append("\n");
            }
            String rr = r.toString();
            Set<String> ids = new HashSet<String>();
            if(rr.length() > 0){
                r = new StringBuffer();//items 
                rr.replace("STAT items", "");
                for(String s : rr.split("\n")){
                    ids.add(s.split(":")[1]);
                }
                if (ids.size() > 0){
                    r = new StringBuffer();//
                    for(String s : ids){
                        os.println("stats cachedump "+ s +" 0");
                        os.flush();
                        while (!(l = is.readLine()).equals("END")) {
                            r.append(l.split(" ")[1]).append("\n");
                        }
                    }
                }
            }
             
            os.close();
            is.close();
            socket.close();
        } catch (Exception e) {
            System.out.println("Error" + e);
        }
        return r.toString();
    }
    
    public static String telnet(String host, int port, String cmd){
        StringBuffer r = new StringBuffer();
        try {
            Socket socket = new Socket(host, port);
            PrintWriter os = new PrintWriter(socket.getOutputStream());
            BufferedReader is = new BufferedReader(new InputStreamReader( socket.getInputStream()));
            os.println(cmd);
            os.flush();
            String l ;
            while (!(l = is.readLine()).equals("END")) {
                r.append(l).append("\n");
            }
            os.close();
            is.close();
            socket.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return r.toString();
    }
	
}

package iitbdc;

import java.io.IOException;
public class DCBotChild extends Thread {
	String hub_ip;
	int listen_port;
	int udp_listen_port;
	
	public DCBotChild(String ip, int lp, int ulp) throws IOException{
		hub_ip=ip;
		listen_port = lp;
		udp_listen_port = ulp;
	}
	
	public void run() {
		try {
			new DCBot(hub_ip, listen_port, udp_listen_port);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	
	public static void main(String[] args) throws IOException{
		new DCBotChild("10.129.141.120",9007,10007).start();
		new DCBotChild("10.105.18.1",9010,10010).start();
		new DCBotChild("10.6.171.18",9009,10009).start();
	}
	
}






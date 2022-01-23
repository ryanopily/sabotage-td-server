package ml.zer0dasho.std;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import ml.zer0dasho.std.net.server.STDServer;

public class STD {
	
	public static void main(String...args) throws Exception {
		STDServer server = new STDServer(new InetSocketAddress(InetAddress.getByName("192.168.3.124"), 25565));
		server.run();
	}
	
}

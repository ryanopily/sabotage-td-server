package ml.zer0dasho.std;

import ml.zer0dasho.std.net.server.STDServer;

public class STD {
	
	public static void main(String...args) throws Exception {
		STDServer server = new STDServer();
		server.run();
	}
	
}

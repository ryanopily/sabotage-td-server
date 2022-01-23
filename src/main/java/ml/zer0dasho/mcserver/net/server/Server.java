package ml.zer0dasho.mcserver.net.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public abstract class Server implements Runnable {
	
	public final Selector selector;
	public final ServerSocketChannel socket;
	public final InetSocketAddress address;

	public long startTime, endTime;
	public boolean running;
	
	public Server(InetSocketAddress address) throws IOException {		
		this.address = address;
		this.selector = Selector.open();
		this.socket = ServerSocketChannel.open();
		
		// Configure and register socket with selector
		socket.configureBlocking(false);
		socket.bind(address);
		socket.register(selector, socket.validOps());
	}
	
	@Override
	public void run() {
		
		// Initialize server properties
		this.running = true;
		this.startTime = System.nanoTime();
		this.initialize();
		
		while(running) {
			try {			
				
				this.serverLoop();
				
				if(selector.selectNow() <= 0)
					continue;

				Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
				while(keys.hasNext()) {
					SelectionKey key = keys.next();
					SocketChannel client = null;

					if(key.isAcceptable()) {
						if((client = this.socket.accept()) != null)
							accept(client);
					}
					
					if(key.isReadable() && (client = (SocketChannel) key.channel()) != null)
						read(client);
							
					if(key.isWritable() && (client = (SocketChannel) key.channel()) != null)
						write(client);

					keys.remove();
				}
				
			} catch(Exception ex) {
				running = false;
				ex.printStackTrace();
			}
		}
		
		endTime = System.nanoTime();
	}
	
	protected abstract void initialize();
	protected abstract void serverLoop();
	protected abstract void accept(SocketChannel client) throws IOException;
	protected abstract void read(SocketChannel client) throws IOException;
	protected abstract void write(SocketChannel client) throws IOException;
}
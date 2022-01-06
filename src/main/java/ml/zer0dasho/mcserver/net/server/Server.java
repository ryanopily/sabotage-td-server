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
	
	public boolean running;
	
	public Server(InetSocketAddress address) throws IOException {		
		this.address = address;
		this.selector = Selector.open();
		this.socket = ServerSocketChannel.open();
		
		socket.configureBlocking(false);
		socket.bind(address);
		socket.register(selector, socket.validOps());
	}
	
	@Override
	public void run() {
		running = true;
		
		while(running) {
			try {
				
				if(selector.selectNow() <= 0)
					continue;

				Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
				while(keys.hasNext()) {
					SelectionKey key = keys.next();

					if(key.isAcceptable())
						accept(this.socket.accept());
					
					if(key.isReadable())
						read((SocketChannel)key.channel());
					
					if(key.isWritable())
						write((SocketChannel)key.channel());

					keys.remove();
				}
				
			} catch(Exception ex) {
				running = false;
				ex.printStackTrace();
			}
		}
	}
	
	protected abstract void accept(SocketChannel client) throws IOException;
	protected abstract void read(SocketChannel client) throws IOException;
	protected abstract void write(SocketChannel client) throws IOException;
}
package ml.zer0dasho.mcserver.net.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ml.zer0dasho.mcserver.net.MinecraftConnection;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket;

public abstract class MinecraftServer extends Server {
	
	public long ticks;
	public Map<SocketChannel, MinecraftConnection> connections;
	
	private long lastTime;
	private long delta;

	public MinecraftServer() throws IOException {
		this(new InetSocketAddress(InetAddress.getLocalHost(), DEFAULT_PORT));
	}
	
	
	public MinecraftServer(InetSocketAddress address) throws IOException  {
		super(address);
		
		this.connections = new HashMap<>();
	}
	
	@Override
	protected void accept(SocketChannel client) throws IOException {
		client.configureBlocking(false);
		client.register(this.selector, client.validOps());
	}
	
	@Override
	protected void read(SocketChannel client) throws IOException {
		MinecraftConnection connection = this.connections.get(client);

		if(!readPacket(client, connection))
			return;

		Iterator<MinecraftPacket> packets = connection.receivePackets();
		while(packets.hasNext()) {
			MinecraftPacket packet = packets.next();
			
			System.out.println(packet);
			
			packets.remove();
		}
	}
	
	private boolean readPacket(SocketChannel client, MinecraftConnection connection) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		client.read(buffer);
		buffer.flip();
		
		return buffer.limit() > 0 && connection.receivePacket(buffer);
	}

	@Override
	protected void write(SocketChannel client) throws IOException {
		if(client == null) return;
		MinecraftConnection connection = this.connections.get(client);
		connection.sendPackets(client);
	}
	
	@Override
	protected void init() {
		this.lastTime = this.startTime;
	}
	
	@Override
	protected void loop() {
		long currentTime = System.nanoTime();
		this.delta += (currentTime - lastTime);
		
		if(this.delta >= TICK_LENGTH) {
			this.ticks++;
			this.delta = 0;
		}
		
		this.lastTime = currentTime;
	}
	
	public static final int DEFAULT_PORT = 25565;
	public static final long TICK_LENGTH = 1_000_000_000 / 20;
}

package ml.zer0dasho.mcserver.net.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;

import ml.zer0dasho.mcserver.net.MinecraftConnection;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket;

/**
 * MinecraftServer outlines the bare-minimum requirements for a server. 
 * No extra bloat included.
 * 
 * @author ryans
 *
 */
public abstract class MinecraftServer extends Server {
	
	// Server-life in ticks
	public long ticks;
	private long lastTime, delta;
	
	// Support for scheduling tasks is a must.
	public final MinecraftScheduler scheduler;
	public final Map<SocketChannel, MinecraftConnection> connections;

	public MinecraftServer(MinecraftScheduler scheduler) throws IOException {
		this(scheduler, new InetSocketAddress(InetAddress.getLocalHost(), DEFAULT_PORT));
	}
	
	public MinecraftServer( MinecraftScheduler scheduler, InetSocketAddress address) throws IOException  {
		super(address);
		
		this.scheduler = scheduler;
		this.connections = new HashMap<>();
	}
	
	// PUBLIC API
	
	public void scheduleLater(int waitTicks, Supplier<Boolean> task) {
		scheduler.scheduleLater(waitTicks, task);
	}
	
	public void scheduleCycle(int cycleTicks, Supplier<Boolean> task) {
		scheduler.scheduleCycle(cycleTicks, task);
	}
	
	// PROTECTED API
	
	@Override
	protected void initialize() {
		this.lastTime = super.startTime;
	}
	
	@Override
	protected void serverLoop() {
		long currentTime = System.nanoTime();
		this.delta += (currentTime - this.lastTime);
		
		if(this.delta >= TICK_LENGTH) {
			this.ticks++;
			this.delta = 0;
			
			this.scheduler.run();
		}

		this.lastTime = currentTime;
	}
	
	@Override
	protected void accept(SocketChannel client) throws IOException {
		client.configureBlocking(false);
		client.register(this.selector, client.validOps());
		connections.put(client, registerConnection(client));
	}
	
	protected abstract MinecraftConnection registerConnection(SocketChannel client);
	
	@Override
	protected void read(SocketChannel client) throws IOException {
		MinecraftConnection connection = connections.get(client);
		
		if(connection == null || !readPacket(client, connection))
			return;

		Iterator<MinecraftPacket> packets = connection.receivePackets();
		while(packets.hasNext()) {
			packets.next();
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

	public static final int DEFAULT_PORT = 25565;
	public static final long TICK_LENGTH = 1_000_000_000 / 20;
}
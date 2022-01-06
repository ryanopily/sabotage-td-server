package ml.zer0dasho.mcserver.net.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ml.zer0dasho.mcserver.net.Connection;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacketManager;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket.ReadablePacket;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket.WriteablePacket;

public abstract class MinecraftServer extends Server {
	
	public MinecraftPacketManager packetManager;
	public final Map<SocketChannel, Connection> connections;
	
	public boolean running;

	public MinecraftServer(MinecraftPacketManager packetManager) throws IOException {
		this(new InetSocketAddress(InetAddress.getLocalHost(), DEFAULT_PORT), packetManager);
	}
	
	
	public MinecraftServer(InetSocketAddress address, MinecraftPacketManager packetManager) throws IOException  {
		super(address);
		
		this.connections = new HashMap<>();
		this.packetManager = packetManager;
	}
	
	@Override
	protected void accept(SocketChannel client) throws IOException {
		client.configureBlocking(false);
		client.register(this.selector, client.validOps());
		
		registerConnection(client);
	}
	
	protected abstract void registerConnection(SocketChannel client);
	
	@Override
	protected void read(SocketChannel client) throws IOException {
		Connection connection = this.connections.get(client);

		if(!readPacket(client, connection))
			return;

		Iterator<ByteBuffer> packets = connection.packetProcessor.received().iterator();
		while(packets.hasNext()) {
			ReadablePacket packet = packetManager.getPacket(packets.next(), connection.state);
			packet.apply(connection);
			
			System.out.println(packet);
			
			packets.remove();
		}
	}
	
	private boolean readPacket(SocketChannel client, Connection connection) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		client.read(buffer);
		buffer.flip();
		
		return buffer.limit() > 0 && connection.packetProcessor.readIn(buffer);
	}
	
	@Override
	protected void write(SocketChannel client) throws IOException {
		Connection conn = this.connections.get(client);
		
		Iterator<WriteablePacket> packets = conn.out.iterator();
		while(packets.hasNext()) {
			WriteablePacket packet = packets.next();
			packet.apply(conn);
			
			client.write(packet.encode());
			
			packets.remove();
		}
	}
	
	public static final int DEFAULT_PORT = 25565;
}

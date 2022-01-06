package ml.zer0dasho.std.net.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import ml.zer0dasho.mcserver.net.Connection;
import ml.zer0dasho.mcserver.net.packets.login.out.LoginSuccess;
import ml.zer0dasho.mcserver.net.packets.play.out.JoinGame;
import ml.zer0dasho.mcserver.net.packets.play.out.KeepAlive;
import ml.zer0dasho.mcserver.net.packets.play.out.PlayerPositionAndLook;
import ml.zer0dasho.mcserver.net.packets.play.out.SpawnPosition;
import ml.zer0dasho.mcserver.net.server.MinecraftServer;
import ml.zer0dasho.std.net.packets.STDPacketManager;
import ml.zer0dasho.std.net.packets.STDPacketProcessor;

public class STDServer extends MinecraftServer {
	
	public STDServer() throws IOException {
		super(new STDPacketManager());
	}
	
	public STDServer(InetSocketAddress address) throws IOException {
		super(address, new STDPacketManager());
	}

	@Override
	protected void registerConnection(SocketChannel client) {
		Connection connection = new Connection(new STDPacketProcessor());
		this.connections.put(client, connection);
		
		connection.out.add(new LoginSuccess());
		connection.out.add(new JoinGame());
		connection.out.add(new SpawnPosition());
		connection.out.add(new PlayerPositionAndLook());
		connection.out.add(new KeepAlive());
	}
}

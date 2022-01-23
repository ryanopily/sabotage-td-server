package ml.zer0dasho.std.net.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import ml.zer0dasho.mcserver.net.MinecraftConnection;
import ml.zer0dasho.mcserver.net.packets.login.out.LoginSuccess;
import ml.zer0dasho.mcserver.net.packets.play.out.ChunkData;
import ml.zer0dasho.mcserver.net.packets.play.out.ChunkData.ChunkSection;
import ml.zer0dasho.mcserver.net.packets.play.out.JoinGame;
import ml.zer0dasho.mcserver.net.packets.play.out.KeepAlive;
import ml.zer0dasho.mcserver.net.packets.play.out.PlayerPositionAndLook;
import ml.zer0dasho.mcserver.net.server.MinecraftServer;
import ml.zer0dasho.std.net.STDConnection;

public class STDServer extends MinecraftServer {
	
	public STDScheduler scheduler = new STDScheduler();
	
	public STDServer() throws IOException {
		this(new InetSocketAddress(InetAddress.getLocalHost(), DEFAULT_PORT));
	}
	
	public STDServer(InetSocketAddress address) throws IOException {
		super(new STDScheduler(), address);

		
		this.scheduleCycle(300, () -> {
			this.connections.values().forEach(conn -> {
				int keepAliveId = ThreadLocalRandom.current().nextInt();
				KeepAlive test = new KeepAlive(keepAliveId);
				conn.sendPackets(test);
			});
			
			return false;
		});
	}

	@Override
	protected MinecraftConnection registerConnection(SocketChannel client) {
		MinecraftConnection connection = new STDConnection();
		this.connections.put(client, connection);

		LoginSuccess one = new LoginSuccess(UUID.randomUUID().toString(), "zer0dashoml");
		JoinGame two = new JoinGame("flat", 1, false, (byte)1, (byte)0, (byte)0, (byte)1);
		PlayerPositionAndLook three = new PlayerPositionAndLook(0, 20, 0, 0, 0, false);
		
		connection.sendPackets(one, two, three);
		
		this.scheduleLater(20, () -> {
			for(int x = -3; x < 3; x++) {
				for(int z = -3; z < 3; z++) {
					ChunkSection section = new ChunkSection();
					connection.sendPackets(new ChunkData(x, z, new ChunkSection[] {section}));
				}
			}
			System.out.println("Sent chunks");
			return true;
		});
		
		return connection;
	}
}
package ml.zer0dasho.std.net.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.UUID;

import ml.zer0dasho.mcserver.net.MinecraftConnection;
import ml.zer0dasho.mcserver.net.packets.login.out.LoginSuccess;
import ml.zer0dasho.mcserver.net.packets.play.out.JoinGame;
import ml.zer0dasho.mcserver.net.packets.play.out.PlayerPositionAndLook;
import ml.zer0dasho.mcserver.net.server.MinecraftServer;
import ml.zer0dasho.std.net.STDConnection;

public class STDServer extends MinecraftServer {
	
	public STDServer() throws IOException {
		super();
	}
	
	public STDServer(InetSocketAddress address) throws IOException {
		super(address);
	}
	
	@Override
	protected void accept(SocketChannel client) throws IOException {
		super.accept(client);
		
		MinecraftConnection connection = new STDConnection();
		this.connections.put(client, connection);

		LoginSuccess one = new LoginSuccess();
			one.UUID = UUID.randomUUID().toString();
			one.username = "zer0dashoml";
			
		JoinGame two = new JoinGame();
			two.entity_id = 1;
			two.gamemode = 1;
			two.dimension = -1;
			two.difficulty = 0;
			two.max_players = 1;
			two.level_type = "flat";
			two.reduced_debug_info = false;
			
		PlayerPositionAndLook three = new PlayerPositionAndLook();
			three.x = 0;
			three.y = 50;
			three.z = 0;
			three.yaw = 0;
			three.pitch = 0;
			three.on_ground = true;
		
		connection.sendPackets(one, two, three);
	}
}

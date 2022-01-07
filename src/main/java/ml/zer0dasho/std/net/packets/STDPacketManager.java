package ml.zer0dasho.std.net.packets;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import ml.zer0dasho.mcserver.net.MinecraftConnection;
import ml.zer0dasho.mcserver.net.MinecraftConnection.ConnectionState;
import ml.zer0dasho.mcserver.net.VarInt;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket;
import ml.zer0dasho.mcserver.net.packets.UnknownPacket;
import ml.zer0dasho.mcserver.net.packets.handshaking.in.Handshake;
import ml.zer0dasho.mcserver.net.packets.login.in.LoginStart;

public class STDPacketManager {

	public static final Map<ConnectionState, Map<Integer, Function<ByteBuffer, MinecraftPacket>>> PACKET_REGISTRY = new HashMap<>() {
		
		private static final long serialVersionUID = 1L;
		
		{
			Map<Integer, Function<ByteBuffer, MinecraftPacket>> HANDSHAKING = new HashMap<>();
			HANDSHAKING.put(0, Handshake::new);
			
			Map<Integer, Function<ByteBuffer, MinecraftPacket>> LOGIN = new HashMap<>();
			LOGIN.put(0, LoginStart::new);
			
			this.put(ConnectionState.HANDSHAKING, HANDSHAKING);
			this.put(ConnectionState.LOGIN, LOGIN);
		}
	};
	
	public static MinecraftPacket getPacket(ByteBuffer packetData, MinecraftConnection connection) {
		Map<Integer, Function<ByteBuffer, MinecraftPacket>> map = PACKET_REGISTRY.get(connection.state);
		
		int type[] = new int[2];
		VarInt.getVarInt(packetData.array(), 0, type);

		if(map == null || !map.containsKey(type[1]))
			return new UnknownPacket(packetData);

		MinecraftPacket result = map.get(type[1]).apply(packetData);
		
		if(result instanceof Handshake) {
			connection.state = ConnectionState.LOGIN;
		}
		
		else if(result instanceof LoginStart) {
			connection.state = ConnectionState.PLAY;
		}
		
		return result;
	}

}
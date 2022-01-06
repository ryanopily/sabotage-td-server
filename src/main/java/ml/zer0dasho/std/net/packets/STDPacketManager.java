package ml.zer0dasho.std.net.packets;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import ml.zer0dasho.mcserver.net.VarInt;
import ml.zer0dasho.mcserver.net.Connection.ConnectionState;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacketManager;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket.ReadablePacket;
import ml.zer0dasho.mcserver.net.packets.handshaking.in.Handshake;
import ml.zer0dasho.mcserver.net.packets.login.in.LoginStart;

public class STDPacketManager implements MinecraftPacketManager {

	public static final Map<ConnectionState, Map<Integer, Function<ByteBuffer, ReadablePacket>>> PACKET_REGISTRY = new HashMap<>() {
		
		private static final long serialVersionUID = 1L;
		
		{
			Map<Integer, Function<ByteBuffer, ReadablePacket>> HANDSHAKING = new HashMap<>();
			HANDSHAKING.put(0, Handshake::new);
			
			Map<Integer, Function<ByteBuffer, ReadablePacket>> LOGIN = new HashMap<>();
			LOGIN.put(0, LoginStart::new);
			
			this.put(ConnectionState.HANDSHAKING, HANDSHAKING);
			this.put(ConnectionState.LOGIN, LOGIN);
		}
	};
	
	public ReadablePacket getPacket(ByteBuffer packetData, ConnectionState connectionState) {
		Map<Integer, Function<ByteBuffer, ReadablePacket>> map = PACKET_REGISTRY.get(connectionState);
		
		int type[] = new int[2];
		VarInt.getVarInt(packetData.array(), 0, type);

		if(map == null || !map.containsKey(type[1]))
			return new ReadablePacket(packetData);

		return map.get(type[1]).apply(packetData);
	}

}
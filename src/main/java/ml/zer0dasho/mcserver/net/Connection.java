package ml.zer0dasho.mcserver.net;

import java.util.ArrayList;
import java.util.List;

import ml.zer0dasho.mcserver.net.packets.MinecraftPacketProcessor;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket.WriteablePacket;

public class Connection {

	public ConnectionState state;
	public MinecraftPacketProcessor packetProcessor;
	public List<WriteablePacket> out = new ArrayList<>();
	
	public Connection(MinecraftPacketProcessor packetProcessor) {
		this.packetProcessor = packetProcessor;
		this.state = ConnectionState.HANDSHAKING;
	}
	
	public static enum ConnectionState {
		
		HANDSHAKING, STATUS, LOGIN, PLAY;

		public static ConnectionState of(int value) {
			switch(value) {
				case 0:
					return HANDSHAKING;
				case 1:
					return STATUS;
				case 2: 
					return LOGIN;
				default:
					return PLAY;
			}
		}
	}
}

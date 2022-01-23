package ml.zer0dasho.mcserver.net;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import ml.zer0dasho.mcserver.net.packets.MinecraftPacket;

public abstract class MinecraftConnection {

	public ConnectionState state;
	
	public MinecraftConnection() {
		this.state = ConnectionState.HANDSHAKING;
	}
	
	public abstract Iterator<MinecraftPacket> receivePackets();
	public abstract boolean receivePacket(ByteBuffer packetData);
	
	public abstract void sendPackets(SocketChannel client);
	public abstract void sendPackets(MinecraftPacket...packets);
	
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

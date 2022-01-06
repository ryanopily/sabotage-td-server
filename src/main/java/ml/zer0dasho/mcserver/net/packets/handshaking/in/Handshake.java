package ml.zer0dasho.mcserver.net.packets.handshaking.in;

import java.nio.ByteBuffer;

import ml.zer0dasho.mcserver.net.Connection;
import ml.zer0dasho.mcserver.net.Connection.ConnectionState;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket.ReadablePacket;

public class Handshake extends ReadablePacket {
	
	public final int version;
	public final String address;
	public final short port;
	public final int state;
	
	public Handshake(ByteBuffer data) {
		super(data);

		this.version = this.readVarInt();
		this.address = this.readUTF8();
		this.port = this.data.getShort();
		this.state = this.readVarInt();
	}
	
	@Override
	public void apply(Connection connection) {
		connection.state = ConnectionState.of(this.state);
	}
	
	@Override
	public String toString() {
		return 
				"Handshake:\n"
				+ super.toString() 
				+ String.format(
						"version: %d\n"
							+ "address: %s\n"
							+ "port: %d\n"
							+ "state: %d\n", 
						version,
						address,
						port,
						state
				);
	}
}

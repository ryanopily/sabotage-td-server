package ml.zer0dasho.mcserver.net.packets.handshaking.in;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import ml.zer0dasho.mcserver.net.NetUtils;
import ml.zer0dasho.mcserver.net.VarInt;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket;

public class Handshake extends MinecraftPacket {
	
	public final int ID = 0x00;
	public int protocol_version;
	public String server_address;
	public short server_port;
	public int next_state;
	
	public Handshake(ByteBuffer packetData) {
		try {
			this.decode(packetData);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
						protocol_version,
						server_address,
						server_port,
						next_state
				);
	}

	@Override
	public void decode(ByteBuffer buffer) throws IOException {
		VarInt.getVarInt(buffer); // length
		VarInt.getVarInt(buffer); // ID
		this.protocol_version = VarInt.getVarInt(buffer);
		this.server_address = NetUtils.readUTF8(buffer);
		this.server_port = buffer.getShort();
		this.next_state = VarInt.getVarInt(buffer);
		
	}
	@Override
	public ByteBuffer encode() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		
		VarInt.putVarInt(protocol_version, result);
		result.write(NetUtils.writeUTF8(server_address));
		result.write(ByteBuffer.allocate(Short.BYTES).putShort(server_port).get());
		VarInt.putVarInt(next_state, result);
		
		VarInt.putVarInt(ID, bos);
		VarInt.putVarInt(result.size(), bos);
		result.writeTo(bos);

		return ByteBuffer.wrap(bos.toByteArray());
	}
}

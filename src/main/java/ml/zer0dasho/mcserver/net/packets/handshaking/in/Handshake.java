package ml.zer0dasho.mcserver.net.packets.handshaking.in;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import ml.zer0dasho.mcserver.net.NetUtils;
import ml.zer0dasho.mcserver.net.VarInt;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket;

public class Handshake extends MinecraftPacket {
	
	public final int ID = 0x00;
	public String serverAddress;
	public short serverPort;
	public int protocolVersion, nextState;
	
	public Handshake(ByteBuffer packetData) {
		try {
			this.decode(packetData);
		} catch (IOException e) {
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
						protocolVersion,
						serverAddress,
						serverPort,
						nextState
				);
	}

	@Override
	public void decode(ByteBuffer buffer) throws IOException {
		VarInt.getVarInt(buffer); // length
		VarInt.getVarInt(buffer); // ID
		this.protocolVersion = VarInt.getVarInt(buffer);
		this.serverAddress = NetUtils.readUTF8(buffer);
		this.serverPort = buffer.getShort();
		this.nextState = VarInt.getVarInt(buffer);
		
	}
	@Override
	public ByteBuffer encode() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		
		VarInt.putVarInt(protocolVersion, result);
		result.write(NetUtils.writeUTF8s(serverAddress));
		result.write(ByteBuffer.allocate(Short.BYTES).putShort(serverPort).get());
		VarInt.putVarInt(nextState, result);
		
		VarInt.putVarInt(ID, bos);
		VarInt.putVarInt(result.size(), bos);
		result.writeTo(bos);

		return ByteBuffer.wrap(bos.toByteArray());
	}
}

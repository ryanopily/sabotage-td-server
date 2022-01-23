package ml.zer0dasho.mcserver.net.packets.login.out;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import ml.zer0dasho.mcserver.net.NetUtils;
import ml.zer0dasho.mcserver.net.VarInt;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket;

public class LoginSuccess extends MinecraftPacket {
	
	public final int ID = 0x02;
	public String UUID, username;
	
	public LoginSuccess(String UUID, String username) {
		this.UUID = UUID;
		this.username = username;
	}

	@Override
	public void decode(ByteBuffer buffer) throws IOException {
		VarInt.getVarInt(buffer);
		VarInt.getVarInt(buffer);
		this.UUID = NetUtils.readUTF8(buffer);
		this.username = NetUtils.readUTF8(buffer);
	}

	@Override
	public ByteBuffer encode() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		
		VarInt.putVarInt(ID, result);
		result.write(NetUtils.writeUTF8s(UUID));
		result.write(NetUtils.writeUTF8s(username));
		
		VarInt.putVarInt(result.size(), bos);
		result.writeTo(bos);
		
		return ByteBuffer.wrap(bos.toByteArray());
	}
	
	
}

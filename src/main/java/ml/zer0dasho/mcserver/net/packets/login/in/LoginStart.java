package ml.zer0dasho.mcserver.net.packets.login.in;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import ml.zer0dasho.mcserver.net.NetUtils;
import ml.zer0dasho.mcserver.net.VarInt;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket;

public class LoginStart extends MinecraftPacket {

	public final int ID = 0x00;
	public String name;
	
	public LoginStart(ByteBuffer buffer) {
		try {
			this.decode(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String toString() {
		return 
				"LoginStart:\n" 
				+ super.toString() 
				+ String.format(
						"name: %s\n",
						name
				);
	}

	@Override
	public void decode(ByteBuffer buffer) throws IOException {
		VarInt.getVarInt(buffer);
		VarInt.getVarInt(buffer);
		this.name = NetUtils.readUTF8(buffer);
	}

	@Override
	public ByteBuffer encode() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		
		result.write(NetUtils.writeUTF8s(this.name));
		
		VarInt.putVarInt(ID, bos);
		VarInt.putVarInt(result.size(), bos);
		result.writeTo(bos);
		
		return ByteBuffer.wrap(bos.toByteArray());
	}
}
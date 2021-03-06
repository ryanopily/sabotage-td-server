package ml.zer0dasho.mcserver.net.packets.play.out;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import ml.zer0dasho.mcserver.net.NetUtils;
import ml.zer0dasho.mcserver.net.VarInt;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket;

public class SpawnPosition extends MinecraftPacket {

	public final int ID = 0x05;
	public long position;
	
	public SpawnPosition(long position) {
		this.position = position;
	}

	@Override
	public ByteBuffer encode() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		
		result.write(NetUtils.writeLongs(position));
		
		VarInt.putVarInt(ID, bos);
		VarInt.putVarInt(result.size(), bos);
		result.writeTo(bos);
		
		return ByteBuffer.wrap(bos.toByteArray());
	}


	@Override
	public void decode(ByteBuffer buffer) throws IOException {
		VarInt.getVarInt(buffer);
		VarInt.getVarInt(buffer);
		
		this.position = buffer.getLong();
	}
	
}

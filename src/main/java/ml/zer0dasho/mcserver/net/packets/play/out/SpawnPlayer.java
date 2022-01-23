package ml.zer0dasho.mcserver.net.packets.play.out;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

import ml.zer0dasho.mcserver.net.NetUtils;
import ml.zer0dasho.mcserver.net.VarInt;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket;

public class SpawnPlayer extends MinecraftPacket {

	public static final int ID = 0x0C;
	public UUID id;
	
	@Override
	public void decode(ByteBuffer buffer) throws IOException {

	}

	@Override
	public ByteBuffer encode() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		
		VarInt.putVarInt(ID, result);
		VarInt.putVarInt(469, result);
		
		result.write(NetUtils.writeLongs(id.getMostSignificantBits(), id.getLeastSignificantBits()));
		result.write(NetUtils.writeInts(0, 1, 0));
		result.write(0);
		result.write(0);
		result.write(NetUtils.writeShorts((short)0));
		result.write(3 << 5 | 6 & 0x1F);
		result.write(NetUtils.writeFloats(20.0f));
		result.write(127);
		
		VarInt.putVarInt(result.size(), bos);
		result.writeTo(bos);
		
		return ByteBuffer.wrap(bos.toByteArray());
	}
	
}

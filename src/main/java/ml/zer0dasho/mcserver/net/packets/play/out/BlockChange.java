package ml.zer0dasho.mcserver.net.packets.play.out;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import ml.zer0dasho.mcserver.net.VarInt;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket;

public class BlockChange extends MinecraftPacket {

	public final int ID = 0x23;
	
	public long position;
	public int blockId;
	
	public BlockChange(long position, int blockId) {
		this.position = position;
		this.blockId = blockId;
	}
	
	@Override
	public void decode(ByteBuffer buffer) throws IOException {
		VarInt.getVarInt(buffer);
		VarInt.getVarInt(buffer);
		
		this.position = buffer.getLong();
		this.blockId = VarInt.getVarInt(buffer);
	}

	@Override
	public ByteBuffer encode() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		
		VarInt.putVarInt(ID, result);
		result.write(ByteBuffer.allocate(8).putLong(position).array());
		VarInt.putVarInt(blockId, result);
		
		VarInt.putVarInt(result.size(), bos);
		result.writeTo(bos);
		
		return ByteBuffer.wrap(bos.toByteArray());
	}
}

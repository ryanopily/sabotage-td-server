package ml.zer0dasho.mcserver.net.packets.play.out;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import ml.zer0dasho.mcserver.net.NetUtils;
import ml.zer0dasho.mcserver.net.VarInt;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket;
import ml.zer0dasho.mcserver.net.packets.play.out.ChunkData.ChunkSection;

public class MapChangeBulk extends MinecraftPacket {

	public final int ID = 0x26;
	
	public byte skylight_sent;
	public int chunk_column_count;
	
	public ChunkMeta[] chunk_meta;
	public ChunkSection[] chunk_data;
	
	public static class ChunkMeta {
		public int chunkX = 0, chunkY = 0;
		public short bitmask = 1;
		
		public byte[] encode() throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bos.write(NetUtils.writeInts(chunkX, chunkY));
			bos.write(NetUtils.writeShorts(bitmask));
			return bos.toByteArray();
		}
	}
	
	@Override
	public void decode(ByteBuffer buffer) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ByteBuffer encode() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(), result = new ByteArrayOutputStream();
		
		VarInt.putVarInt(ID, result);
		result.write(skylight_sent);
		VarInt.putVarInt(chunk_column_count, result);
		
		Arrays.stream(chunk_meta).forEach(data -> {
			try {
				result.write(data.encode());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		Arrays.stream(chunk_data).forEach(data -> {
			try {
				result.write(data.encode());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		VarInt.putVarInt(result.size(), bos);
		bos.write(result.toByteArray());
		return ByteBuffer.wrap(bos.toByteArray());
	}

}
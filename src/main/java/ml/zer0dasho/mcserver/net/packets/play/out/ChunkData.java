package ml.zer0dasho.mcserver.net.packets.play.out;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import ml.zer0dasho.mcserver.net.NetUtils;
import ml.zer0dasho.mcserver.net.VarInt;
import ml.zer0dasho.mcserver.net.packets.MinecraftPacket;

public class ChunkData extends MinecraftPacket  {

	public final int ID = 0x21;
	
	public ChunkSection[] data;
	
	public int chunkX, chunkZ;
	public short primaryBitmask;
	public boolean groundUpContinuous;
	
	public ChunkData(int chunkX, int chunkZ, ChunkSection[] data) {
		this.chunkX = chunkX;
		this.chunkZ = chunkZ;
		this.data = data;
		this.groundUpContinuous = true;
	}
	
	@Override
	public void decode(ByteBuffer buffer) throws IOException {
		VarInt.getVarInt(buffer);
		VarInt.getVarInt(buffer);
		this.chunkX = buffer.getInt();
		this.chunkZ = buffer.getInt();
		this.groundUpContinuous = buffer.get() == 0 ? false : true;
		this.primaryBitmask = buffer.getShort();
	}

	@Override
	public ByteBuffer encode() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		
		VarInt.putVarInt(ID, result);
		result.write(NetUtils.writeInts(chunkX, chunkZ));
		result.write(groundUpContinuous ? 1 : 0);
		result.write(NetUtils.writeShorts((short)(Short.MAX_VALUE >> (15 - data.length))));
		
		ByteArrayOutputStream sectionData = new ByteArrayOutputStream();
		for(ChunkSection section : data)
			sectionData.write(section.encode());
		
		VarInt.putVarInt(sectionData.size(), result);
		result.write(sectionData.toByteArray());
		
		VarInt.putVarInt(result.size(), bos);
		result.writeTo(bos);
		
		return ByteBuffer.wrap(bos.toByteArray());
	}
	
	public static class ChunkSection {
		
		public byte[] chunk_data;
		public int bits_per_block;
		public int total_elements;
		public byte[] light;
		public byte[] biome;
		
		public ChunkSection() {
			this.chunk_data = new byte[16*16*16 * 2];
			this.bits_per_block = 8;
			this.total_elements = 16 * 16 * 16;
			this.light = new byte[total_elements];
			this.biome = new byte[256];
			
			int pos = 0;
			
			for(int i = 0; i < 4096; i++) {
				int block = (1 << 4) | 0;
				
				chunk_data[pos++] = (byte) (block & 0xff);
				chunk_data[pos++] = (byte) ((block >> 8) & 0xff);
			}
			
			Arrays.fill(light, (byte)(0xFF));
			Arrays.fill(biome, (byte) 2);
		}
		
		public byte[] encode() throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			
			bos.write(chunk_data);
			VarInt.putVarInt(bits_per_block, bos);
			VarInt.putVarInt(total_elements, bos);
			bos.write(this.light);
			bos.write(this.biome);
			
			return bos.toByteArray();
		}
	}
}
package ml.zer0dasho.mcserver.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class NetUtils {

	public static String readUTF8(ByteBuffer buffer) throws IOException {
		int length = VarInt.getVarInt(buffer);
		byte[] string = new byte[length];
		buffer.get(string);
		return new String(string);
	}
	
	public static byte[] writeChars(char...values) {
		ByteBuffer buffer = ByteBuffer.allocate(Character.BYTES * values.length);
		
		for(char value : values) {
			buffer.putChar(value);
		}
		
		return buffer.array();
	}
	
	public static byte[] writeShorts(short...values) {
		ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES * values.length);
		
		for(short value : values)
			buffer.putShort(value);
		
		return buffer.array();
	}
	
	public static byte[] writeInts(int...values) {
		ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES * values.length);
		
		for(int value : values) 
			buffer.putInt(value);
		
		return buffer.array();
	}
	
	public static byte[] writeUTF8s(String...values) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
		for(String value : values) {
	        final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
	        VarInt.putVarInt(bytes.length, bos);
	        bos.write(bytes);
		}
		
        return bos.toByteArray();
	}
	
	public static byte[] writeDoubles(double...values) {
		ByteBuffer buffer = ByteBuffer.allocate(Double.BYTES * values.length);
		
		for(double value : values) 
			buffer.putDouble(value);
		
		return buffer.array();
	}
	
	public static byte[] writeFloats(float...values) {
		ByteBuffer buffer = ByteBuffer.allocate(Float.BYTES * values.length);
		
		for(float value : values)
			buffer.putFloat(value);
		
		return buffer.array();
	}
	
	public static byte[] writeLongs(long...values) {
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES * values.length);
		
		for(long value : values) 
			buffer.putLong(value);
		
		return buffer.array();
	}
	
	public static long position(int x, int y, int z) {
		return ((x & 0x3FFFFFF) << 38) | ((z & 0x3FFFFFF) << 12) | (y & 0xFFF);
	}
}

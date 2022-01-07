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
	
	public static byte[] writeUTF8(String value) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
        final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        VarInt.putVarInt(bytes.length, bos);
        bos.write(bytes);
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
	
}

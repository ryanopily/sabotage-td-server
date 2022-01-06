package ml.zer0dasho.mcserver.net.packets;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import ml.zer0dasho.mcserver.net.Connection;
import ml.zer0dasho.mcserver.net.VarInt;

public class MinecraftPacket {
	
		public void apply(Connection connection) {}
	
		public static class ReadablePacket extends MinecraftPacket {
			
			public final int length;
			public final int id;
			public final ByteBuffer data;
			
			public ReadablePacket(ByteBuffer data) {
				this.data = data;
				this.length = this.readVarInt();
				this.id = this.readVarInt();
			}
			
			
			
		    public String readUTF8() {
		        // Read the string's length
		        final int len = readVarInt();
		        final byte[] bytes = new byte[len];
		        data.get(bytes);
		        return new String(bytes, StandardCharsets.UTF_8);
		    }
			
		    public int readVarInt() {
		        return VarInt.getVarInt(data);
		    }
		    
			public long readVarLong() {
				return VarInt.getVarLong(data);
			}

			@Override
			public String toString() {
				return 
					String.format(
							"length: %d\n" +
							"id: %d\n",
							length,
							id
					);	
			}
		}
		
		public static class WriteablePacket extends MinecraftPacket {
			
			public ByteArrayOutputStream os;
			protected ByteBuffer buffer;
			
			public WriteablePacket() {
				this.os = new ByteArrayOutputStream();
				this.buffer = ByteBuffer.allocate(10);
			}
			
			public ByteBuffer encode() {
				byte[] output = os.toByteArray();
				int size = VarInt.varIntSize(output.length);

				ByteBuffer result = ByteBuffer.allocate(size + output.length);
				VarInt.putVarInt(output.length, result);
				result.put(output);
				result.flip();
				
				return result;
			}
			
		    public void writeUTF8(String value) {
		        final byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
		        
		        writeVarInt(bytes.length);
		        try {
					os.write(bytes);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    
			public void writeVarLong(long value) {
				try {
					VarInt.putVarLong(value, os);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		    
			public void writeVarInt(int value) {
				try {
					VarInt.putVarInt(value, os);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			public void writeDouble(double value) {
				try {
					byte[] data = new byte[Double.BYTES];
					this.buffer.putDouble(0, value);
					this.buffer.get(0, data);
					
					os.write(data);
				} catch(IOException ex) {
					ex.printStackTrace();
				}
			}
			
			public void writeFloat(float value) {
				try {
					byte[] data = new byte[Float.BYTES];
					this.buffer.putFloat(0, value);
					this.buffer.get(0, data);
					
					os.write(data);
				} catch(IOException ex) {
					ex.printStackTrace();
				}
			}
			
			
			public void writeInt(int value) {
				try {
					byte[] data = new byte[Integer.BYTES];
					this.buffer.putInt(0, value);
					this.buffer.get(0, data);
					os.write(data);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			public void writeByte(byte value) {
				os.write(value);
			}

		}
	
}

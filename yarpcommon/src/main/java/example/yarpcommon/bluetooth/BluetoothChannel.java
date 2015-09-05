package example.yarpcommon.bluetooth;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

public class BluetoothChannel {

	private InputStream inputStream;
	private OutputStream outputStream;
	private BluetoothStatus bluetoothStatus;
	private String connectedTargetName;
	private String connectedTargetAddress;

	public BluetoothChannel(InputStream inputStream, OutputStream outputStream, BluetoothStatus bluetoothStatus, String connectedTargetName, String connectedTargetAddress) {
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		this.bluetoothStatus = bluetoothStatus;
		this.connectedTargetName = connectedTargetName;
        this.connectedTargetAddress = connectedTargetAddress;
	}

	public boolean isValid() {
		return bluetoothStatus.isValid();
	}

	public boolean sendImage(String imageFilename) throws IOException {
		if (isValid()) {
			Bitmap bm = BitmapFactory.decodeFile(imageFilename);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 80, baos);
			byte[] imageInByteArray = baos.toByteArray();
			send(imageInByteArray);
			return true;
		} else {
			return false;
		}
	}

	public Bitmap receiveImage(BluetoothReceivingCallback receivingCallback) throws IOException {
		if (isValid()) {
			byte[] imageData = receive(receivingCallback);
			Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
			return bitmap;
		} else {
			return null;
		}
	}

	public boolean sendString(String message) throws IOException {
		if (isValid()) {
			byte[] messageInByteArray = message.getBytes();
			send(messageInByteArray);
			return true;
		} else {
			return false;
		}
	}

	public String receiveString(BluetoothReceivingCallback receivingCallback) throws IOException {
		if (isValid()) {
			byte[] messageData = receive(receivingCallback);
			return new String(messageData);
		} else {
			return null;
		}
	}

	public boolean sendInteger(int integerValue) throws IOException {
		if (isValid()) {
			ByteBuffer bb = ByteBuffer.allocate(4);
			bb.putInt(integerValue);
			byte[] data = bb.array();

			outputStream.write(data);
			return true;
		} else {
			return false;
		}
	}

	public int receiveInteger() throws IOException {
		int result = -1;
		if (isValid()) {
			byte[] data = new byte[4];
			inputStream.read(data);
			result = ByteBuffer.wrap(data).getInt();
		}

		return result;
	}

	public boolean sendByte(byte byteValue) throws IOException {
		if (isValid()) {
			ByteBuffer bb = ByteBuffer.allocate(1);
			bb.put(byteValue);
			byte[] data = bb.array();

			outputStream.write(data);
			return true;
		} else {
			return false;
		}
	}

	public byte receiveByte() throws IOException {
		byte result = -1;
		if (isValid()) {
			result = (byte)inputStream.read();
		}

		return result;
	}
	
	public boolean sendLong(long longValue) throws IOException {
		if (isValid()) {
			ByteBuffer bb = ByteBuffer.allocate(8);
			bb.putLong(longValue);
			byte[] data = bb.array();

			outputStream.write(data);
			return true;
		} else {
			return false;
		}
	}

	public long receiveLong() throws IOException {
		long result = -1;
		if (isValid()) {
			byte[] data = new byte[8];
			inputStream.read(data);
			result = ByteBuffer.wrap(data).getLong();
		}

		return result;
	}
	
	public void send(byte[] data) throws IOException {
		try {
			sendLengthOfData(data);
			sendActualData(data);
		} catch (IOException e) {
			bluetoothStatus.setValid(false);
			throw e;
		}
	}

	public void sendFloatArray(float[] data) throws IOException {
		ByteBuffer byteBuffer = ByteBuffer.allocate(4 * data.length);
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
		floatBuffer.put(data);
		send(byteBuffer.array());
	}

	public float[] receiveFloatArray(BluetoothReceivingCallback receivingCallback) throws IOException {
		byte[] data = receive(receivingCallback);
		ByteBuffer byteBuffer = ByteBuffer.wrap(data);
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();

		float[] floatArray = new float[floatBuffer.limit()];
		floatBuffer.get(floatArray);

		return floatArray;
	}

	private void sendLengthOfData(byte[] data) throws IOException {
		ByteBuffer lengthOfDataInByteBuffer = ByteBuffer.allocate(4);
		lengthOfDataInByteBuffer.putInt(data.length);
		outputStream.write(lengthOfDataInByteBuffer.array());
	}

	private void sendActualData(byte[] data) throws IOException {
		outputStream.write(data);
		outputStream.flush();
	}

	public byte[] receive(BluetoothReceivingCallback receivingCallback) throws IOException {
		try {
			int expectedDataSize = receiveLengthOfData();
			byte[] data = new byte[expectedDataSize];
			receiveActualData(receivingCallback, expectedDataSize, data);
			return data;
		} catch (IOException e) {
			bluetoothStatus.setValid(false);
			throw e;
		}
	}

	private int receiveLengthOfData() throws IOException {
		byte[] lengthOfDataInByteBuffer = new byte[4];
		inputStream.read(lengthOfDataInByteBuffer);
		return ByteBuffer.wrap(lengthOfDataInByteBuffer).getInt();
	}
	
	private void receiveActualData(BluetoothReceivingCallback receivingCallback, int expectedDataSize, byte[] data) throws IOException {
		int totalBytesRead = 0;
		int currentBytesRead = 0;
		int availableSize = expectedDataSize;
		while (totalBytesRead < expectedDataSize) {
			currentBytesRead = inputStream.read(data, totalBytesRead, availableSize);
			totalBytesRead += currentBytesRead;
			availableSize -= currentBytesRead;
			if (receivingCallback != null) {
				receivingCallback.uponReceiving(totalBytesRead, expectedDataSize);
			}
		}
	}

	public String getConnectedTargetName() {
		return connectedTargetName;
	}

    public String getConnectedTargetAddress() {
        return connectedTargetAddress;
    }
}

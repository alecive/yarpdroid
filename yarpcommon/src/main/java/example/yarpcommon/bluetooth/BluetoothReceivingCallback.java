package example.yarpcommon.bluetooth;

public interface BluetoothReceivingCallback {

	void uponReceiving(int totalBytesRead, int expectedTotalBytes);
}

package example.yarpcommon.bluetooth;

import java.io.IOException;

public interface BluetoothWorker {

	void doWork(BluetoothChannel bluetoothChannel) throws IOException, BluetoothException;
	
}

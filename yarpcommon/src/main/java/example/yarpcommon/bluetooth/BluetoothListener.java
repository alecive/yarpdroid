package example.yarpcommon.bluetooth;

public class BluetoothListener {

	private boolean isRunning;
	private BluetoothChannel bluetoothChannel;
	private BluetoothWorker bluetoothWorker;
	private Thread thread;
	private BluetoothListenerExceptionHandler exceptionHandler;

	public BluetoothListener(BluetoothChannel bluetoothChannel, BluetoothWorker bluetoothWorker, BluetoothListenerExceptionHandler exceptionHandler) {
		this.bluetoothChannel = bluetoothChannel;
		this.bluetoothWorker = bluetoothWorker;
		this.exceptionHandler = exceptionHandler;
		createThread();
	}

	private void createThread() {
		thread = new Thread() {
			public void run() {
				while (isRunning) {
					if (bluetoothWorker != null) {
						try {
							bluetoothWorker.doWork(bluetoothChannel);
						} catch (Exception e) {
							isRunning = false;
                            if (exceptionHandler != null) {
                                exceptionHandler.handleException(e);
                            }
						}
					}
				}
			}
		};
	}

	public void start() {
		isRunning = true;
		thread.start();
	}

	public void stop() {
		isRunning = false;
		thread.interrupt();
	}

}

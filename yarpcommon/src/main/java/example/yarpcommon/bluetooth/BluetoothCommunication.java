package example.yarpcommon.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import example.yarpcommon.ApplicationID;

public class BluetoothCommunication {

	private static final String NAME = "Bluetooth";
	private static final String TAG = "sg.edu.astar.i2r.vc.commonlibrary.bluetooth.BluetoothCommunication";
	private static final String ERROR_BLUETOOTH_NOT_AVAILABLE = "Bluetooth is not available.";
	private static final String ERROR_BLUETOOTH_NOT_ENABLED = "Bluetooth is not enabled.";
	private static final String ERROR_INCORRECT_TARGET_APPLICATION = "Target is running wrong app. ";

    private UUID uuid; //"00001101-0000-1000-8000-00805F9B34FB"
	private ApplicationID applicationID;

	private BluetoothStatus bluetoothStatus;

	private BluetoothAdapter bluetoothAdapter;
	private BluetoothServerSocket bluetoothServerSocket = null;
	private BluetoothSocket connectedListenerSocket = null;
	private BluetoothSocket connectedInitiatorSocket = null;
	private BluetoothDevice device;

	public BluetoothCommunication(ApplicationID applicationID) {
		this.applicationID = applicationID;
        this.uuid = this.applicationID.getUuid();
		setupBluetoothStatus();
	}

	private void setupBluetoothStatus() {
		bluetoothStatus = new BluetoothStatus();
		bluetoothStatus.setValid(false);
	}

	public BluetoothChannel getNewChannelAsListener() throws BluetoothException {
		BluetoothChannel bluetoothChannel = null;
		setupBluetooth();
		try {
			listenAndAcceptIncomingConnection();
			bluetoothStatus.setValid(true);
			bluetoothChannel = new BluetoothChannel(connectedListenerSocket.getInputStream(), connectedListenerSocket.getOutputStream(), bluetoothStatus, 
					connectedListenerSocket.getRemoteDevice().getName(), connectedListenerSocket.getRemoteDevice().getAddress());
            Log.d(TAG, "Connected device address= " + connectedListenerSocket.getRemoteDevice().getAddress());
			performHandshakingCheckAsListener(bluetoothChannel);
		} catch (IOException e) {
			throw onError(e.getMessage());
		}
		return bluetoothChannel;
	}

	private void listenAndAcceptIncomingConnection() throws IOException {
		bluetoothServerSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME, uuid);
		connectedListenerSocket = bluetoothServerSocket.accept();
	}

	public BluetoothChannel getNewChannelAsInitiator() throws BluetoothException {
		BluetoothChannel bluetoothChannel = null;
		setupBluetooth();
		try {
			sendConnectionRequest();
			bluetoothStatus.setValid(true);
			bluetoothChannel = new BluetoothChannel(connectedInitiatorSocket.getInputStream(), connectedInitiatorSocket.getOutputStream(), bluetoothStatus, 
					connectedInitiatorSocket.getRemoteDevice().getName(), connectedInitiatorSocket.getRemoteDevice().getAddress());
			performHandshakingCheckAsInitiator(bluetoothChannel);
		} catch (IOException e) {
			throw onError(e.getMessage());
		}
		return bluetoothChannel;
	}

    public BluetoothChannel getNewChannelAsInitiator(String address) throws BluetoothException {
        BluetoothChannel bluetoothChannel = null;
        setupBluetooth();
        try {
            sendConnectionRequest(address);
            bluetoothStatus.setValid(true);
            bluetoothChannel = new BluetoothChannel(connectedInitiatorSocket.getInputStream(), connectedInitiatorSocket.getOutputStream(), bluetoothStatus,
                    connectedInitiatorSocket.getRemoteDevice().getName(), connectedInitiatorSocket.getRemoteDevice().getAddress());
            performHandshakingCheckAsInitiator(bluetoothChannel);
        } catch (IOException e) {
            throw onError(e.getMessage());
        }
        return bluetoothChannel;
    }

	public void close() {
		bluetoothStatus.setValid(false);
		closeBluetoothListenerSocket();
		closeBluetoothInitiatorSocket();
		closeBluetoothServerSocket();
	}

	private void closeBluetoothServerSocket() {
		if (bluetoothServerSocket != null) {
			try {
				bluetoothServerSocket.close();
			} catch (IOException e) {
			}
		}
	}

	private void closeBluetoothInitiatorSocket() {
		if (connectedInitiatorSocket != null) {
			try {
				connectedInitiatorSocket.close();
			} catch (IOException e) {
			}
		}
	}

	private void closeBluetoothListenerSocket() {
		if (connectedListenerSocket != null) {
			try {
				connectedListenerSocket.close();
			} catch (IOException e) {
			}
		}
	}

	private void sendConnectionRequest() throws IOException {
		ArrayList<String> macAddresses = getPairedDevicesMacAddress();
        sendConnectionRequest(macAddresses.get(0));
	}

    private void sendConnectionRequest(String macAddress) throws IOException {
        device = bluetoothAdapter.getRemoteDevice(macAddress);
        connectedInitiatorSocket = device.createInsecureRfcommSocketToServiceRecord(uuid);
        bluetoothAdapter.cancelDiscovery();
        connectedInitiatorSocket.connect();
    }

	private BluetoothException onError(String message) {
		bluetoothStatus.setValid(false);
		return new BluetoothException(message);
	}

	private void performHandshakingCheckAsListener(BluetoothChannel bluetoothChannel) throws IOException, BluetoothException {
		int receivedId = bluetoothChannel.receiveInteger();
		bluetoothChannel.sendInteger(applicationID.getValue());
		ApplicationID targetApplication = ApplicationID.getApplicationID(receivedId);
		if (!isTargetCorrectApplication(targetApplication)) {
			throw onError(ERROR_INCORRECT_TARGET_APPLICATION + " Expecting " + applicationID.getDisplayName() + " but received " + targetApplication.getDisplayName());
		}
	}

	private void performHandshakingCheckAsInitiator(BluetoothChannel bluetoothChannel) throws IOException, BluetoothException {
		bluetoothChannel.sendInteger(applicationID.getValue());
		int receivedId = bluetoothChannel.receiveInteger();
		ApplicationID targetApplication = ApplicationID.getApplicationID(receivedId);
		if (!isTargetCorrectApplication(targetApplication)) {
			throw onError(ERROR_INCORRECT_TARGET_APPLICATION + " Expecting " + applicationID.getDisplayName() + " but received " + targetApplication.getDisplayName());
		}
	}

	private boolean isTargetCorrectApplication(ApplicationID targetApplication) {
		return (targetApplication.getValue() == applicationID.getValue());
	}

	private void setupBluetooth() throws BluetoothException {
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		verifyBluetoothIsPresent();
		verifyBluetoothIsEnabled();
	}

	private void verifyBluetoothIsPresent() throws BluetoothException {
		if (bluetoothAdapter == null) {
			throw new BluetoothException(ERROR_BLUETOOTH_NOT_AVAILABLE);
		}
	}

	private void verifyBluetoothIsEnabled() throws BluetoothException {
		if (!bluetoothAdapter.isEnabled()) {
			throw new BluetoothException(ERROR_BLUETOOTH_NOT_ENABLED);
		}
	}

	private ArrayList<String> getPairedDevicesMacAddress() {
		ArrayList<String> macAddresses = new ArrayList<String>();

		if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
			// get paired devices
			Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
			for (BluetoothDevice device : pairedDevices) {
				macAddresses.add(device.getAddress());
			}
		}
		return macAddresses;
	}

}

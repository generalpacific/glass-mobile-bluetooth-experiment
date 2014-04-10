package com.example.bluetoothexperiment.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.bluetoothexperiment.MainActivity;
import com.example.bluetoothexperiment.R;
import com.example.bluetoothexperiment.requestresponse.RequestManager;
import com.example.bluetoothexperiment.requestresponse.request.LocalExecutor;

public final class BluetoothManager {
	private final String CLASS_NAME = BluetoothManager.class.getSimpleName();
	private static BluetoothManager instance;
	
	private TextView myLabel;
	private EditText myTextboxData;
	private Spinner mySpinnerDeviceName;
	private TextView myDataLabel;
	
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothSocket mmSocket;
	private BluetoothSocket mmFallBackSocket;
	private BluetoothSocket mmClientSocket;
	private BluetoothDevice mmDevice;
	private OutputStream mmOutputStream;
	
	private InputStream mmInputStream;
	private Thread workerThread;
	private byte[] readBuffer;
	private int readBufferPosition;
	private volatile boolean stopWorker;
	private MainActivity mainActivity;
	private boolean isInitialized;
	private boolean isServerInitialized;
	private boolean isClientInititialized;
	private boolean useFallBack = false;
	private String deviceName;
	private static boolean isBusy = false;

	private BluetoothManager(MainActivity mainActivity) {
		if (instance != null) {
			throw new IllegalStateException(
					"Cannot instantiate BluetoothManager twice.");
		}
		this.mainActivity = mainActivity;
		mySpinnerDeviceName = (Spinner)mainActivity.findViewById(R.id.paired_devices_spinner);
		myLabel = (TextView)mainActivity.findViewById(R.id.label2);
		myTextboxData = (EditText)mainActivity.findViewById(R.id.entry2);
		myDataLabel = (TextView)mainActivity.findViewById(R.id.datalabel);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
	}

	public static BluetoothManager getInstance(MainActivity mainActivity) {
		if (instance == null) {
			synchronized (BluetoothManager.class) {
				if (instance == null) {
					instance = new BluetoothManager(mainActivity);
				}
			}
		}
		return instance;
	}

	public boolean findBT() {
		String logName = CLASS_NAME + ".findBT()";
		Log.i(logName, "in findBT");
		deviceName = mySpinnerDeviceName.getSelectedItem().toString();
		if(deviceName == null || deviceName.isEmpty()) {
			myLabel.setText("Please add the device name.");
			return false;
		}
		Log.i(logName, "deviceName : " + deviceName);
		if (mBluetoothAdapter == null) {
			myLabel.setText("No bluetooth adapter available");
			return false;
		}

		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBluetooth = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			mainActivity.startActivityForResult(enableBluetooth, 0);
		}

		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
				.getBondedDevices();
		if (pairedDevices.size() > 0) {
			for (BluetoothDevice device : pairedDevices) {
				if (device.getName().equalsIgnoreCase(deviceName)) {
					mmDevice = device;
					break;
				}
			}
		}
		if(mmDevice == null) {
			myLabel.setText("Bluetooth Not found Device Found");
			return false;
		}
		Log.i(logName, deviceName + " FOUND!!!!!!!!!!!!!");
		myLabel.setText("Bluetooth Device Found");
		return true;
	}
	
	public void serverBT() throws IOException {
		String logName = CLASS_NAME + ".serverBT()";
		UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
		
		mBluetoothAdapter.cancelDiscovery();
		
		BluetoothServerSocket listenUsingRfcommWithServiceRecord = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(deviceName, uuid);
		Log.i(logName, "server socket created");
		mmClientSocket = listenUsingRfcommWithServiceRecord.accept();
		mmInputStream = mmClientSocket.getInputStream();
		Log.i(logName, "connection accepted");
		isServerInitialized = true;
		if(isClientInititialized) {
			changeUILabel(myLabel, "bluetooth opened");
			isInitialized = true;
		}
		listenUsingRfcommWithServiceRecord.close();
	}

	public void openBT() throws IOException {
		UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); // Standard
																				// SerialPortService
																				// ID
		mBluetoothAdapter.cancelDiscovery();
		mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
		changeUILabel(myLabel, "socket created");
		try {
			mmSocket.connect();
		}catch(IOException ex) {
			Class<?> clazz = mmSocket.getRemoteDevice().getClass();
			Class<?>[] paramTypes = new Class<?>[] {Integer.TYPE};

			Method m = null;
			try {
				m = clazz.getMethod("createRfcommSocket", paramTypes);
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Object[] params = new Object[] {Integer.valueOf(1)};

			try {
				mmFallBackSocket = (BluetoothSocket) m.invoke(mmSocket.getRemoteDevice(), params);
				useFallBack = true;
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try{
				mmFallBackSocket.connect();
			}catch(IOException e) {
				e.printStackTrace();
				changeUILabel(myLabel, "IOEXCEPTION");
				return;
			}
		}
		changeUILabel(myLabel, "socket connected");
		if(!useFallBack) {
			mmOutputStream = mmSocket.getOutputStream();
		}else {
			mmOutputStream = mmFallBackSocket.getOutputStream();
		}
		//mmInputStream = mmSocket.getInputStream();
		changeUILabel(myLabel, "IO opened");

		beginListenForData();

		isClientInititialized = true;
		if(isServerInitialized) {
			changeUILabel(myLabel, "bluetooth opened");
			isInitialized = true;
		}
	}

	void beginListenForData() {
		final byte delimiter = 10; // This is the ASCII code for a newline
									// character

		stopWorker = false;
		readBufferPosition = 0;
		readBuffer = new byte[100000];
		workerThread = new Thread(new Runnable() {
			public void run() {
				while (!Thread.currentThread().isInterrupted() && !stopWorker) {
					try {
						if(mmInputStream == null) {
							continue;
						}
						int bytesAvailable = mmInputStream.available();
						if (bytesAvailable > 0) {
							byte[] packetBytes = new byte[bytesAvailable];
							mmInputStream.read(packetBytes);
							for (int i = 0; i < bytesAvailable; i++) {
								byte b = packetBytes[i];
								if (b == delimiter) {
									byte[] encodedBytes = new byte[readBufferPosition];
									System.arraycopy(readBuffer, 0,
											encodedBytes, 0,
											encodedBytes.length);
									String data = new String(
											encodedBytes, "US-ASCII");
									readBufferPosition = 0;
									Log.e("GOTDATA",data);
									if(mainActivity.isServer()) {
										prependToDataLabel("Got request: " + data);
										String response = LocalExecutor.execute(data);
										prependToDataLabel("Sending reponse: " + response);
										sendData(response + "\n");
									}else {
										//prependToDataLabel("Got response: " + data);
										String[] split = data.split("#");
										int requestId = Integer.parseInt(split[0]);
										String requestStr = new String(RequestManager.getInstance().getRequest(requestId));
										prependToDataLabel("Time taken(Network) for request id " + 
												requestId + " = " + 
												RequestManager.getInstance().removeRequest(requestId) + "ms");
										try {
											Thread.sleep(250);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										prependToDataLabel("Executing: "  + requestStr + " locally.");
										try {
											Thread.sleep(250);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										long start = System.currentTimeMillis();
										String execute = LocalExecutor.execute(requestStr);
										prependToDataLabel("Local response "  + execute);
										long executionTime = System.currentTimeMillis() - start;
										try {
											Thread.sleep(250);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										prependToDataLabel("Time taken(Local) for request id " + 
												requestId + " = " + 
												executionTime + "ms");
									}
								} else {
									readBuffer[readBufferPosition++] = b;
								}
							}
						}
					} catch (IOException ex) {
						stopWorker = true;
					}
				}
			}
		});

		workerThread.start();
	}

	public synchronized void sendData() throws IOException {
		if(!isInitialized) {
			Log.e("SEND DATA", "NOT INITIALIZED");
			return;
		}
		String msg = myTextboxData.getText().toString();
		msg += "\n";
		Log.e("SEND DATA", "sending :" + msg);
		sendData(msg);
		myLabel.setText("Data Sent");
	}
	
	public synchronized void sendData(String message) throws IOException {
		if(!isInitialized) {
			Log.e("SEND DATA", "NOT INITIALIZED");
			return;
		}
		mmOutputStream.write(message.getBytes());
	}

	public void closeBT() throws IOException {
		if(!isInitialized) {
			return;
		}
		isInitialized = false;
		isServerInitialized = false;
		isClientInititialized = false;
		stopWorker = true;
		if(mmOutputStream != null) {
			mmOutputStream.close();
		}
		if(mmInputStream != null) {
			mmInputStream.close();
		}
		if(mmSocket != null) {
			mmSocket.close();
		}
		if(mmClientSocket != null) {
			mmClientSocket.close();
		}
		myLabel.setText("Bluetooth Closed");
	}
	
	public boolean isInitialized() {
		return isInitialized;
	}
	
	private synchronized void prependToDataLabel(String message) {
		while(isBusy);
		isBusy = true;
		String currentMessage = myDataLabel.getText().toString();
		currentMessage = message + "\n" + currentMessage;
		final String displayMessage = currentMessage;
		mainActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				myDataLabel.setText(displayMessage);
				
			}
		});
		isBusy = false;
	}
	
	private synchronized void changeUILabel(final TextView view, final String message) {
		mainActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				view.setText(message);
				
			}
		});
	}
	
	public List<String> getPairedDevices() {
		List<String> pairedDevices = new ArrayList<String>();
		if (!mBluetoothAdapter.isEnabled()) {
			Intent enableBluetooth = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			mainActivity.startActivityForResult(enableBluetooth, 0);
		}
		Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
		Log.e("getPairedDevices", bondedDevices.toString());
		if(bondedDevices.size() < 1) {
			return pairedDevices;
		}
		for (BluetoothDevice bluetoothDevice : bondedDevices) {
			pairedDevices.add(bluetoothDevice.getName());
		}
		return pairedDevices;
	}
}


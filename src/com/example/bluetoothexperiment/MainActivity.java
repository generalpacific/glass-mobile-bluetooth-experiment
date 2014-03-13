package com.example.bluetoothexperiment;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bluetoothexperiment.bluetooth.BluetoothManager;

@SuppressLint("ShowToast")
public class MainActivity extends Activity {
	private final MainActivity mainActivity = this;
	private boolean isServer = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button openButton = (Button) findViewById(R.id.open);
		Button sendButton = (Button) findViewById(R.id.send);
		Button closeButton = (Button) findViewById(R.id.close);
		
		hideAll();
		
		String[] clientServerArray = {"", "Client", "Server"};
		final Spinner clientServerSpinner = (Spinner)findViewById(R.id.client_server_spinner);
		ArrayAdapter<String> clientServerArrayAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, 
				clientServerArray);
		clientServerArrayAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		clientServerSpinner.setAdapter(clientServerArrayAdaptor);
		clientServerSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String string = clientServerSpinner.getSelectedItem().toString();
				if(string == null || string.isEmpty()) {
					return;
				}
				if(string.equals("Server")) {
					isServer = true;
				}else if(string.equals("Client")) {
					isServer = false;
				}
				makeVisible(isServer);
				clientServerSpinner.setEnabled(false);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// DO NOTHING
			}

		});
		
		Spinner spinner = (Spinner)findViewById(R.id.paired_devices_spinner);
		ArrayAdapter<String> devicesArrayAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, 
				BluetoothManager.getInstance(mainActivity).getPairedDevices());
		devicesArrayAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(devicesArrayAdaptor);

		// Open Button
		openButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
					if(BluetoothManager.getInstance(mainActivity).isInitialized()) {
						Toast.makeText(getApplicationContext(), "Bluetooth connection is already initialized", Toast.LENGTH_SHORT).show();
						return;
					}
					if(BluetoothManager.getInstance(mainActivity).findBT()) {
						Thread openBTThread = new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									BluetoothManager.getInstance(mainActivity).openBT();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
						}); 
						
						Thread serverBTThread = new Thread( new Runnable() {
							@Override
							public void run() {
								try {
									BluetoothManager.getInstance(mainActivity).serverBT();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
						});
						
						openBTThread.start();
						serverBTThread.start();
						Toast.makeText(getApplicationContext(), "Bluetooth connection opened", Toast.LENGTH_SHORT).show();
					}else {
						Toast.makeText(getApplicationContext(), "Bluetooth Device not found", Toast.LENGTH_SHORT).show();
					}
			}
		});

		// Send Button
		sendButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					if(BluetoothManager.getInstance(mainActivity).isInitialized()) {
						BluetoothManager.getInstance(mainActivity).sendData();
					}else {
						Toast.makeText(getApplicationContext(), "Bluetooth Manager not initialized.", Toast.LENGTH_SHORT).show();
					}
				} catch (IOException ex) {
				}
			}
		});

		// Close button
		closeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					if(BluetoothManager.getInstance(mainActivity).isInitialized()) {
						BluetoothManager.getInstance(mainActivity).closeBT();
					}else {
						Toast.makeText(getApplicationContext(), "Bluetooth Manager not initialized.", Toast.LENGTH_SHORT).show();
					}
				} catch (IOException ex) {
				}
			}
		});
	}
	
	protected void makeVisible(boolean isServer) {
		if(isServer) {
			findViewById(R.id.label1).setVisibility(View.VISIBLE);
			findViewById(R.id.label2).setVisibility(View.VISIBLE);
			findViewById(R.id.paired_devices_spinner).setVisibility(View.VISIBLE);
			findViewById(R.id.open).setVisibility(View.VISIBLE);
		}else {
			findViewById(R.id.close).setVisibility(View.VISIBLE);
			findViewById(R.id.entry2).setVisibility(View.VISIBLE);
			findViewById(R.id.label1).setVisibility(View.VISIBLE);
			findViewById(R.id.label2).setVisibility(View.VISIBLE);
			findViewById(R.id.open).setVisibility(View.VISIBLE);
			findViewById(R.id.paired_devices_spinner).setVisibility(View.VISIBLE);
			findViewById(R.id.send).setVisibility(View.VISIBLE);
		}
		
	}

	private void hideAll() {
		findViewById(R.id.close).setVisibility(View.INVISIBLE);
		findViewById(R.id.entry2).setVisibility(View.INVISIBLE);
		findViewById(R.id.label1).setVisibility(View.INVISIBLE);
		findViewById(R.id.label2).setVisibility(View.INVISIBLE);
		findViewById(R.id.open).setVisibility(View.INVISIBLE);
		findViewById(R.id.paired_devices_spinner).setVisibility(View.INVISIBLE);
		findViewById(R.id.send).setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	public void onBackPressed() {
		// Do nothing when back button is pressed.
	}
	
	public boolean isServer() {
		return isServer;
	}
}
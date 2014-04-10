package com.example.bluetoothexperiment;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bluetoothexperiment.bluetooth.BluetoothManager;
import com.example.bluetoothexperiment.handler.AdditionRequestHandler;
import com.example.bluetoothexperiment.handler.MatrixMultiplicationHandler;
import com.example.bluetoothexperiment.handler.NQueensRequestHandler;
import com.example.bluetoothexperiment.requestresponse.RequestManager;
import com.example.bluetoothexperiment.requestresponse.Requests;

@SuppressLint("ShowToast")
public class MainActivity extends Activity {
	private final MainActivity mainActivity = this;
	private boolean isServer = false;
	public Requests requestType = Requests.ADDITION;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		hideAll();

		Button openButton = (Button) findViewById(R.id.open);
		Button sendButton = (Button) findViewById(R.id.send);
		Button closeButton = (Button) findViewById(R.id.close);
		
		/* Populate the client/server spinner */
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
		
		/* Populate request type */
		final Spinner requestTypeSpinner = (Spinner)findViewById(R.id.request_type_spinner);
		ArrayAdapter<String> requestArrayAdaptor = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, 
				Requests.requestValues());
		requestArrayAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		requestTypeSpinner.setAdapter(requestArrayAdaptor);
		requestTypeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String string = requestTypeSpinner.getSelectedItem().toString();
				if(string == null || string.isEmpty()) {
					return;
				}
				requestType = Requests.getValue(string);
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
						int requestId = RequestManager.getInstance().getNextRequestId();
						String string = ((EditText)findViewById(R.id.entry2)).getText().toString();
						String msg = null;
						switch(requestType) {
						case MATRIX_MULTIPLICATION:
							msg = MatrixMultiplicationHandler.createRequest(requestId, Integer.parseInt(string));
							RequestManager.getInstance().addRequestForRequestId(requestId, msg);
							break;
						case ADDITION:
							msg = AdditionRequestHandler.createRequest(requestId, string);
							RequestManager.getInstance().addRequestForRequestId(requestId, msg);
							break;
						case NQUEENS:
							msg = NQueensRequestHandler.createRequest(requestId, string);
							RequestManager.getInstance().addRequestForRequestId(requestId, msg);
							break;
						default:
							break;
						}
						Log.e("SEND DATA", "sending :" + msg);
						BluetoothManager.getInstance(mainActivity).sendData(msg);
						((TextView)findViewById(R.id.label2)).setText("Data Sent");
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
		TextView statusLabel = (TextView)findViewById(R.id.label2);
		if(isServer) {
			findViewById(R.id.label1).setVisibility(View.VISIBLE);
			findViewById(R.id.paired_devices_spinner).setVisibility(View.VISIBLE);
			findViewById(R.id.open).setVisibility(View.VISIBLE);
			statusLabel.setText("Status");
			statusLabel.setVisibility(View.VISIBLE);
		}else {
			findViewById(R.id.close).setVisibility(View.VISIBLE);
			findViewById(R.id.entry2).setVisibility(View.VISIBLE);
			findViewById(R.id.label1).setVisibility(View.VISIBLE);
			statusLabel.setVisibility(View.VISIBLE);
			findViewById(R.id.open).setVisibility(View.VISIBLE);
			findViewById(R.id.paired_devices_spinner).setVisibility(View.VISIBLE);
			findViewById(R.id.send).setVisibility(View.VISIBLE);
			findViewById(R.id.label1_1).setVisibility(View.VISIBLE);
			findViewById(R.id.request_type_spinner).setVisibility(View.VISIBLE);
		}
		
	}

	private void hideAll() {
		findViewById(R.id.label1).setVisibility(View.INVISIBLE);
		findViewById(R.id.paired_devices_spinner).setVisibility(View.INVISIBLE);
		findViewById(R.id.label1_1).setVisibility(View.INVISIBLE);
		findViewById(R.id.request_type_spinner).setVisibility(View.INVISIBLE);
		findViewById(R.id.label2).setVisibility(View.INVISIBLE);
		findViewById(R.id.entry2).setVisibility(View.INVISIBLE);
		findViewById(R.id.close).setVisibility(View.INVISIBLE);
		findViewById(R.id.open).setVisibility(View.INVISIBLE);
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
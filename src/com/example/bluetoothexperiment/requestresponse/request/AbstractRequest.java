package com.example.bluetoothexperiment.requestresponse.request;

public abstract class AbstractRequest implements Request {
	protected int requestId;
	
	public final int getRequestId() {
		return requestId;
	}
}

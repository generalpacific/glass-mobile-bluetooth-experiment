package com.example.bluetoothexperiment.requestresponse.request;

import com.example.bluetoothexperiment.handler.AdditionRequestHandler;
import com.example.bluetoothexperiment.requestresponse.RequestSeparators;

/**
 * Request for addition of two numbers
 * @author prashant
 *
 */
public final class AdditionRequest extends AbstractRequest {
	private int a;
	private int b;
	
	public AdditionRequest(int requestId, int a, int b) {
		this.requestId = requestId;
		this.a = a;
		this.b = b;
	}
	
	public int getA() {
		return a;
	}
	
	public int getB() {
		return b;
	}

	@Override
	public String getString() {
		return AdditionRequestHandler.getHeader() + RequestSeparators.HEADER_SEPARATOR + requestId 
				+ RequestSeparators.HEADER_SEPARATOR + a + "," + b;
	}
}



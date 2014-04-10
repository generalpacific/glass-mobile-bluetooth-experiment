package com.example.bluetoothexperiment.requestresponse.request;

import com.example.bluetoothexperiment.handler.NQueensRequestHandler;
import com.example.bluetoothexperiment.requestresponse.RequestSeparators;

public class NQueensRequest extends AbstractRequest {
	private int N = 8;

	public NQueensRequest(int requestId,int N) {
		this.requestId = requestId;
		this.N = N;
	}
	
	public int getN() {
		return N;
	}

	@Override
	public String getString() {
		return NQueensRequestHandler.getHeader() + RequestSeparators.HEADER_SEPARATOR + requestId + 
				RequestSeparators.HEADER_SEPARATOR + N;
	}

}

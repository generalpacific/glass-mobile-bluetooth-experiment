package com.example.bluetoothexperiment.handler;

import com.example.bluetoothexperiment.exception.IllegalRequestException;
import com.example.bluetoothexperiment.exception.UnparsebleException;
import com.example.bluetoothexperiment.nqueens.NQueensUtil;
import com.example.bluetoothexperiment.requestresponse.RequestSeparators;
import com.example.bluetoothexperiment.requestresponse.request.NQueensRequest;
import com.example.bluetoothexperiment.requestresponse.request.Request;

public class NQueensRequestHandler implements Handler {

	private static final String header = "NQueens";
	
	public static String getHeader() {
		return header;
	}
	
	public static String createRequest(int requestId, String message) {
		return header + RequestSeparators.HEADER_SEPARATOR + requestId + RequestSeparators.HEADER_SEPARATOR + message + "\n";
	}
	
	@Override
	public String process(Request request) throws UnparsebleException,
			IllegalRequestException {
		if(!(request instanceof NQueensRequest)) {
			throw new IllegalRequestException(Request.class.getSimpleName() + " not valid for NQueensRequestHandler");
		}
		NQueensRequest nqueensRequest = (NQueensRequest)request;
		return nqueensRequest.getRequestId() + RequestSeparators.HEADER_SEPARATOR + NQueensUtil.solve(nqueensRequest.getN());
	}

	@Override
	public Request parse(String request) throws UnparsebleException {
		String[] split = request.split(RequestSeparators.HEADER_SEPARATOR);
		if(split == null || split.length != 3) {
			throw new UnparsebleException("Request: " + request + " cannot be parsed. No request Id.");
		}
		
		String requestStr = split[1];

		int requestId = 0;
		try{
			requestId = Integer.parseInt(requestStr);
		}catch(NumberFormatException e) {
			throw new UnparsebleException("Request id: " + requestStr + " is not a number.");
		}
		
		int a = 0;
		try{
			a = Integer.parseInt(split[2]);
		}catch(NumberFormatException e) {
			throw new UnparsebleException("Parameter: " + split[2] + " is not a number.");
		}
		
		return new NQueensRequest(requestId, a);
	}

}

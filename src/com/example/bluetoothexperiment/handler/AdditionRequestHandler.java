package com.example.bluetoothexperiment.handler;

import com.example.bluetoothexperiment.exception.IllegalRequestException;
import com.example.bluetoothexperiment.exception.UnparsebleException;
import com.example.bluetoothexperiment.requestresponse.RequestSeparators;
import com.example.bluetoothexperiment.requestresponse.request.AdditionRequest;
import com.example.bluetoothexperiment.requestresponse.request.Request;

public final class AdditionRequestHandler implements Handler {

	private static String header = "Addition";
	
	public static String getHeader() {
		return header;
	}
	
	public static String createRequest(int requestId, String message) {
		return header + RequestSeparators.HEADER_SEPARATOR + requestId + RequestSeparators.HEADER_SEPARATOR + message + "\n";
	}
	
	@Override
	public String process(Request request) throws UnparsebleException, IllegalRequestException {
		if(!(request instanceof AdditionRequest)) {
			throw new IllegalRequestException(Request.class.getSimpleName() + " not valid for AdditionHandler");
		}
		AdditionRequest additionRequest = (AdditionRequest)request;
		int result = additionRequest.getA() + additionRequest.getB();
		return additionRequest.getRequestId() + RequestSeparators.HEADER_SEPARATOR + result;
	}

	@Override
	public Request parse(String request) throws UnparsebleException {
		String[] split = request.split(RequestSeparators.HEADER_SEPARATOR);
		if(split == null || split.length != 3) {
			throw new UnparsebleException("Request: " + request + " cannot be parsed. No request Id.");
		}
		
		String requestStr = split[1];
		String numbersStr = split[2];
		
		int requestId = 0;
		try{
			requestId = Integer.parseInt(requestStr);
		}catch(NumberFormatException e) {
			throw new UnparsebleException("Request id: " + requestStr + " is not a number.");
		}
		
		String[] split1 = numbersStr.split(",");
		if(split1 == null || split1.length != 2) {
			throw new UnparsebleException("Request: " + request + " cannot be parsed.");
		}
		
		int a = 0;
		try{
			a = Integer.parseInt(split1[0]);
		}catch(NumberFormatException e) {
			throw new UnparsebleException("Parameter: " + split1[0] + " is not a number.");
		}
		
		int b = 0;
		try{
			b = Integer.parseInt(split1[1]);
		}catch(NumberFormatException e) {
			throw new UnparsebleException("Parameter: " + split1[1] + " is not a number.");
		}
		
		return new AdditionRequest(requestId, a, b);
	}

}

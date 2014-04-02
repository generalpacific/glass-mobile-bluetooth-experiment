package com.example.bluetoothexperiment.handler;

import com.example.bluetoothexperiment.exception.UnparsebleException;
import com.example.bluetoothexperiment.requestresponse.RequestSeparators;

public final class AdditionRequestHandler implements Handler {

	private int requestId;
	private int a;
	private int b;
	private static String header = "Addition";
	
	public static String getHeader() {
		return header;
	}
	
	public static String createRequest(int requestId, String message) {
		return header + RequestSeparators.HEADER_SEPARATOR + requestId + "#" + message;
	}
	
	@Override
	public String process(String request) throws UnparsebleException {
		String[] split = request.split(RequestSeparators.HEADER_SEPARATOR);
		parse(split[1]);
		int result = a + b;
		return requestId + "#" + Integer.toString(result);
	}

	@Override
	public void parse(String request) throws UnparsebleException {
		String[] split = request.split("#");
		if(split == null || split.length != 2) {
			throw new UnparsebleException("Request: " + request + " cannot be parsed. No request Id.");
		}
		try{
			requestId = Integer.parseInt(split[0]);
		}catch(NumberFormatException e) {
			throw new UnparsebleException("Request id: " + split[0] + " is not a number.");
		}
		
		String[] split1 = split[1].split(",");
		if(split1 == null || split1.length != 2) {
			throw new UnparsebleException("Request: " + request + " cannot be parsed.");
		}
		try{
			a = Integer.parseInt(split1[0]);
		}catch(NumberFormatException e) {
			throw new UnparsebleException("Parameter: " + split1[0] + " is not a number.");
		}
		try{
			b = Integer.parseInt(split1[1]);
		}catch(NumberFormatException e) {
			throw new UnparsebleException("Parameter: " + split1[1] + " is not a number.");
		}
	}

}

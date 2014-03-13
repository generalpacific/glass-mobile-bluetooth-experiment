package com.example.bluetoothexperiment.handler;

import com.example.bluetoothexperiment.exception.UnparsebleException;

public final class AdditionRequestHandler implements Handler {

	private int a;
	private int b;
	
	@Override
	public String process(String request) throws UnparsebleException {
		parse(request);
		int result = a + b;
		return Integer.toString(result);
	}

	@Override
	public void parse(String request) throws UnparsebleException {
		String[] split = request.split(",");
		if(split == null || split.length != 2) {
			throw new UnparsebleException("Request: " + request + " cannot be parsed.");
		}
		try{
			a = Integer.parseInt(split[0]);
		}catch(NumberFormatException e) {
			throw new UnparsebleException("Parameter: " + split[0] + " is not a number.");
		}
		try{
			b = Integer.parseInt(split[1]);
		}catch(NumberFormatException e) {
			throw new UnparsebleException("Parameter: " + split[1] + " is not a number.");
		}
	}

}

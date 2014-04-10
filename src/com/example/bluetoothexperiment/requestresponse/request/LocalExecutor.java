package com.example.bluetoothexperiment.requestresponse.request;

import com.example.bluetoothexperiment.exception.IllegalRequestException;
import com.example.bluetoothexperiment.exception.UnparsebleException;
import com.example.bluetoothexperiment.handler.Handler;
import com.example.bluetoothexperiment.requestresponse.RequestFactory;

/**
 * Executes the given requests locally.
 * 
 * @author prashant
 *
 */
public final class LocalExecutor {

	private LocalExecutor() {
		throw new IllegalStateException("Cannot instatiate this class");
	}
	
	public static String execute(String request) {
		request = request.trim();
		String response;
		try {
			Handler requestHandler = RequestFactory.getInstance().getRequestHandler(request);
			Request parse = requestHandler.parse(request);
			response = requestHandler.process(parse);
		} catch (UnparsebleException e) {
			response = "ERROR: " + request + " is not a valid request. Exception: " + e.getMessage();
		}catch (IllegalRequestException e) {
			response = "ERROR: " + request + " is not a valid request. Exception: " + e.getMessage();
		}
		return response;
	}
}

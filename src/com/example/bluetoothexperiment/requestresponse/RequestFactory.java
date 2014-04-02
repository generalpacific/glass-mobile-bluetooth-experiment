package com.example.bluetoothexperiment.requestresponse;

import com.example.bluetoothexperiment.exception.IllegalRequestException;
import com.example.bluetoothexperiment.handler.AdditionRequestHandler;
import com.example.bluetoothexperiment.handler.Handler;
import com.example.bluetoothexperiment.handler.MatrixMultiplicationHandler;

/**
 * Request factory to return the request handler object depending upon the type of data.
 * @author prashant
 *
 */
public final class RequestFactory {
	private static RequestFactory instance;
	
	private RequestFactory() {
		if(instance != null) {
			throw new IllegalStateException("Cannot instantiate singleton class twice");
		}
	}
	
	public static RequestFactory getInstance() {
		if(instance == null) {
			synchronized (RequestFactory.class) {
				if(instance == null) {
					instance = new RequestFactory();
				}
			}
		}
		return instance;
	}
	
	public Handler getRequestHandlerBasedOnHeader(String header) throws IllegalRequestException {
		switch(Requests.valueOf(header)){
		case ADDITION:
			return new AdditionRequestHandler();
		case MATRIX_MULTIPLICATION:
			return new MatrixMultiplicationHandler();
		default:
			throw new IllegalRequestException("Illegal request header:" + header);
		}
	}
	
	public Handler getRequestHandler(String data) throws IllegalRequestException {
		String[] split = data.split(RequestSeparators.HEADER_SEPARATOR);
		if(split == null || split.length == 0) {
			throw new IllegalRequestException("Cannot get header from the request");
		}
		return getRequestHandlerBasedOnHeader(split[0]);
	}
}

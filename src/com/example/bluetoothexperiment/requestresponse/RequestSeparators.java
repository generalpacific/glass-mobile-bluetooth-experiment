package com.example.bluetoothexperiment.requestresponse;

/**
 * Separators used to distinguish fields in the request.
 * @author prashant
 *
 */
public final class RequestSeparators {
	private RequestSeparators() {
		throw new IllegalStateException("Cannot instantiate the constants class");
	}
	
	public final static String HEADER_SEPARATOR = "$";
}

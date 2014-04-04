package com.example.bluetoothexperiment.requestresponse.request;

/**
 * General request interface which will have the parse method which will create 
 * the request from the string passed.
 * @author prashant
 *
 */
public interface Request {
	/**
	 * Returns the string of the current request.
	 * This method is complement to the Handle.createRequest method.
	 * @return
	 */
	public String getString(); 
}	

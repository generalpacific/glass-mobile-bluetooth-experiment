package com.example.bluetoothexperiment.handler;

import com.example.bluetoothexperiment.exception.IllegalRequestException;
import com.example.bluetoothexperiment.exception.UnparsebleException;
import com.example.bluetoothexperiment.requestresponse.request.Request;

/**
 * General Handler interface which will be inherited by all the respective handlers for 
 * different tasks like addition, matrix multiplication.
 * @author prashant
 *
 */
public interface Handler {
	public String process(Request request) throws UnparsebleException, IllegalRequestException;
	public Request parse(String request) throws UnparsebleException;
}

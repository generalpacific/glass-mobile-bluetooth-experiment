package com.example.bluetoothexperiment.requestresponse;

import com.example.bluetoothexperiment.handler.AdditionRequestHandler;
import com.example.bluetoothexperiment.handler.MatrixMultiplicationHandler;
import com.example.bluetoothexperiment.handler.NQueensRequestHandler;



/**
 * This is a class that stores the type of requests supported by the system.
 * @author prashant
 *
 */
public enum Requests {
	ADDITION(AdditionRequestHandler.getHeader()),
	MATRIX_MULTIPLICATION(MatrixMultiplicationHandler.getHeader()),
	NQUEENS(NQueensRequestHandler.getHeader());
	
	
	private String value;
	
	private Requests(String value) {
		this.value = value;
	}
	
	public static Requests getValue(String str) {
		Requests[] values = values();
		for (Requests requests : values) {
			if(requests.value.equals(str)) {
				return requests;
			}
		}
		return null;
	}
	
	public static String[] requestValues() {
		return new String[] {ADDITION.value, MATRIX_MULTIPLICATION.value, NQUEENS.value}; 
	}
	
	@Override
	public String toString() {
		return value;
	}
}

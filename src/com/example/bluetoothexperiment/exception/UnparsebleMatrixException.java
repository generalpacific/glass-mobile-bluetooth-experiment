package com.example.bluetoothexperiment.exception;

/**
 * Thrown when the matrix is unparseable
 * @author prashant
 *
 */
public final class UnparsebleMatrixException extends Exception {
	private static final long serialVersionUID = 2807631209353453263L;

	public UnparsebleMatrixException(String message) {
		super(message);
	}
	
	public UnparsebleMatrixException() {
		super();
	}
}

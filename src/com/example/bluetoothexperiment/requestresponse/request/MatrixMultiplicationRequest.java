package com.example.bluetoothexperiment.requestresponse.request;

import com.example.bluetoothexperiment.handler.MatrixMultiplicationHandler;
import com.example.bluetoothexperiment.matrix.Matrix;
import com.example.bluetoothexperiment.requestresponse.RequestSeparators;

/**
 * This is general MatrixMultiplicationRequest which contains fields like
 * matrix1, matrix2 etc.
 * @author prashant
 *
 */
public class MatrixMultiplicationRequest extends AbstractRequest {
	private Matrix mat1;
	private Matrix mat2;
	
	public MatrixMultiplicationRequest(int requestId, Matrix mat1, Matrix mat2) {
		this.requestId = requestId;
		this.mat1 = mat1;
		this.mat2 = mat2;
	}

	public Matrix getMat1() {
		return mat1;
	}

	public Matrix getMat2() {
		return mat2;
	}

	@Override
	public String getString() {
		return MatrixMultiplicationHandler.getHeader() + RequestSeparators.HEADER_SEPARATOR + requestId + 
				RequestSeparators.HEADER_SEPARATOR + mat1 +
				RequestSeparators.HEADER_SEPARATOR + mat2;
	}
	
	
}

package com.example.bluetoothexperiment.handler;

import com.example.bluetoothexperiment.exception.UnparsebleException;
import com.example.bluetoothexperiment.exception.UnparsebleMatrixException;
import com.example.bluetoothexperiment.matrix.Matrix;
import com.example.bluetoothexperiment.matrix.MatrixUtil;
import com.example.bluetoothexperiment.requestresponse.RequestSeparators;
import com.example.bluetoothexperiment.requestresponse.Requests;

public class MatrixMultiplicationHandler implements Handler {

	private int requestId;
	private Matrix mat1;
	private Matrix mat2;
	private static final String header = "Matrix";
	private static String divider = "#";
	
	public static String getHeader() {
		return header;
	}
	
	public static String createRequest(int requestId, int matrixSize){
		Matrix mat1 = null;
		try {
			mat1 = MatrixUtil.getRandomMatrix(matrixSize);
		} catch (UnparsebleMatrixException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Matrix mat2 = null;
		try {
			mat2 = MatrixUtil.getRandomMatrix(matrixSize);
		} catch (UnparsebleMatrixException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return header + RequestSeparators.HEADER_SEPARATOR +  requestId + divider + mat1 + divider + mat2 + "\n";
	}
	
	@Override
	public String process(String request) throws UnparsebleException {
		String[] split = request.split(RequestSeparators.HEADER_SEPARATOR);
		parse(split[1]);
		
		Matrix result = MatrixUtil.multiply(mat1, mat2);
		return requestId + "#" + result.toString();
	}

	@Override
	public void parse(String request) throws UnparsebleException {
		String[] split = request.split("#");
		if(split == null || split.length != 3) {
			throw new UnparsebleException("Request: " + request + " not parseable");
		}
		
		try {
			requestId = Integer.parseInt(split[0]);
		}catch(NumberFormatException ex) {
			throw new UnparsebleException("RequestId: " + split[0] + " is not a valid number.");
		}
		
		try {
			mat1 = new Matrix(split[1]);
		} catch (UnparsebleMatrixException e) {
			throw new UnparsebleException("Matrix: " + split[1] + " is not a valid matrix.");
		}
		
		try {
			mat2 = new Matrix(split[2]);
		} catch (UnparsebleMatrixException e) {
			throw new UnparsebleException("Matrix: " + split[2] + " is not a valid matrix.");
		}
	}

}

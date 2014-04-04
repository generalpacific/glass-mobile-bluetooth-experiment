package com.example.bluetoothexperiment.handler;

import com.example.bluetoothexperiment.exception.IllegalRequestException;
import com.example.bluetoothexperiment.exception.UnparsebleException;
import com.example.bluetoothexperiment.exception.UnparsebleMatrixException;
import com.example.bluetoothexperiment.matrix.Matrix;
import com.example.bluetoothexperiment.matrix.MatrixUtil;
import com.example.bluetoothexperiment.requestresponse.RequestSeparators;
import com.example.bluetoothexperiment.requestresponse.request.MatrixMultiplicationRequest;
import com.example.bluetoothexperiment.requestresponse.request.Request;

public class MatrixMultiplicationHandler implements Handler {

	private static final String header = "Matrix";
	
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
		return header + RequestSeparators.HEADER_SEPARATOR +  requestId + RequestSeparators.HEADER_SEPARATOR
				+ mat1 + RequestSeparators.HEADER_SEPARATOR + mat2 + "\n";
	}
	
	@Override
	public String process(Request request) throws UnparsebleException, IllegalRequestException {
		if(!(request instanceof MatrixMultiplicationRequest)) {
			throw new IllegalRequestException(Request.class.getSimpleName() + " not valid for MatrixHandler");
		}
		MatrixMultiplicationRequest matMulRequest = (MatrixMultiplicationRequest)request;
		return matMulRequest.getRequestId() + RequestSeparators.HEADER_SEPARATOR + MatrixUtil.multiply(matMulRequest.getMat1(), matMulRequest.getMat2());
	}

	@Override
	public Request parse(String request) throws UnparsebleException {
		String[] split = request.split(RequestSeparators.HEADER_SEPARATOR);
		if(split == null || split.length != 4) {
			throw new UnparsebleException("Request: " + request + " not parseable");
		}
		
		String requestIdStr = split[1];
		String mat1Str = split[2];
		String mat2Str = split[3];
		
		int requestId = 0;
		try {
			requestId = Integer.parseInt(requestIdStr);
		}catch(NumberFormatException ex) {
			throw new UnparsebleException("RequestId: " + split[0] + " is not a valid number.");
		}
		
		Matrix mat1 = null;
		try {
			mat1 = new Matrix(mat1Str);
		} catch (UnparsebleMatrixException e) {
			throw new UnparsebleException("Matrix: " + split[1] + " is not a valid matrix.");
		}
		
		Matrix mat2 = null;
		try {
			mat2 = new Matrix(mat2Str);
		} catch (UnparsebleMatrixException e) {
			throw new UnparsebleException("Matrix: " + split[2] + " is not a valid matrix.");
		}
		return new MatrixMultiplicationRequest(requestId, mat1, mat2);
	}

}

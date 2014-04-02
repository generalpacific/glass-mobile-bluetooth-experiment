package com.example.bluetoothexperiment.matrix;

import java.util.Random;

import com.example.bluetoothexperiment.exception.UnparsebleMatrixException;

public final class MatrixUtil {
	
	private MatrixUtil() {
		// util class so cannot instantiated
	}
	
	public static Matrix getRandomMatrix(int n) throws UnparsebleMatrixException {
		Random random = new Random();
		StringBuilder strBuilder = new StringBuilder();
		for(int i = 0; i < n; ++i) {
			for(int j = 0; j < n; ++j) {
				strBuilder.append(random.nextInt());
				strBuilder.append(",");
			}
			strBuilder.append(";");
		}
		return new Matrix(strBuilder.toString());
	}
	
	public static Matrix multiply(Matrix mat1, Matrix mat2) {
		if(mat1.n() != mat2.m()) {
			return null;
		}
		
		// TODO
		Matrix result = new Matrix(mat1.m());
		for(int i = 0; i < mat1.m(); ++i) {
			for(int j = 0; j < mat1.n(); ++j) {
				int temp = 0;
				for(int k = 0; k < mat2.n(); ++k) {
					temp += mat1.getIJ(i, j)*mat2.getIJ(j, k);
				}
				result.setIJ(i, j, temp);
			}
		}
		return result;
	}
}

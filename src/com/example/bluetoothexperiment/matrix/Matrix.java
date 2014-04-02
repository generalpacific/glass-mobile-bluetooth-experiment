package com.example.bluetoothexperiment.matrix;

import com.example.bluetoothexperiment.exception.UnparsebleMatrixException;

public final class Matrix {
	private int a[][];
	private int m = -1;
	private int n = -1;
	
	/**
	 * Initializes the empty matrix with dimensions nxn
	 * @param n
	 */
	public Matrix(int n) {
		this.m = n;
		this.n = n;
		a = new int[n][n];
	}
	
	public Matrix(String str) throws UnparsebleMatrixException {
		String[] rows = str.split(";");
		m = rows.length;
		String[] split = rows[0].split(",");
		n = split.length;
		a = new int[m][n];
		int i = 0, j = 0;
		for (String row : rows) {
			String[] elements = row.split(",");
			if(n != elements.length) {
				throw new UnparsebleMatrixException("Row lengths dont match");
			}
			j = 0;
			for (String element : elements) {
				try{
					a[i][j] = Integer.parseInt(element);
				}catch(NumberFormatException ex) {
					throw new UnparsebleMatrixException(element + " is not parseble");
				}
				++j;
			}
			++i;
		}
	}
	
	public int getIJ(int i, int j) {
		return a[i][j];
	}

	public void setIJ(int i, int j, int num) {
		a[i][j] = num;
	}
	
	public int m() {
		return m;
	}
	
	public int n() {
		return n;
	}
	
	@Override
	public String toString(){
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < m; ++i ){
			for(int j = 0; j< n ; ++j) {
				str.append(a[i][j]);
				str.append(",");
			}
			str.append(";");
		}
		return str.toString();
	}
}

package com.example.bluetoothexperiment.handler;

import com.example.bluetoothexperiment.exception.UnparsebleException;

public interface Handler {
	public String process(String request) throws UnparsebleException;
	public void parse(String request) throws UnparsebleException;
}

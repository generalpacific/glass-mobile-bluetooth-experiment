package com.example.bluetoothexperiment.requestresponse;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;

public final class RequestManager {
	private static RequestManager instance;
	private int currentRequestNumber = 0;
	@SuppressLint("UseSparseArrays")
	private Map<Integer,Long> requestIdStartTimeMap = new HashMap<Integer, Long>();
	@SuppressLint("UseSparseArrays")
	private Map<Integer,String> requestIdRequestMap = new HashMap<Integer, String>();
		
	private RequestManager() {
		//Singleton, hence  private constructor
		if(instance != null) {
			throw new IllegalStateException("Cannot instantiate RequestManager twice");
		}
	}
	
	public static RequestManager getInstance() {
		if(instance == null) {
			synchronized (RequestManager.class) {
				if(instance == null) {
					instance = new RequestManager();
				}
			}
		}
		return instance;
	}
	
	/**
	 * Adds the request and returns the request id for request.
	 * Sets the current time as start time for this request.
	 * @return requestId
	 */
	public synchronized int getNextRequestId() {
		currentRequestNumber++;
		return currentRequestNumber;
	}
	
	/**
	 * Adds the request and returns the request id for request.
	 * Sets the current time as start time for this request.
	 * @return requestId
	 */
	public synchronized void addRequestForRequestId(int requestId, String request) {
		requestIdRequestMap.put(requestId, request);
		requestIdStartTimeMap.put(requestId, System.currentTimeMillis());
	}
	
	/**
	 * Removes the request and returns the time required for execution of the request.
	 * @return time taken to execute the request
	 */
	public synchronized long removeRequest(Integer requestId) {
		if(!requestIdStartTimeMap.containsKey(requestId)) {
			throw new IllegalArgumentException("RequestId is not valid");
		}
		Long startTime = requestIdStartTimeMap.get(requestId);
		requestIdStartTimeMap.remove(requestId);
		requestIdRequestMap.remove(requestId);
		return System.currentTimeMillis() - startTime;
	}
	
	public synchronized String getRequest(int reqeustId) {
		return requestIdRequestMap.get(reqeustId);
	}
}

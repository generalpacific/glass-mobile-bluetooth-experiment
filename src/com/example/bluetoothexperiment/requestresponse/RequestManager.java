package com.example.bluetoothexperiment.requestresponse;

import android.annotation.SuppressLint;
import java.util.HashMap;
import java.util.Map;

public final class RequestManager {
	private static RequestManager instance;
	private int currentRequestNumber = 0;
	@SuppressLint("UseSparseArrays")
	private Map<Integer,Long> requestIdStartTimeMap = new HashMap<Integer, Long>();
		
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
	public int addRequest() {
		currentRequestNumber++;
		requestIdStartTimeMap.put(currentRequestNumber, System.currentTimeMillis());
		return currentRequestNumber;
	}
	
	/**
	 * Removes the request and returns the time required for execution of the request.
	 * @return time taken to execute the request
	 */
	public long removeRequest(Integer requestId) {
		if(!requestIdStartTimeMap.containsKey(requestId)) {
			throw new IllegalArgumentException("RequestId is not valid");
		}
		Long startTime = requestIdStartTimeMap.get(requestId);
		requestIdStartTimeMap.remove(requestId);
		return System.currentTimeMillis() - startTime;
	}
}

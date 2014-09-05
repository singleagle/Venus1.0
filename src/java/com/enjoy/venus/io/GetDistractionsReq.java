package com.enjoy.venus.io;

public class GetDistractionsReq{
	double longitude;
	double latitude;
	double distanceMile;
	
	String ipAddr;
	long fromTimeMs;
	long endTimeMs;
	int fromIndex;
	int maxItemPerPage;
	
	public GetDistractionsReq() {
		super();
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLocation(double longitude, double latitude) {
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public long getFromTimeMs() {
		return fromTimeMs;
	}

	public void setFromTimeMs(long fromTimeMs) {
		this.fromTimeMs = fromTimeMs;
	}

	public long getEndTimeMs() {
		return endTimeMs;
	}

	public void setEndTimeMs(long endTimeMs) {
		this.endTimeMs = endTimeMs;
	}

	public double getDistanceMile() {
		return distanceMile;
	}

	public void setDistanceMile(double distanceMile) {
		this.distanceMile = distanceMile;
	}

	public int getFromIndex() {
		return fromIndex;
	}

	public void setFromIndex(int fromIndex) {
		this.fromIndex = fromIndex;
	}

	public int getMaxItemPerPage() {
		return maxItemPerPage;
	}

	public void setMaxItemPerPage(int maxItemPerPage) {
		this.maxItemPerPage = maxItemPerPage;
	}
	
	
	
}

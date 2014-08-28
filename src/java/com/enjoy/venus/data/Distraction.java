package com.enjoy.venus.data;

import java.util.ArrayList;

/**
 * 消遣活动
 * @author tangwh
 *
 */
public class Distraction {
	private long creatTime;
	private long endTime;
	private final long creatUserId;
	private String description;
	private String destination;//?
	private String destCoord;
	private ArrayList<Long> teamMemberList;
	private ArrayList<String> tagList;
	private PayType payType;
	private int maxMemberCount;
	private int minMemberCount;
	
	
	public Distraction(long creatUserId) {
		this.creatUserId = creatUserId;
	}
	
	
	
	public long getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(long creatTime) {
		this.creatTime = creatTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getDestCoord() {
		return destCoord;
	}
	public void setDestCoord(String destCoord) {
		this.destCoord = destCoord;
	}
	
	public long getCreatUserId() {
		return creatUserId;
	}
	
	static public enum PayType{
		TYPE_ME(0),
		TYPE_AA(1),
		TYPE_YOU(2);
		
		private final int mValue;
		
		private PayType(int value){
			mValue = value;
		}

		public int value(){
			return mValue;
		}
		
		
	}
}

package com.enjoy.venus.data;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.bson.types.ObjectId;


/**
 * 消遣活动
 * @author tangwh
 *
 */
public class Distraction {
	private String _id;
	private String title;
	private long creatTime;
	private long endTime;
	private long creatUserId;
	private String description;
	private String destination;//?
	private double[] location = new double[2];
	private ArrayList<Long> teamMemberList;
	private ArrayList<String> tagList;
	private PayType payType;
	private int maxMemberCount;
	private int minMemberCount;
	
	
	public Distraction() {
	}
	
	
	public String getId() {
		return _id;
	}


	public void setId(String _id) {
		this._id = _id;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
	
	public long getCreatUserId() {
		return creatUserId;
	}
	
	public void setLocation(double longitude, double latitude){
		location[0] = longitude;
		location[1] = latitude;
	}
	
	public void setLocation(double[] location){
		this.location[0] = location[0];
		this.location[1] = location[1];
	}
	
	public double getLatitude() {
		return location[1];
	}


	public double getLongitude() {
		return location[0];
	}
	
	public double[] getLocation(){
		return location.clone();
	}

	public ArrayList<Long> getTeamMemberList() {
		return teamMemberList;
	}



	public void setTeamMemberList(ArrayList<Long> teamMemberList) {
		this.teamMemberList = teamMemberList;
	}



	public ArrayList<String> getTagList() {
		return tagList;
	}



	public void setTagList(ArrayList<String> tagList) {
		this.tagList = tagList;
	}



	public PayType getPayType() {
		return payType;
	}


	public void setPayType(int payType) {
		PayType [] types = PayType.values();
		if(payType > 0 && payType < types.length){
			this.payType =  types[payType];
		}else{
			this.payType = PayType.TYPE_AA;
		}
	}

	public void setPayType(PayType payType) {
		this.payType = payType;
	}



	public int getMaxMemberCount() {
		return maxMemberCount;
	}



	public void setMaxMemberCount(int maxMemberCount) {
		this.maxMemberCount = maxMemberCount;
	}



	public int getMinMemberCount() {
		return minMemberCount;
	}

	public void setMinMemberCount(int minMemberCount) {
		this.minMemberCount = minMemberCount;
	}



	public void setCreatUserId(long creatUserId) {
		this.creatUserId = creatUserId;
	}
}

package com.enjoy.venus.dto;

import java.util.ArrayList;
import java.util.List;

import com.enjoy.venus.db.record.DistractionRecord;

public class DistractionProfile {
	private String id;
	private String title;
	private long creatTime;
	private long startTime;
	private long creatUserId;
	private String description;
	private String origin; //始发地
	private int farawayMeters;
	private int requestMemberCount;
	private int partnerCount;
	private String imageId;

	public DistractionProfile(DistractionRecord record, int farawayMeters){
		id = record.getId();
		title = record.getTitle();
		creatTime = record.getCreatTime();
		startTime = record.getStartTime();
		creatUserId = record.getCreatUserId();
		description = record.getDescription();
		if(record.getOrigin() == null){
			origin = record.getDestination();
		}else{
			origin = record.getOrigin();
		}
		
		this.farawayMeters = farawayMeters;
		requestMemberCount = record.getMaxMemberCount();
		
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getCreatUserId() {
		return creatUserId;
	}

	public void setCreatUserId(long creatUserId) {
		this.creatUserId = creatUserId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public int getFarawayMeters() {
		return farawayMeters;
	}

	public void setFarawayMeters(int farawayMeters) {
		this.farawayMeters = farawayMeters;
	}


	public int getRequestMemberCount() {
		return requestMemberCount;
	}

	public void setRequestMemberCount(int requestMemberCount) {
		this.requestMemberCount = requestMemberCount;
	}

	public int getPartnerCount() {
		return partnerCount;
	}

	public void setPartnerCount(int partnerCount) {
		this.partnerCount = partnerCount;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	
	
	
	static public class PageDAProfile{
		private int startIndex;
		private int totalCount;
		private ArrayList<DistractionProfile> DAProfileList;
		
		
		public int getStartIndex() {
			return startIndex;
		}
		
		public void setStartIndex(int startIndex) {
			this.startIndex = startIndex;
		}
		
		public int getTotalCount() {
			return totalCount;
		}
		
		public void setTotalCount(int totalCount) {
			this.totalCount = totalCount;
		}
		
		public List<DistractionProfile> getDAProfileList() {
			return DAProfileList;
		}
		
		public void setDAProfileList(ArrayList<DistractionProfile> DAProfileList) {
			this.DAProfileList = DAProfileList;
		}
	}
	
}

package com.enjoy.venus.dto;

import java.util.ArrayList;
import java.util.List;

import com.enjoy.venus.db.record.DATypeRecord;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class DATypeDTO {
	
	private int typeId;
	private String mainTypeName;
	private String subTypeName;
	
	public DATypeDTO(DATypeRecord record, String mainTypeName){
		typeId = record.getTypeId();
		this.mainTypeName = mainTypeName;
		subTypeName = record.getTypeName();
	}
	
	public int getTypeId() {
		return typeId;
	}
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	
	public String getMainTypeName() {
		return mainTypeName;
	}
	
	public void setMainTypeName(String mainTypeName) {
		this.mainTypeName = mainTypeName;
	}
	
	public String getSubTypeName() {
		return subTypeName;
	}
	public void setSubTypeName(String subTypeName) {
		this.subTypeName = subTypeName;
	}

	public static class DATypeDTOList {
		private int mainTypeCount;
		private ArrayList<DATypeDTO> list;
		
		public DATypeDTOList(ArrayList<DATypeDTO> dtolist, int mainTypeCount){
			this.mainTypeCount = mainTypeCount;
			
			list = (ArrayList<DATypeDTO>) dtolist.clone();
		}
	}
	
		
	
	
}

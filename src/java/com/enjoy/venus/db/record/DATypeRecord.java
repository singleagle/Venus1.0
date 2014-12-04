package com.enjoy.venus.db.record;

public class DATypeRecord {
	public static final int SCOPE_PUBLIC = 0;
	public static final int SCOPE_PRIVATE = 0;
	
	private int typeId;
	private String typeName;
	private long createUIN;
	private int scope; //全局的
	
	
	public int getTypeId() {
		return typeId;
	}
	
	public int getSubTypeId(){
		return typeId & 0xffff;
	}
	
	public int getMainTypeId(){
		return (typeId >> 16);
	}
	
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	
	public void setTypeId(int mainTypeId, int subTypeId){
		typeId = (mainTypeId << 16) | (subTypeId & 0xffff);
	}

	
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public long getCreateUIN() {
		return createUIN;
	}
	public void setCreateUIN(long createUIN) {
		this.createUIN = createUIN;
	}
	public int getScope() {
		return scope;
	}
	public void setScope(int scope) {
		this.scope = scope;
	}
		
	
	
}

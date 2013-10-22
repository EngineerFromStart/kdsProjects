package com.parallellogic.test;

public class Comment {
	public long index;
	public String Name;
	public String Int1;
	public String Int2;
	public String Float1;
	public String Float2;
	public void setName(String name) {
		this.Name = name;
	}
	public void setInt1(int int1) {
		this.Int1 = String.valueOf(int1);
	}
	public String getInt1(){
		return Int1;
	}
	public void setInt2(int int2) {
		this.Int2 = String.valueOf(int2);
	}
	public void setFloat1(float float1) {
		this.Float1 = String.valueOf(float1);		
	}
	public void setFloat2(float float2) {
		this.Float2 = String.valueOf(float2);
	}
	public void setIndex(long index){
		this.index = index;
	}
}

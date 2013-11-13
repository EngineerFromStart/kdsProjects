package com.eventglace.app;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

public class LiveSqrdItems {
	private String title;
	private String photo;
	private String group;
	private String color;
	private String path;
	private String role;
	private String item;
	private Number x;
	private Number y;
	private Number z;
	private Number width;
	private Number height;
	private JSONObject states;
	private JSONObject body;
	private JSONObject permission;
	private JSONObject lock;
	private JSONObject due;
	private Date date;
	private Number[] geo = new Number[2];
	private Number[] box = new Number[2];
	private String[] tags;
	
	public LiveSqrdItems(){
		title = "yes";
	}
	public String createQuery(){
		String model = new String();
		model = "{";
		if (title != null && title.length() > 0)model += "\"title\":\""+title+"\",";
		if (photo != null && photo.length() > 0)model += "\"photo\":\""+photo+"\",";
		if (group != null && group.length() > 0)model += "\"group\":\""+group+"\",";
		if (color != null && color.length() > 0)model += "\"color\":\""+color+"\",";
		if (path != null && path.length() > 0)model += "\"path\":\""+path+"\",";
		if (role != null && role.length() > 0)model += "\"role\":\""+role+"\",";
		if (item != null && item.length() > 0)model += "\"item\":\""+item+"\",";
		if (x != null)model += "\"x\":"+x+",";
		if (y != null)model += "\"y\":"+y+",";
		if (z != null)model += "\"z\":"+z+",";
		if (width != null)model += "\"width\":"+width+",";
		if (height != null)model += "\"height\":"+height+",";
		if (states != null && states.length() > 0)model += "\"states\":"+states.toString()+",";
		if (body != null && body.length() > 0)model += "\"body\":"+body.toString()+",";
		if (permission != null && permission.length() > 0)model += "\"permission\":"+permission.toString()+",";
		if (lock != null && lock.length() > 0)model += "\"lock\":"+lock.toString()+",";
		if (due != null && due.length() > 0)model += "\"due\":"+due.toString()+",";
		if (geo != null && geo.length > 0)model += "\"geo\":"+Arrays.toString(geo)+",";
		if (box != null && box.length > 0)model += "\"box\":"+Arrays.toString(box)+",";
		if (tags != null && tags.length > 0)model += "\"tags\":"+Arrays.toString(tags)+",";
		if (date != null)model += "\"date\":\""+new SimpleDateFormat("yyyy-MM-dd hh:mm").format(date)+"\","; 
		model += "}";
		if (model.contains(",}")) model = model.replace(",}","}");
		String test = "{le\":\"Harlem Apartasdfsafmentsss\",\"permission\":[],\"box\":[10, 20],\"geo\":[],\"group\":\"event\"}";
		return model;
	}
	public void setTitle(String value){
		title = value;
	}
	public String getTitle(){
		return title;
	}
	public void setPhoto(String value){
		photo = value;
	}
	public String getPhoto(){
		return photo;
	}
	public void setGroup(String value){
		group = value;
	}
	public String getGroup(){
		return group;
	}
	public void setColor(String value){
		color = value;
	}
	public String getColor(){
		return color;
	}
	public void setPath(String value){
		path = value;
	}
	public String getPath(){
		return path;
	}
	public void setRole(String value){
		role = value;
	}
	public String getRole(){
		return role;
	}
	public void setItem(String value){
		item = value;
	}
	public String getItem(){
		return item;
	}
	public void setX(Number value){
		x = value;
	}
	public Number getX(){
		return x;
	}
	public void setY(Number value){
		y = value;
	}
	public Number getY(){
		return y;
	}
	public void setZ(Number value){
		z = value;
	}
	public Number getZ(){
		return z;
	}
	public void setWidth(Number value){
		width = value;
	}
	public Number getWidth(){
		return width;
	}
	public void setHeight(Number value){
		height = value;
	}
	public Number getHeight(){
		return height;
	}
	public void setStates(JSONObject value){
		states = value;
	}
	public JSONObject getStates(){
		return states;
	}
	public void setBody(JSONObject value){
		body = value;
	}
	public JSONObject getBody(){
		return body;
	}
	public void setPermission(String ID, String role){
		JSONObject value = new JSONObject();
		try {
			if (ID != null && role != null){
				value.put("ID", ID);
				value.put("role", role);
				permission = value;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public JSONObject getPermission(){
		return permission;
	}
	public void setLock(JSONObject value){
		lock = value;
	}
	public JSONObject getLock(){
		return lock;
	}
	public void setDue(Date date, Number period, Number freq){
		due = new JSONObject();
		try {
			due.put("date", new SimpleDateFormat("yyyy-MM-dd hh:mm").format(date).toString());
			due.put("period", period);
			due.put("freq", freq);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	public JSONObject getDue(){
		return due;
	}
	public void setDate(Date value){
		date = value;
	}
	public Date getDate(){
		return date;
	}
	public void setGeo(Number[] value){
		geo = value;
	}
	public Number[] getGeo(){
		return geo;
	}
	public void setBox(Number[] value){
		box = value;
	}
	public Number[] getBox(){
		return box;
	}
	public void setTag(String[] value){
		tags = value;
	}
	public String[] getTag(){
		return tags;
	}
}

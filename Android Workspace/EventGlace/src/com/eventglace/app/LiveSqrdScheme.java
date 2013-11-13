package com.eventglace.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class LiveSqrdScheme<K,V> extends HashMap <K,V>{
	/**
	 * 
	 */
	/*   use this format to use this class for hashmaps
	 public LiveSqrdItems(){
		item.put("title", new String());
		item.put("photo", new String());
		item.put("group", new String());
		item.put("color", new String());
		item.put("path", new String());
		item.put("x", Integer.valueOf((null)));
		item.put("y", Integer.valueOf((null)));
		item.put("z", Integer.valueOf((null)));
		item.put("width", Integer.valueOf((null)));
		item.put("height", Integer.valueOf((null)));
		item.put("states", new JSONObject());
		item.put("body", new JSONObject());
		item.put("role", new Object());//
		item.put("permission", new JSONObject());
		item.put("item", Integer.valueOf((null)));//
		item.put("lock", new JSONObject());
		item.put("date", new SimpleDateFormat("yyyy-MM-dd").format(0));//
		item.put("due", new HashMap<String,Object>());
		item.put("geo", geo);
		item.put("box", box);
		item.put("tags", new ArrayList<String>());
	}
	public void set(String key, Object value){
		if (item.containsKey(key)){
			if (item.get(key).getClass().getName().equals(value.getClass().getName())){
				item.put(key, value);
			}else{
				throw new IllegalStateException("The value type does not match");
			}
		}
	}
	public Object get(String key){
		return item.get(key);
	}	
	public String getQuery(){
		String model = "{\"tags\":"+Arrays.toString(geo)+",\"body\":{\"startTime\":\"12:55 PM\",\"startDate\":\"2013-11-15\",\"address\":\"187 Edgecasdfsdafombe Ave, New York 10030\",\"description\":\"this it tasdfsdfhe temp apartment i am currently living in\"},\"title\":\"Harlem Apartasdfsafmentsss\",\"permission\":[],\"box\":[10, 20],\"geo\":[],\"group\":\"event\"}";
		return model;
	}
	 */
	
	private static final long serialVersionUID = 1L;

	@Override
	public V put(K key, V value){
		if (!value.getClass().getName().equals(get(key).getClass().getName())){
			throw new IllegalStateException("Value type do not match");
		}
		else{
			return super.put(key, value);
		}
	}
	@Override
	public void putAll(Map<? extends K, ? extends V> collection) {
		if(containsKey(collection.keySet())){
			if (!get(collection.keySet()).getClass().getName().equals(
					collection.get(collection.keySet()).getClass().getName())
				){
				super.putAll(collection);
			}else{
				throw new IllegalStateException("Value Types done match");
			}
			
		}else{
			throw new IllegalStateException("The keys dont match");
		}
	}
	@Override
	public V remove(Object key) {
	    return null;// doesn't remove anything
	}
}

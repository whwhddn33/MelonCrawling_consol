package DAO;

import java.util.HashMap;

public class Session {
	private static HashMap<String, Integer> session = new HashMap<>();
	
	public static void put(String key,Integer i) {
		session.put(key, i);
	}
	public static Integer get(String key) {
		return session.get(key);
	}
}


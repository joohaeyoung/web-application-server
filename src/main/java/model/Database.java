package model;

import java.util.HashMap;
import java.util.Map;


public class Database {
	
	
	private static Map<String, User> users = new HashMap<String, User>();
	
	public static void addUser(User user) {
		
		users.put(user.getUserId(), user); //userId를 키값으로한다 user 를 한다. 
	
	}
	
	public static User getUsers( String userId ) {
		return users.get(userId);
	}
}

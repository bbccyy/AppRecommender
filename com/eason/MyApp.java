package com.eason;

import com.mongodb.MongoClient;

public class MyApp {

	public static void main(String[] args) {
		
		MongoClient mongoClient = new MongoClient("localhost", 27017);
		
		DataService ds = new DataService(mongoClient);
		
		System.out.println(ds.retrieve_app_info("score","5").size());
		System.out.println(ds.retrieve_user_download_history().size());

	}

}

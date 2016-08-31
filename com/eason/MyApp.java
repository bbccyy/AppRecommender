package com.eason;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MyApp {

	public static void main(String[] args) {
		
		MongoClient clt = new MongoClient("localhost", 27017);	
//		DataService ds = new DataService(clt);
		
		MongoDatabase db = clt.getDatabase("appstore");
		MongoCollection<Document> app_info = db.getCollection("app_info");
		
		System.out.println();
//		System.out.println(ds.retrieve_user_download_history().size());
		
		//Recommender rd = new Recommender();
		
		//rd.start(3);
		
		
		

	}

}

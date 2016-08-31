package com.eason;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.mongodb.MongoClient;

public class Recommender {
	
	private MongoClient mongoClient;
	private DataService ds;
	private static volatile ConcurrentLinkedQueue<String> appList = 
			new ConcurrentLinkedQueue<String>();
	
	public Recommender(){
		
		this.mongoClient = new MongoClient("localhost", 27017); //default host name and port number
		
		this.ds = new DataService(mongoClient);
		
	}
	
	public void start( int numberOfThreads ){
		
		Map<String, String> app_info = ds.retrieve_app_info();
		Map<Integer, Set<String>> user_download_history = ds.retrieve_user_download_history();
		appList.addAll(app_info.keySet());
		
		for(int idx=0; idx<numberOfThreads; idx++){
			Calculator work = new Calculator(appList, user_download_history, ds);
			Thread th = new Thread(work);
			th.start();
		}
		
	}
	
}

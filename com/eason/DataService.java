package com.eason;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.*;

public class DataService {
	
	MongoClient client;
	MongoDatabase db;
	MongoCollection<Document> user_download_history;
	MongoCollection<Document> app_info;
	
	public DataService(MongoClient client){
		this.client = client;
		this.db = client.getDatabase("appstore");
		this.user_download_history = db.getCollection("user_download_history");
		this.app_info = db.getCollection("app_info");
	}
	
	public Map<String, String> retrieve_app_info(){
		Map<String, String> res = new HashMap<String, String>();
		FindIterable<Document> iterable = app_info.find();
		for(Document doc : iterable){
			String app_id = (String) doc.get("app_id");
			String title = (String) doc.get("title");
			res.put(app_id, title);
		}
		return res;
	}
	
	//overload method, only retrieve application that has score >= value
	public Map<String, String> retrieve_app_info(String score, String value){
		Map<String, String> res = new HashMap<String, String>();
		FindIterable<Document> iterable = app_info.find(or(eq(score,value),gt(score,value)));
		for(Document doc : iterable){
			String app_id = (String) doc.get("app_id");
			String title = (String) doc.get("title");
			res.put(app_id, title);
		}
		return res;
	}
	
	@SuppressWarnings("unchecked")
	public Map<Integer, Set<String>> retrieve_user_download_history(){
		Map<Integer, Set<String>> res = new HashMap<Integer, Set<String>>();
		FindIterable<Document> iterable = user_download_history.find();
		for(Document doc : iterable){
			Set<String> download_history = new HashSet<String>();
			download_history.addAll((List<String>) doc.get("download_history"));
			int user_id = (Integer) doc.get("user_id");
			res.put(user_id, download_history);
		}
		return res;
	}
	
	
	// new Document("app_id", app_id),  new Document("$set", new Document("top_5_app", top_5))
	// app_id --> String,   top_5 --> List<String>
	public void update_app_info(Document filterd, Document update_info){
		app_info.updateOne(filterd, update_info);
	}
	

}

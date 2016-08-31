package com.eason;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bson.Document;

public class Calculator implements Runnable{
	
	private volatile ConcurrentLinkedQueue<String> appList;
	private Map<Integer, Set<String>> user_download_history;
	private DataService ds;
	
	public Calculator(ConcurrentLinkedQueue<String> apps, 
			Map<Integer, Set<String>> user_download_history,
			DataService ds){
		this.appList = apps;
		this.user_download_history = user_download_history;
		this.ds = ds;
	}

	public void run() {
		
		while(!appList.isEmpty()){
			String app_id = appList.poll();
			if(app_id==null) return;
			System.out.println(Thread.currentThread().getName() + " compute app: " + app_id);
			calculate_top_5(app_id);
		}
		System.out.println(Thread.currentThread().getName() + " work done!");
	}
	
	private void calculate_top_5(String app_id){
		
		//compute a table with all applications and their associated cosine similarities
		Map<String, Float> similarity = new HashMap<String, Float>();
		Set<String> appSet = new HashSet<String>();
		appSet.add(app_id);
		for(Set<String> history : user_download_history.values()){
			if(!history.contains(app_id)) continue;
			float sim = Helper.cosine_similarity(appSet, history);
			for(String app : history){
				similarity.compute(app, (key, oldVal)->oldVal==null?sim:oldVal+sim);
			}
		}
		
		//check if absent
		if(!similarity.containsKey(app_id)) return;
		
		//sort using min_heap
		similarity.remove(app_id);
		PriorityQueue<Entry<String, Float>> que = new PriorityQueue<>((a,b)->
											{	float tmp = a.getValue()- b.getValue();
												if(tmp<0) return -1;
												else if(tmp==0) return 0;
												else return 1;
											});
		for(Entry<String, Float> entry : similarity.entrySet()){
			que.offer(entry);
			if(que.size()==6){
				que.poll();
			}
		}
		
		//get top 5 app_id
		List<String> top_5 = new ArrayList<>();
		while(!que.isEmpty()){
			top_5.add(0, que.poll().getKey());
		}
		
		// app_id --> String,   top_5 --> List<String>
		synchronized(ds){
			Document filterd = new Document("app_id", app_id);
			Document update_info = new Document("$set", new Document("top_5_app", top_5));
			ds.update_app_info(filterd, update_info);
		}
		
	}

}

package com.eason;

import java.util.Set;

public class Helper {
	
	public static float cosine_similarity(Set<String> set1, Set<String> set2){
		int match_count = count_match(set1, set2);
		return (float) (match_count / Math.sqrt(set1.size() * set2.size()));
	}
	
	private static int count_match(Set<String> set1, Set<String> set2){
		int count = 0;
		for(String app : set1){
			if(set2.contains(app))
				count++;
		}
		return count;
	}
}

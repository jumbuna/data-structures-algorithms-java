package com.jumbuna.algs;

import com.jumbuna.ds.Vector;

public class Zalgorithm {
	public static Vector<Integer> subStringSearch(String text, String pattern, char separator) {
		String concated = pattern + separator + text;
		Vector<Integer> zArray = new Vector<>();
		Vector<Integer> vec = new Vector<>();
		//(m+n) time Z-array construction
		zArray.insert(0);
		int i = 0;
		for(int j = 1; j < concated.length(); j++) {
			int startIndex = j + i;
			while(concated.charAt(startIndex) == concated.charAt(i) && startIndex < concated.length()) {
				++i;
				++startIndex;
			}
			zArray.insert(i);
			if(i == pattern.length()) {
				vec.insert(zArray.size()-2-pattern.length());
			}
			if(i <= 1) {
				i = 0;
			}else {
				int k = 1;
				for(; (zArray.elementAt(k) + j + k) < startIndex && k < i; k++) {
					zArray.insert(zArray.elementAt(k));
					++j;
				}
				if(k == i) {
					i = 0;
					--j;
				}else {
					i = zArray.elementAt(k);
				}
			}
		}
		
		return vec;
	}
	
	public static void main(String[] args) {
		Vector<Integer> vec = Zalgorithm.subStringSearch("abacaccdaabaabad", "aba", '&');
		for(int i = 0; i < vec.size(); i++) {
			System.out.println(vec.elementAt(i));
		}
	}
}
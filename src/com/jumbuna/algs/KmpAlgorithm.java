package com.jumbuna.algs;

import com.jumbuna.ds.Vector;

public class KmpAlgorithm {
	public static void constructKmpTable(String pattern, Vector<Integer> array) {
		array.clear();
		array.insert(0);
		int i = 0;
		for(int j = 1; j< pattern.length(); j++) {
			if(pattern.charAt(j) == pattern.charAt(i)) {
				array.insert(++i);
			}else {
				while(i > 0) {
					i = array.elementAt(i-1);
					if(pattern.charAt(i) == pattern.charAt(j)) {
						array.insert(++i);
						break;
					}
				}
				if(i == 0) {
					array.insert(pattern.charAt(i) == pattern.charAt(j) ? ++i : 0);
//					pattern.charAt(i) == pattern.charAt(j) ? array.insert(++i) : array.insert(i);
				}
			}
		}
	}
	
	public static int subStringSearch(String text, String pattern, Vector<Integer> array) {
		int arrayIndex = 0;
		for(int i = 0; i < text.length(); i++) {
			if(pattern.charAt(arrayIndex) == text.charAt(i)) {
				++arrayIndex;
				if(arrayIndex == pattern.length()) {
					return i-arrayIndex+1;
				}
			}else {
				arrayIndex = Math.max(0, arrayIndex-1);
				if(pattern.charAt(arrayIndex) == text.charAt(i)) {
					++arrayIndex;
				}
			}
		}
		return -1;
	}
	
	public static int subStringSearch(String text, String pattern) {
		Vector<Integer> array = new Vector<>();
		constructKmpTable(pattern, array);
		return subStringSearch(text, pattern, array);
	}
	
	public static void main(String[] args) {
		System.out.println(KmpAlgorithm.subStringSearch("jacobo", "bo"));
	}
}
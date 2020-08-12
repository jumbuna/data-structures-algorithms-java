//
// Trie.java
// Created by Jacob Bumbuna Wangoni on 11/08/2020.
package com.jumbuna.ds;

import java.util.*;
import com.jumbuna.algs.QuickSort;

public class Trie {
	private class TrieNode {
		HashMap<Character,TrieNode> map = new HashMap<>();
		boolean eow = false;
		public void delete() {
			Vector<Character> keys = map.keys();
			for(int i = 0; i < keys.size(); i++) {
				map.get(keys.elementAt(i)).delete();
				map.remove(keys.elementAt(i));
			}
			if(eow) {
				--wordCount;
			}
		}
	}
	TrieNode root = new TrieNode();
	int wordCount = 0;
	public void insert(String word) {
		insert(word, 0, root);
		++wordCount;
	}
	
	public void removeWord(String word) {
		removeWord(word, 0, root);
	}
	private void insert(String word, int charIndex, TrieNode candidate) {
		if(charIndex == word.length()) {
			candidate.eow = true;
		} else {
			char c = word.charAt(charIndex);
			if(candidate.map.contains(c)) {
				insert(word, ++charIndex, candidate.map.get(c));
			} else {
				TrieNode node = new TrieNode();
				candidate.map.insert(c, node);
				insert(word, ++charIndex, node);
			}
		}
	}
	
	private boolean trackDownEow(String word, int charIndex, TrieNode candidate, boolean wordSearch) {
		if(charIndex == word.length()) {
			if(wordSearch) return candidate.eow;
			else return true;
		}
		if(candidate.map.contains(word.charAt(charIndex))) {
			return trackDownEow(word, ++charIndex, candidate.map.get(word.charAt(charIndex-1)), wordSearch);
		}
		return false;
	}
	
	public void removeWordWithPrefix(String prefix) {
		removePrefixWordsWithPrefix(prefix, 0, root);
	}
	
	private void removeWord(String word, int charIndex, TrieNode candidate) {
		if(charIndex == word.length()) {
			candidate.eow = false;
			--wordCount;
			return;
		}
		if(candidate.map.contains(word.charAt(charIndex))) {
			char c = word.charAt(charIndex);
			TrieNode temp = candidate.map.get(c);
			removeWord(word, ++charIndex, temp);
			if(!temp.eow && temp.map.size() == 0) {
				candidate.map.remove(c);
			}
		}
	}
	
	public Vector<String> wordsWithPrefix(String prefix) {
		Vector<String> vector = new Vector<>();
		wordsWithPrefix(prefix, 0, root, vector);
		return vector;
	}
	
	private void wordsWithPrefix(String prefix, int charIndex, TrieNode candidate, Vector<String> vector) {
		if(charIndex == prefix.length()) {
			constructWordFromPrefix(prefix, vector, candidate);
			return;
		}
		if(candidate.map.contains(prefix.charAt(charIndex))) {
			wordsWithPrefix(prefix, ++charIndex, candidate.map.get(prefix.charAt(charIndex-1)), vector);
		}
	}
	
	private void constructWordFromPrefix(String prefix, Vector<String> vector, TrieNode candidate) {
		if(candidate.eow) {
			vector.insert(prefix);
		}
		Vector<Character> keys = candidate.map.keys();
		String temp;
		for(int i = 0; i < keys.size(); i++) {
			temp = prefix + keys.elementAt(i);
			constructWordFromPrefix(temp, vector, candidate.map.get(keys.elementAt(i)));
		}
	}
	
	public Vector<String> typoCorrection(String typo, int accuracy) {
		Vector<String> vec = new Vector<>();
		int end = typo.length()-1;
		while(typo.length() * accuracy/100.0 < end && vec.size() == 0) {
			wordsWithPrefix(typo.substring(0, --end), 0, root, vec);
		}
		return vec;
	}
	
	private void removePrefixWordsWithPrefix(String prefix, int charIndex, TrieNode candidate) {
		if(charIndex == prefix.length()) {
			candidate.delete();
			return;
		}
		if(candidate.map.contains(prefix.charAt(charIndex))) {
			char c = prefix.charAt(charIndex);
			TrieNode temp = candidate.map.get(c);
			removePrefixWordsWithPrefix(prefix, ++charIndex, temp);
			if(temp.map.size() == 0 && !temp.eow) {
				candidate.map.remove(c);
			}
		}
	}
	
	public boolean containsWord(String word) {
		return trackDownEow(word, 0, root, true);
	}
	
	public boolean containsPrefix(String prefix) {
		return trackDownEow(prefix, 0, root, false);
	}
	
	public int size() {
		return wordCount;
	}
	
	public static void main(String[] args) {
		Trie trie = new Trie();
		for(String i: args) {
			trie.insert(i);
		}
//		Scanner scan = new Scanner(System.in);
//		System.out.println("insert into dictionary: ");
//		System.out.print(">>> ");
//		while(scan.hasNext()) {
//			trie.insert(scan.next());
//		}
		Scanner scan = new Scanner(System.in);
		System.out.println("Typos: (typo, approx %) ");
		Vector<String> vec;
		while(true) {
			System.out.print(">>> ");
			System.out.println("possible match (s): " + trie.typoCorrection(scan.next(), scan.nextInt()));
		}
//		System.out.println("search: ");
//		while(true) {
//			System.out.print(">>> ");
//			System.out.println(trie.containsWord(scan.next()));
//		}
	}
}
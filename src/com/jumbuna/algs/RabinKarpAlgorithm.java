package com.jumbuna.algs;

public class RabinKarpAlgorithm {
	public static int rabinKarpAlgorithm (String text, String pattern) {
		int patternLength = pattern.length();
		long patternHash = substringHash(pattern, patternLength);
		long hash = 0;
		int len = text.length() - pattern.length();
		for(int i = 0; i <= len; i++) {
			if(i == 0) {
				hash = substringHash(text, patternLength);
			} else {
				hash = runningHash(hash, text.charAt(i-1), text.charAt(i+patternLength-1), patternLength);
			}
			if (hash == patternHash) {
			int j = 0;
			for(; j < patternLength; j++) {
				if(text.charAt(i+j) != pattern.charAt(j)) {
					break;
				}
			}
			if(j == patternLength) {
				return i;
			}
		}
		}	
		return -1;
	}

	public static long runningHash(long hash, char oldChar, char newChar, int patternLength) {
		hash -= oldChar;
		hash /= 3;
		hash += (newChar * Math.pow(3, patternLength-1));
		return hash;
	}

	public static long substringHash(String string, int patternLength) {
		long hash = 0;
		for(int i = 0; i < patternLength; i++) {
			hash += (string.charAt(i) * Math.pow(3, i));
		}
		return hash;
	}

	public static void main(String[] args) {
		System.out.println(RabinKarpAlgorithm.rabinKarpAlgorithm(args[0], args[1]));
	}
}
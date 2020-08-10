import java.util.*;

//
// FenwickTree.java
// Created by Jacob Bumbuna Wangoni on 10/08/2020.

public class FenwickTree {
	private int[] fenwickArray;
	private int[] valuesArray;
	FenwickTree(int[] array) {
		fenwickArray = new int[array.length+1];
		valuesArray = new int[array.length];
		System.arraycopy(array, 0, fenwickArray, 1, array.length);
		System.arraycopy(array, 0, valuesArray, 0, array.length);
		for(int i = 1; i< fenwickArray.length; i++) {
			int parent = i + calculateLsb(i);
			if(parent < fenwickArray.length) {
				fenwickArray[parent] += fenwickArray[i];
			}
		}
	}
	
	private int calculateLsb(int i) {
		return i & -i;
	}
	
	private int positionSum(int i) {
		int sum = 0;
		while(i > 0) {
			sum += fenwickArray[i];
			i -= calculateLsb(i);
		}
		return sum;
	}
	
	public int rangeSum(int i, int j) {
		return positionSum(j) - positionSum(i-1);
	}
	
	public void update(int i, int j) {
		int difference = j - valuesArray[i];
		valuesArray[i] = j;
		++i;
		while(true) {
			fenwickArray[i] += difference;
			i += calculateLsb(i);
			if(i >= fenwickArray.length) break;
		}
	}
	
	public static void main(String[] args) {
		int[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
		FenwickTree tree = new FenwickTree(array);
		Scanner scan = new Scanner(System.in);
//		tree.update(0, 10);
		while(true) {
			System.out.print("range: ");
			int y = scan.nextInt();
			int x = scan.nextInt();
			System.out.println(">>> " +tree.rangeSum(y, x));
		}
	}
	
}
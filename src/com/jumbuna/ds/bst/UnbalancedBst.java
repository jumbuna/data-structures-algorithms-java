//
// UnbalancedBst.java
// Created by Jacob Bumbuna Wangoni on 07/08/2020.
package com.jumbuna.ds.bst;

import java.util.*;

public class UnbalancedBst <T extends Comparable<T>> extends BinarySearchTree<T> {
	
	UnbalancedBst() {
		super(null);
	}
	
	private void insert(BinarySearchTreeNode<T> candidate, BinarySearchTreeNode<T> parent, T element) {
		if(candidate == null && parent == null) {
			root = new BinarySearchTreeNode<T>(element, null);
			return;
		}
		if(candidate == null) {
			if(parent.element.compareTo(element) < 0) {
				parent.leftChild = new BinarySearchTreeNode<T>(element, parent);
			}else {
				parent.rightChild = new BinarySearchTreeNode<T>(element, parent);
			}
			return;
		}
		
		if(candidate.element.compareTo(element) > 0) {
			insert(candidate.leftChild, candidate, element);
		}else {
			insert(candidate.rightChild, candidate, element);
		}
	}
	
	public void insert(T element) {
		insert(root, root, element);
		++nodeCount;
	}
	
	public void remove(T element) {
		if(contains(element)) {
			remove(root, element);
			--nodeCount;
		}
	}
	
	public void remove(BinarySearchTreeNode<T> candidate, T element) {
		if(candidate == null) {
			return;
		}
		
		if(candidate.element.compareTo(element) != 0) {
			if(candidate.element.compareTo(element) > 0) {
				remove(candidate.leftChild, element);
			}else {
				remove(candidate.rightChild, element);
			}
			return;
		}
		
		BinarySearchTreeNode<T> parent = candidate.parent;
		if(candidate.rightChild == null) {
			if(parent != null) {
				if(parent.element.compareTo(element) < 0) {
					parent.leftChild = candidate.leftChild;
				}else {
					parent.rightChild = candidate.leftChild;
				}
			}else {
				root = candidate.leftChild;
			}
			if(candidate.leftChild != null) {
				candidate.leftChild.parent = parent;
			}
		}else if(candidate.leftChild == null) {
			if(parent != null) {
				if(parent.element.compareTo(element) < 0) {
					parent.leftChild = candidate.rightChild;
				}else {
					parent.rightChild = candidate.rightChild;
				}
			}else {
				root = candidate.rightChild;
			}
			if(candidate.rightChild != null) {
				candidate.rightChild.parent = parent;
			}
		} else {
			T successor = BstUtils.preOrderSuccessor(candidate.leftChild, getSentinel());
			candidate.element = successor;
			remove(candidate.leftChild, successor);
		}

	}

	public static void main(String[] args) {
		UnbalancedBst<Integer> bst = new UnbalancedBst<>();
		bst.insert(10);
		bst.insert(1);
		bst.insert(11);
		bst.insert(12);
		bst.insert(5);
		bst.insert(4);
		bst.insert(14);
		
		Scanner scan = new Scanner(System.in);
		System.out.println("enter search values: ");
		while(true) {
			System.out.print(">>> ");
			System.out.println(bst.contains(scan.nextInt()));
		}
	}
	
}
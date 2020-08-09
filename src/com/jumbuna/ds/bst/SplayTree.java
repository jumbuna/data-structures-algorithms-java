//
// SplayTree.java
// Created by Jacob Bumbuna Wangoni on 09/08/2020.

package com.jumbuna.ds.bst;

import java.util.*;

public class SplayTree <T extends Comparable<T>> extends BinarySearchTree<T> {
	SplayTree() {
		super(null);
	}
	
	public void insert(T element) {
		insert(root, root, element);
		++nodeCount;
	}
	
	public void remove(T element) {
		if(contains(element)) {
			//split and join
			BinarySearchTreeNode<T> temp = root.rightChild;
			if(root.leftChild != null) {
				root = root.leftChild;
				splay(preOrderSuccessorNode(root));
				root.rightChild = temp;
				temp.parent = root;
			} else {
				root = temp;
				temp.parent = null;
			}
			--nodeCount;
		}
	}
	
	public boolean contains(T element) {
		BinarySearchTreeNode<T> temp = root;
		int compare;
		while(temp != null) {
			compare = temp.element.compareTo(element);
			if(compare == 0) {
				splay(temp);
				return true;
			}
			if(compare > 0) {
				temp = temp.leftChild;
			}else {
				temp = temp.rightChild;
			}
		}
		return false;
	}
	
	private BinarySearchTreeNode<T> preOrderSuccessorNode(BinarySearchTreeNode<T> leftChild) {
		while(leftChild.rightChild != null) {
			leftChild = leftChild.rightChild;
		}
		return leftChild;
	}
	
	private void insert(BinarySearchTreeNode<T> candidate, BinarySearchTreeNode<T> parent, T element) {
		if(candidate == null && parent == null) {
			root = new BinarySearchTreeNode<T>(element, parent);
			return;
		}
		
		if(candidate == null) {
			candidate = new BinarySearchTreeNode<T>(element, parent);
			if(parent.element.compareTo(element) > 0) {
				parent.leftChild = candidate;
			}else {
				parent.rightChild = candidate;
			}
			splay(candidate);
			return;
		}
		
		if(candidate.element.compareTo(element) > 0) {
			insert(candidate.leftChild, candidate, element);
		}else {
			insert(candidate.rightChild, candidate, element);
		}	
	}
	
	private void splay(BinarySearchTreeNode<T> candidate) {
		while(root != candidate) {
			zigCase(candidate);
			zigZagCase(candidate);
			zigZigCase(candidate);
		}
	}
	
	private void zigCase(BinarySearchTreeNode<T> candidate) {
		BinarySearchTreeNode<T> parent = candidate.parent;
		if(parent == root) {
			if(candidate == parent.leftChild) {
				BstUtils.leftLeftCase(candidate, this);
			}else {
				BstUtils.rightRightCase(candidate, this);
			}
		}
	}
	
	private void zigZagCase(BinarySearchTreeNode<T> candidate) {
		BinarySearchTreeNode<T> parent = candidate.parent;
		if(candidate != root && parent != root) {
			BinarySearchTreeNode<T> grandParent = parent.parent;
			if(parent == grandParent.leftChild && candidate == parent.rightChild) {
				BstUtils.rightRightCase(candidate, this);
			} else if(parent == grandParent.rightChild && candidate == parent.leftChild) {
				BstUtils.leftLeftCase(candidate, this);
			}
		}
	}
	
	private void zigZigCase(BinarySearchTreeNode<T> candidate) {
		BinarySearchTreeNode<T> parent = candidate.parent;
		if(candidate != root && parent != root) {
			BinarySearchTreeNode<T> grandParent = parent.parent;
			if(parent == grandParent.leftChild && candidate == parent.leftChild) {
				BstUtils.leftLeftCase(parent, this);
				BstUtils.leftLeftCase(candidate, this);
			} else if(parent == grandParent.rightChild && candidate == parent.rightChild) {
				BstUtils.rightRightCase(parent, this);
				BstUtils.rightRightCase(candidate, this);
			}
		}
	}
	
	public static void main(String[] args) {
		SplayTree<Integer> tree = new SplayTree<>();
		Scanner scan = new Scanner(System.in);
		while(true) {
			System.out.print(">>> ");
			tree.insert(scan.nextInt());
			System.out.println(tree.root.element);
		}
	}
}
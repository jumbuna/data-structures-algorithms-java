//
// BinarySearchTree.java
// Created by Jacob Bumbuna Wangoni on 07/08/2020.
package com.jumbuna.ds.bst;

//base class for all Bsts

import com.jumbuna.ds.Vector;

public abstract  class BinarySearchTree <T extends Comparable<T>> {
	public BinarySearchTreeNode<T> root;
	private BinarySearchTreeNode<T> sentinel;
	public int nodeCount;

	public abstract void insert(T element);
	public abstract void remove(T element);

	BinarySearchTree(BinarySearchTreeNode<T> sentinel) {
		this.sentinel = sentinel;
	}
	
	public int size() {
		return nodeCount;
	}
	
	public boolean contains(T element) {
		if(nodeCount == 0) {
			return false;
		}
		BinarySearchTreeNode<T> temp = root;
		while(temp != sentinel) {
			if(temp.element.equals(element)) {
				return true;
			}
			temp = temp.element.compareTo(element) > 0 ? temp.leftChild : temp.rightChild;
		}
		
		return false;
	}

	public Vector<T> treeTraversal(TraversalOrder order) {
		Vector<T> vector = new Vector<>(size());
		BstUtils.TreeTraversal(order, sentinel, vector, root);
		return vector;
	}

	public BinarySearchTreeNode<T> getSentinel() {
		return sentinel;
	}

	public void clear() {
		BstUtils.clearTree(this);
		nodeCount = 0;
		root = sentinel;
	}

}
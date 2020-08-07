//
// BinarySearchTreeNode.java
// Created by Jacob Bumbuna Wangoni on 07/08/2020.
package com.jumbuna.ds.bst;

//base class for all Bsts

public class BinarySearchTreeNode <T extends Comparable<T>> {
	public T element;
	public BinarySearchTreeNode<T> leftChild;
	public BinarySearchTreeNode<T> rightChild;
	public BinarySearchTreeNode<T> parent;
	
	BinarySearchTreeNode(T element, BinarySearchTreeNode<T> parent) {
		this.element = element;
		this.parent = parent;
		leftChild = rightChild = null;
	}
	
	BinarySearchTreeNode(T element, BinarySearchTreeNode<T> parent, BinarySearchTreeNode<T> sentinel) {
		this.element = element;
		this.parent = parent;
		leftChild = rightChild = sentinel;
	}
	
}
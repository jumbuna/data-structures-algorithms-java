//
// AAtree.java
// Created by Jacob Bumbuna Wangoni on 09/08/2020.

package com.jumbuna.ds.bst;

import com.jumbuna.ds.Queue;
import java.util.*;

public class AAtree <T extends Comparable<T>> extends BinarySearchTree<T>  {
	
	AAtree() {
		super(null);
	}
	private class AAnode <K extends Comparable<K>> extends BinarySearchTreeNode<K> {
		AAnode(K element, BinarySearchTreeNode<K> parent) {
			super(element, parent);
		}
		int level = 1;
		public AAnode<K> getLeftChildAsAAnode() {
			return (AAnode<K>) leftChild;
		} 
		public AAnode<K> getRightChildAsAAnode() {
			return (AAnode<K>) rightChild;
		} 
		public AAnode<K> getParentAsAAnode() {
			return (AAnode<K>) parent;
		}
	}
	
	public void insert(T element) {
		insert(root, root, element);
		++nodeCount;
	}
	
	public void remove(T element) {
		remove(root, element);
	}
	
	public void insert(BinarySearchTreeNode<T> candidate, BinarySearchTreeNode<T> parent, T element) {
		if(candidate == null && parent == null) {
			root = new AAnode<T>(element, parent);
			return;
		}
		if(candidate == null) {
			candidate = new AAnode<T>(element, parent);
			if(parent.element.compareTo(element) > 0) {
				parent.leftChild = candidate;
			}else {
				parent.rightChild = candidate;
			}
		} else {
			if(candidate.element.compareTo(element) > 0) {
				insert(candidate.leftChild, candidate, element);
			}else {
				insert(candidate.rightChild, candidate, element);
			}
		}
		BinarySearchTreeNode<T> temp = candidate.parent;
		skew((AAnode<T>) candidate);
		candidate = candidate.parent == temp ? candidate : candidate.parent;
		split((AAnode<T>) candidate);
	}
	
	private void skew(AAnode<T> candidate) {
		if(candidate.leftChild != null) {
			if(candidate.getLeftChildAsAAnode().level == candidate.level) {
				BstUtils.leftLeftCase(candidate.leftChild, this);
			}
		}
	}
	
	private void split(AAnode<T> candidate) {
		if(candidate.rightChild != null) {
			if(candidate.parent != null) {
				if(candidate.getParentAsAAnode().level == candidate.getRightChildAsAAnode().level) {
					BstUtils.rightRightCase(candidate, this);
					++candidate.level;
				}
			}
		}
	}
	
	private void remove(BinarySearchTreeNode<T> candidate, T element) {
		if(candidate == null) {
			return;
		}
		int compare = candidate.element.compareTo(element);
		if(compare == 0) {
			if(candidate.leftChild == null || candidate.rightChild == null) {
				BinarySearchTreeNode<T> child = candidate.rightChild == null ? candidate.leftChild : candidate.rightChild;
				if(candidate.parent != null) {
					if(candidate.parent.leftChild == candidate) {
						candidate.parent.leftChild = child;
					}else {
						candidate.parent.rightChild = child;
					}
				} else {
					root = child;
				}
				if(child != null) child.parent = candidate.parent;
				candidate = null;
				--nodeCount;
			} else {
				T successor = BstUtils.preOrderSuccessor(candidate.leftChild, null);
				candidate.element = successor;
				remove(candidate.leftChild, successor);
			}
			return;
		}
		if(compare > 0) remove(candidate.leftChild, element);
		else remove(candidate.rightChild, element);
		updateLevel((AAnode<T>) candidate);
		BinarySearchTreeNode<T> temp = candidate.parent;
		skew((AAnode<T>) candidate);
		candidate = candidate.parent == temp ? candidate : candidate.parent;
		split((AAnode<T>) candidate);
	}
	
	private void updateLevel(AAnode<T> candidate) {
		int leftLevel, rightLevel;
		leftLevel = rightLevel = 0;
		if(candidate.leftChild != null) leftLevel = candidate.getLeftChildAsAAnode().level;
		if(candidate.rightChild != null) rightLevel = candidate.getRightChildAsAAnode().level;
		int levelDiff = candidate.level - Math.min(leftLevel, rightLevel);
		if(levelDiff == 2) {
			if(rightLevel == candidate.level) {
				--candidate.getRightChildAsAAnode().level;
			}
			--candidate.level;
		}
	}
	
	private void printTree() {
		if(nodeCount == 0) return;
		Queue<AAnode<T>> nodeQueue = new Queue<>();
		nodeQueue.push((AAnode<T>) root);
		while (!nodeQueue.empty()) {
			if(nodeQueue.peek().getLeftChildAsAAnode() != null) nodeQueue.push(nodeQueue.peek().getLeftChildAsAAnode());
			if(nodeQueue.peek().getRightChildAsAAnode() != null) nodeQueue.push(nodeQueue.peek().getRightChildAsAAnode());
			System.out.print(nodeQueue.peek().element + "(" + nodeQueue.pop().level + "), ");
		}
		System.out.println();
	}
	
	public static void main(String[] args) {
		AAtree<Integer> tree = new AAtree<>();
		Scanner scan = new Scanner(System.in);
		while (tree.size() < 10) {
			System.out.print(">>> ");
			tree.insert(scan.nextInt());
			tree.printTree();
		}
		System.out.println("remove :");
		while (tree.size() > 0) {
			System.out.print(">>> ");
			tree.remove(scan.nextInt());
			tree.printTree();
		}
	}
}
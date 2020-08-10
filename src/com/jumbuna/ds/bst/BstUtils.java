//
// BstUtils.java
// Created by Jacob Bumbuna Wangoni on 07/08/2020.

package com.jumbuna.ds.bst;

import com.jumbuna.ds.Queue;
import com.jumbuna.ds.Vector;

public class BstUtils {
	
	public static <T extends Comparable<T>> T preOrderSuccessor(BinarySearchTreeNode<T> leftChild, BinarySearchTreeNode<T> sentinel) {
		while(leftChild.rightChild != sentinel) {
			leftChild = leftChild.rightChild;
		}
		return leftChild.element;
	}
	public static <T extends Comparable<T>> T postOrderSuccessor(BinarySearchTreeNode<T> rightChild, BinarySearchTreeNode<T> sentinel) {
		while(rightChild.leftChild != sentinel) {
			rightChild = rightChild.leftChild;
		}
		return rightChild.element;
	}

	public static <T extends Comparable<T>> void TreeTraversal(TraversalOrder order, BinarySearchTreeNode<T> sentinel, Vector<T> vector, BinarySearchTreeNode<T> startingNode) {
		switch (order) {
			case IN:
				inOrder(vector, startingNode, sentinel);
				break;
			case PRE:
				preOrder(vector, startingNode, sentinel);
				break;
			case POST:
				postOrder(vector, startingNode, sentinel);
				break;
			case LEVEL:
				levelOrder(vector, startingNode, sentinel);
		}
	}

	private static <T extends Comparable<T>> void inOrder(Vector<T> vector, BinarySearchTreeNode<T> startingNode, BinarySearchTreeNode<T> sentinel) {
		if(startingNode != sentinel) {
			inOrder(vector, startingNode.leftChild, sentinel);
			vector.insert(startingNode.element);
			inOrder(vector, startingNode.rightChild, sentinel);
		}
	}

	private static <T extends Comparable<T>> void preOrder(Vector<T> vector, BinarySearchTreeNode<T> startingNode, BinarySearchTreeNode<T> sentinel) {
		if(startingNode != sentinel) {
			vector.insert(startingNode.element);
			preOrder(vector, startingNode.leftChild, sentinel);
			preOrder(vector, startingNode.rightChild, sentinel);
		}
	}

	private static <T extends Comparable<T>> void postOrder(Vector<T> vector, BinarySearchTreeNode<T> startingNode, BinarySearchTreeNode<T> sentinel) {
		if(startingNode != sentinel) {
			postOrder(vector, startingNode.leftChild, sentinel);
			postOrder(vector, startingNode.rightChild, sentinel);
			vector.insert(startingNode.element);
		}
	}

	private static <T extends Comparable<T>> void levelOrder(Vector<T> vector, BinarySearchTreeNode<T> startingNode, BinarySearchTreeNode<T> sentinel) {
		Queue<BinarySearchTreeNode<T>> nodeQueue = new Queue<>();
		nodeQueue.push(startingNode);
		while(!nodeQueue.empty()) {
			BinarySearchTreeNode<T> temp = nodeQueue.pop();
			if(temp.leftChild != sentinel) {
				nodeQueue.push(temp.leftChild);
			}
			if(temp.rightChild != sentinel) {
				nodeQueue.push(temp.rightChild);
			}
			vector.insert(temp.element);
		}
	}

	public static <T extends Comparable<T>> void clearTree(BinarySearchTree<T> tree) {
		if(tree.size() > 0) {
			Queue<BinarySearchTreeNode<T>> nodeQueue = new Queue<>();
			nodeQueue.push(tree.root);
			BinarySearchTreeNode<T> temp;
			while(!nodeQueue.empty()) {
				temp = nodeQueue.pop();
				if(temp.leftChild != tree.getSentinel()) {
					nodeQueue.push(temp.leftChild);
				}
				if(temp.rightChild != tree.getSentinel()) {
					nodeQueue.push(temp.rightChild);
				}
				temp.leftChild = temp.rightChild = temp.parent = null;
				temp.element = null;
			}
		}
	}

	private static <T extends Comparable<T>> void leftRotation(BinarySearchTreeNode<T> candidate, BinarySearchTree<T> tree) {
		BinarySearchTreeNode<T> parent = candidate.parent;
		BinarySearchTreeNode<T> leftChild = candidate.leftChild;
		BinarySearchTreeNode<T> grandParent = parent.parent;

		parent.rightChild = leftChild;
		candidate.leftChild = parent;
		candidate.parent = grandParent;

		if(leftChild != tree.getSentinel()) {
			leftChild.parent = parent;
		}

		if(grandParent != null) {
			if(parent == grandParent.leftChild) {
				grandParent.leftChild = candidate;
			}else {
				grandParent.rightChild = candidate;
			}
		}else {
			tree.root = candidate;
		}

		parent.parent = candidate;
	}

	private static <T extends Comparable<T>> void rightRotation(BinarySearchTreeNode<T> candidate, BinarySearchTree<T> tree) {
		BinarySearchTreeNode<T> parent = candidate.parent;
		BinarySearchTreeNode<T> rightChild = candidate.rightChild;
		BinarySearchTreeNode<T> grandParent = parent.parent;

		parent.leftChild = rightChild;
		candidate.rightChild = parent;
		candidate.parent = grandParent;

		if(rightChild != tree.getSentinel()) {
			rightChild.parent = parent;
		}

		if(grandParent != null) {
			if(parent == grandParent.leftChild) {
				grandParent.leftChild = candidate;
			}else {
				grandParent.rightChild = candidate;
			}
		}else {
			tree.root = candidate;
		}

		parent.parent = candidate;
	}

	public static <T extends Comparable<T>> void leftLeftCase(BinarySearchTreeNode<T> candidate, BinarySearchTree<T> tree) {
		rightRotation(candidate, tree);
	}

	public static <T extends Comparable<T>> void leftRightCase(BinarySearchTreeNode<T> candidate, BinarySearchTree<T> tree) {
		leftRotation(candidate, tree);
		rightRotation(candidate, tree);
	}

	public static <T extends Comparable<T>> void rightRightCase(BinarySearchTreeNode<T> candidate, BinarySearchTree<T> tree) {
		leftRotation(candidate, tree);
	}

	public static <T extends Comparable<T>> void rightLeftCase(BinarySearchTreeNode<T> candidate, BinarySearchTree<T> tree) {
		rightRotation(candidate, tree);
		leftRotation(candidate, tree);
	}

}
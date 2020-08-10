//
// Btree.java
// Created by Jacob Bumbuna Wangoni on 09/08/2020.
package com.jumbuna.ds;

import com.jumbuna.ds.bst.*;
import java.util.Scanner;

public class Btree <K extends Comparable<K>, V> {
	private int order;
	private BtreeNode root;
	private int keyCount;
	
	Btree(int order) {
		this.order = order;
		root = null;
	}
	
	Btree() {
		this(5);
	}
	
	private class BtreeNode implements Comparable<BtreeNode> {
		BtreeNode(BtreeNode parent) {
			this.parent = parent;
			this.keys = new AAtree<>();
			this.children = new AAtree<>();
			this.values = new HashMap<>();
		}
		AAtree<K> keys;
		AAtree<BtreeNode> children;
		BtreeNode parent;
		HashMap<K, V> values;
		public int compareTo(BtreeNode other) {
			return BstUtils.postOrderSuccessor(keys.root, null).compareTo(BstUtils.postOrderSuccessor(other.keys.root, null));
		}
		public void clear() {
			keys.clear();
			values.clear();
			children.clear();
		}
		public void insert(K key, V value) {
			keys.insert(key);
			values.insert(key, value);
		}
		public void remove(K key) {
			keys.remove(key);
			values.remove(key);
		}
	}
	
	private BtreeNode successorChild(BtreeNode candidate, K key) {
		if(BstUtils.postOrderSuccessor(candidate.keys.root, null).compareTo(key) > 0) {
			return BstUtils.postOrderSuccessor(candidate.children.root, null);
		} else if(BstUtils.preOrderSuccessor(candidate.keys.root, null).compareTo(key) <= 0) {
			return BstUtils.preOrderSuccessor(candidate.children.root, null);
		} else {
			Vector<K> keys = candidate.keys.treeTraversal(TraversalOrder.IN);
			Vector<BtreeNode> children = candidate.children.treeTraversal(TraversalOrder.IN);
			for(int i = 0; i < keys.size(); i++) {
				if(keys.elementAt(i).compareTo(key) > 0) {
					return children.elementAt(i);
				}
			}
		}
		return null;
	}
	
	private BtreeNode preOrderSuccessor(BtreeNode candidate) {
		while(candidate.children.size() != 0) {
			candidate = BstUtils.preOrderSuccessor(candidate.children.root, null);
		}
		return candidate;
	}
	
	public void insert(K key, V value) {
		insert(root, key, value);
		++keyCount;
	}
	
	public void remove(K key) {
		if(keyCount == 0) return;
		remove(root, key);
	}
	
	private void insert(BtreeNode candidate, K key, V value) {
		if(candidate == null) {
			root = new BtreeNode(null);
			root.insert(key, value);
			return;
		}
		if(candidate.children.size() == 0) {
			candidate.insert(key, value);
		} else {
			insert(successorChild(candidate, key), key, value);
		}
		postInsertBalance(candidate);
	}
	
	private void postInsertBalance(BtreeNode candidate) {
		if(candidate.keys.size() == order) {
			BtreeNode parent = candidate.parent == null ? candidate : candidate.parent;
			BtreeNode newLeft = new BtreeNode(parent);
			BtreeNode newRight = new BtreeNode(parent);
			Vector<K> keys = candidate.keys.treeTraversal(TraversalOrder.IN);
			Vector<BtreeNode> children = candidate.children.treeTraversal(TraversalOrder.IN);
			int midian = order/2;
			for(int i = 0; i < this.order; i++) {
				if(i < midian) {
					newLeft.insert(keys.elementAt(i), candidate.values.get(keys.elementAt(i)));
				} else if(i > midian) {
					newRight.insert(keys.elementAt(i), candidate.values.get(keys.elementAt(i)));
				}
			}
			BtreeNode node;
			for(int i = 0; i < children.size(); i++) {
				node = children.elementAt(i);
				if(i <= midian) {
					newLeft.children.insert(node);
					node.parent = newLeft;
				} else {
					newRight.children.insert(node);
					node.parent = newRight;
				}
			}
			V temp = candidate.values.get(keys.elementAt(midian));
			if(parent == candidate) {
				candidate.clear();
				candidate.insert(keys.elementAt(midian), temp);
			} else {
				parent.children.remove(candidate);
				parent.insert(keys.elementAt(midian), temp);
				candidate = null;
			}
			parent.children.insert(newLeft);
			parent.children.insert(newRight);
		}
	}
	
	private void remove(BtreeNode candidate, K key) {
		if(candidate.keys.contains(key)) {
			if(candidate.children.size() == 0) {
				candidate.remove(key);
				--keyCount;
				if(candidate == root) {
					return;
				}
			} else {
				int keyIndex = candidate.keys.treeTraversal(TraversalOrder.IN).indexOf(key);
				BtreeNode successor = preOrderSuccessor(candidate.children.treeTraversal(TraversalOrder.IN).elementAt(keyIndex));
				candidate.remove(key);
				key = BstUtils.preOrderSuccessor(successor.keys.root, null);
				candidate.insert(key, successor.values.get(key));
				remove(successor, key);
			}
		} else {
			if(candidate.children.size() == 0) return;
			else remove(successorChild(candidate, key), key);
		}
		postRemoveBalance(candidate);
	}
	
	private void postRemoveBalance(BtreeNode candidate) {
		if(candidate.keys.size() < Math.ceil(order/2.0)-1) {
			if(candidate == root) {
				if(candidate.keys.size() == 0) {
					root = BstUtils.preOrderSuccessor(candidate.children.root, null);
					root.parent = null;
				}
				return;
			}
			Vector<K> parentKeys = candidate.parent.keys.treeTraversal(TraversalOrder.IN);
			Vector<BtreeNode> parentChildren = candidate.parent.children.treeTraversal(TraversalOrder.IN);
			removeCaseOne(candidate, parentKeys, parentChildren);
		}
	}
	
	private void removeCaseOne(BtreeNode candidate, Vector<K> parentKeys, Vector<BtreeNode> parentChildren) {
		//borrow from right
		int candidateIndex = parentChildren.indexOf(candidate);
		if(candidateIndex+1 < parentChildren.size()) {
			BtreeNode sibling = parentChildren.elementAt(candidateIndex+1);
			if(sibling.keys.size() > Math.ceil(order/2.0)-1) {
				borrowKeyFromRight(candidate, sibling, candidate.parent, parentKeys.elementAt(candidateIndex));
				return;
			}
		}
		removeCaseTwo(candidate, parentKeys, parentChildren, candidateIndex);
	}
	
	private void removeCaseTwo(BtreeNode candidate, Vector<K> parentKeys, Vector<BtreeNode> parentChildren, int candidateIndex) {
		//borrow from left
		if(candidateIndex != 0) {
			BtreeNode sibling = parentChildren.elementAt(candidateIndex-1);
			if(sibling.keys.size() > Math.ceil(order/2.0)-1) {
				borrowKeyFromLeft(candidate, sibling, candidate.parent, parentKeys.elementAt(candidateIndex-1));
				return;
			}
		}
		removeCaseThree(candidate, parentKeys, parentChildren, candidateIndex);
	}
	
	private void borrowKeyFromRight(BtreeNode candidate, BtreeNode sibling, BtreeNode parent, K parentKey) {
		candidate.insert(parentKey, parent.values.get(parentKey));
		parent.remove(parentKey);
		K key = BstUtils.postOrderSuccessor(sibling.keys.root, null);
		parent.insert(key, sibling.values.get(key));
		if(sibling.children.size() > 0) {
			BtreeNode child = BstUtils.postOrderSuccessor(sibling.children.root, null);
			sibling.children.remove(child);
			child.parent = candidate;
			candidate.children.insert(child);
		}
		sibling.remove(key);
	}
	
	private void borrowKeyFromLeft(BtreeNode candidate, BtreeNode sibling, BtreeNode parent, K parentKey) {
		candidate.insert(parentKey, parent.values.get(parentKey));
		parent.remove(parentKey);
		K key = BstUtils.preOrderSuccessor(sibling.keys.root, null);
		parent.insert(key, sibling.values.get(key));
		if(sibling.children.size() > 0) {
			BtreeNode child = BstUtils.preOrderSuccessor(sibling.children.root, null);
			sibling.children.remove(child);
			child.parent = candidate;
			candidate.children.insert(child);
		}
		sibling.remove(key);
	}
	
	private void merge(BtreeNode candidate, BtreeNode sibling, BtreeNode parent, K parentKey) {
		Vector<K> keys = sibling.keys.treeTraversal(TraversalOrder.IN);
		Vector<BtreeNode> children = sibling.children.treeTraversal(TraversalOrder.IN);
		candidate.insert(parentKey, parent.values.get(parentKey));
		parent.remove(parentKey);
		for(int i = 0; i < keys.size(); i++) {
			candidate.insert(keys.elementAt(i), sibling.values.get(keys.elementAt(i)));
		}
		for(int i = 0; i < children.size(); i++) {
			candidate.children.insert(children.elementAt(i));
			children.elementAt(i).parent = candidate;
		}
		if(keys.size() == 0) {
			sibling.keys.insert(parentKey);
		}
		parent.children.remove(sibling);
	}
	
	private void removeCaseThree(BtreeNode candidate, Vector<K> parentKeys, Vector<BtreeNode> parentChildren, int candidateIndex) {
		if(candidateIndex == 0) {
			merge(candidate, parentChildren.elementAt(1), candidate.parent, parentKeys.elementAt(candidateIndex));
		} else {
			merge(parentChildren.elementAt(candidateIndex-1), candidate, candidate.parent, parentKeys.elementAt(candidateIndex-1));
		}
	}
	
	public void printTree() {
		if(root != null) {
			Queue<BtreeNode> nodeQueue = new Queue<>();
			nodeQueue.push(root);
			BtreeNode temp;
			while(!nodeQueue.empty()) {
				temp = nodeQueue.pop();
				Vector<K> keys = temp.keys.treeTraversal(TraversalOrder.IN);
				Vector<BtreeNode> children = temp.children.treeTraversal(TraversalOrder.IN);
				System.out.println(keys);
				for(int i = 0; i < children.size(); i++) {
					nodeQueue.push(children.elementAt(i));
				}
			}
		}
	}
	
	public int size() {
		return keyCount;
	}
	
	public V getValue(K key) {
		if(size() == 0) return null;
		BtreeNode temp = root;
		while(temp.children.size() > 0) {
			if(temp.keys.contains(key)) {
				return temp.values.get(key);
			} else {
				temp = successorChild(temp, key);
			}
		}
		return temp.values.get(key);
	}
	
	public boolean contains(K key) {
		if(size() == 0) {
			return false;
		}
		BtreeNode temp = root;
		while(temp.children.size() > 0) {
			if(temp.keys.contains(key)) {
				return true;
			} else {
				temp = successorChild(temp, key);
			}
		}
		return temp.keys.contains(key);
	}
	
	public static void main(String[] args) {
		Btree<Integer,Integer> tree = new Btree<>(3);
		Scanner scan = new Scanner(System.in);
		while(tree.size() < 10) {
			System.out.print(">>> ");
			int x = scan.nextInt();
			tree.insert(x, x*x);
		}
		System.out.println("remove: ");
		while(tree.size() > 0) {
			System.out.print(">>> ");
			int x = scan.nextInt();
			System.out.println(tree.getValue(x));
			tree.remove(x);
		}
	}
}
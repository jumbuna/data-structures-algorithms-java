package com.jumbuna.ds;

import com.jumbuna.ds.bst.BstUtils;
import com.jumbuna.ds.bst.RedBlackTree;
import com.jumbuna.ds.bst.TraversalOrder;

import java.util.Scanner;

public class BPlusTree <K extends Comparable<K>, V> {
    private class BpNode implements Comparable<BpNode> {
        public RedBlackTree<K> keys = new RedBlackTree<>();
        public RedBlackTree<BpNode> children = new RedBlackTree<>();
        public BpNode parent = null;
        public HashMap<K, V> values = new HashMap<>();
        public BpNode nextLeaf, previousLeaf;
        public BpNode(BpNode parent) {
            this.parent = parent;
        }
        public void insert(K key, V value) {
            keys.insert(key);
            values.insert(key, value);
        }
        public void remove(K key) {
            keys.remove(key);
            values.remove(key);
        }
        public void clear() {
            if(children.size() == 0) {
                values.clear();
            }
            keys.clear();
            children.clear();
        }
        public int compareTo(BpNode other) {
            return BstUtils.postOrderSuccessor(keys.root, keys.getSentinel()).compareTo(BstUtils.postOrderSuccessor(other.keys.root, other.keys.getSentinel()));
        }
    }

    BpNode root = null;
    int keyCount = 0;
    int order;
    BpNode firstLeaf = null;

    BPlusTree(int order) {
        this.order = order;
    }

    BPlusTree() {
        this(5);
    }

    private BpNode successorChild(BpNode candidate, K key) {
        if(BstUtils.postOrderSuccessor(candidate.keys.root, candidate.keys.getSentinel()).compareTo(key) > 0) {
            return BstUtils.postOrderSuccessor(candidate.children.root, candidate.children.getSentinel());
        }else if(BstUtils.preOrderSuccessor(candidate.keys.root, candidate.keys.getSentinel()).compareTo(key) <= 0) {
            return BstUtils.preOrderSuccessor(candidate.children.root, candidate.children.getSentinel());
        } else {
            Vector<K> keys = candidate.keys.treeTraversal(TraversalOrder.IN);
            Vector<BpNode> children = candidate.children.treeTraversal(TraversalOrder.IN);
            for(int i = 1; i < keys.size(); i++) {
                if(keys.elementAt(i).compareTo(key) > 0) {
                    return children.elementAt(i);
                }
            }
        }
        return null;
    }

    private BpNode findInternalKeyNode(BpNode candidate, K key) {
        while(candidate != null) {
            if(candidate.keys.contains(key)) {
                return candidate;
            }
            candidate = candidate.parent;
        }
        return null;

    }

    public void insert(K key , V value) {
        insert(root, key, value);
        ++keyCount;
    }

    private void insert(BpNode candidate, K key, V value) {
        if(candidate == null) {
            firstLeaf = root = new BpNode(null);
            root.insert(key, value);
            return;
        }

        if(candidate.children.size() == 0) {
            candidate.insert(key, value);
            postInsertLeafNode(candidate);
            return;

        } else {
            insert(successorChild(candidate, key), key, value);
        }
        postInsertInternalNode(candidate);
    }

    private void postInsertLeafNode(BpNode candidate) {
        if(candidate.keys.size() == order) {
            BpNode parent = candidate == root ? candidate : candidate.parent;
            BpNode leftLeaf = new BpNode(parent);
            BpNode rightLeaf = new BpNode(parent);
            leftLeaf.previousLeaf = candidate.previousLeaf;
            leftLeaf.nextLeaf = rightLeaf;
            rightLeaf.previousLeaf = leftLeaf;
            rightLeaf.nextLeaf = candidate.nextLeaf;
            if(candidate == firstLeaf) {
                firstLeaf = leftLeaf;
            }
            if(candidate.previousLeaf != null) {
                candidate.previousLeaf.nextLeaf = leftLeaf;
            }
            if(candidate.nextLeaf != null) {
                candidate.nextLeaf.previousLeaf = rightLeaf;
            }
            Vector<K> keys = candidate.keys.treeTraversal(TraversalOrder.IN);
            int midian = order/2;
            for(int i = 0; i < keys.size(); i++) {
                if(i < midian) {
                    leftLeaf.insert(keys.elementAt(i), candidate.values.get(keys.elementAt(i)));
                } else {
                    rightLeaf.insert(keys.elementAt(i), candidate.values.get(keys.elementAt(i)));
                }
            }
            if(candidate == parent) {
                candidate.clear();
                candidate.keys.insert(keys.elementAt(midian));
            } else {
                parent.keys.insert(keys.elementAt(midian));
                parent.children.remove(candidate);
            }
            parent.children.insert(leftLeaf);
            parent.children.insert(rightLeaf);
        }
    }

    private void postInsertInternalNode(BpNode candidate) {
        if(candidate.keys.size() == order) {
            BpNode parent = candidate == root ? candidate : candidate.parent;
            BpNode leftLeaf = new BpNode(parent);
            BpNode rightLeaf = new BpNode(parent);
            Vector<K> keys = candidate.keys.treeTraversal(TraversalOrder.IN);
            Vector<BpNode> children = candidate.children.treeTraversal(TraversalOrder.IN);
            int midian = order/2;
            for(int i = 0; i < keys.size(); i++) {
                if(i < midian) {
                    leftLeaf.keys.insert(keys.elementAt(i));
                } else if(i > midian) {
                    rightLeaf.keys.insert(keys.elementAt(i));
                }
            }
            for(int i = 0; i < children.size(); i++) {
                if(i <= midian) {
                    leftLeaf.children.insert(children.elementAt(i));
                    children.elementAt(i).parent = leftLeaf;
                } else {
                    rightLeaf.children.insert(children.elementAt(i));
                    children.elementAt(i).parent = rightLeaf;
                }
            }
            if(candidate == parent) {
                candidate.clear();
                candidate.keys.insert(keys.elementAt(midian));
            } else {
                parent.keys.insert(keys.elementAt(midian));
                parent.children.remove(candidate);
            }
            parent.children.insert(leftLeaf);
            parent.children.insert(rightLeaf);
        }
    }

    public void remove(K key) {
        remove(root, key);
    }

    private void remove(BpNode candidate, K key) {
        if(candidate.keys.contains(key) && candidate.children.size() == 0) {
            candidate.remove(key);
            --keyCount;
            if(candidate == root) {
                return;
            }
            BpNode internalNode = findInternalKeyNode(candidate.parent, key);
            BpNode ret = postRemoveLeafNodeBalance(candidate);
            if(internalNode != null) {
                if(internalNode.keys.contains(key)) {
                    internalNode.keys.remove(key);
                    internalNode.keys.insert(BstUtils.postOrderSuccessor(ret.keys.root, ret.keys.getSentinel()));
                }
            }
            return;
        }
        if(candidate.children.size() > 0) remove(successorChild(candidate, key), key);
        else return;
        postRemoveInternalNodeBalance(candidate);
    }

    private BpNode postRemoveLeafNodeBalance(BpNode candidate) {
        if(candidate.keys.size() < Math.ceil(order/2.0)-1) {
            Vector<K> parentKeys = candidate.parent.keys.treeTraversal(TraversalOrder.IN);
            Vector<BpNode> parentChildren = candidate.parent.children.treeTraversal(TraversalOrder.IN);
            candidate = removeLeafNodeCaseOne(candidate, candidate.parent, parentKeys, parentChildren);
        }
        return candidate;
    }

    private void postRemoveInternalNodeBalance(BpNode candidate) {
        if(candidate.keys.size() < Math.ceil(order/2.0)-1) {
            if(candidate == root) {
                if(candidate.keys.size() == 0) {
                    root = BstUtils.postOrderSuccessor(candidate.children.root, candidate.children.getSentinel());
                    root.parent = null;
                }
                return;
            }
            Vector<K> parentKeys = candidate.parent.keys.treeTraversal(TraversalOrder.IN);
            Vector<BpNode> parentChildren = candidate.parent.children.treeTraversal(TraversalOrder.IN);
            removeInternalNodeCaseOne(candidate, candidate.parent, parentKeys, parentChildren);
        }

    }

    private BpNode removeLeafNodeCaseOne(BpNode candidate, BpNode parent, Vector<K> parentKeys, Vector<BpNode> parentChildren) {
        //right borrow
        int candidateIndex = parentChildren.indexOf(candidate);
        if(candidateIndex + 1 < parentChildren.size()) {
            BpNode sibling = parentChildren.elementAt(candidateIndex+1);
            if(sibling.keys.size() > Math.ceil(order/2.0)-1) {
                borrowKeyFromRightLeafNode(candidate, sibling, parent, parentKeys.elementAt(candidateIndex));
                return candidate;
            }
        }
        return removeLeafNodeCaseTwo(candidate, parent, parentKeys, parentChildren, candidateIndex);
    }

    private BpNode removeLeafNodeCaseTwo(BpNode candidate, BpNode parent, Vector<K> parentKeys, Vector<BpNode> parentChildren, int candidateIndex) {
        //borrow left
        if(candidateIndex > 0) {
            BpNode sibling = parentChildren.elementAt(candidateIndex-1);
            if(sibling.keys.size() > Math.ceil(order/2.0)-1) {
                borrowKeyFromLeftLeafNode(candidate, sibling, parent, parentKeys.elementAt(candidateIndex-1));
                return candidate;
            }
        }
        return removeLeafNodeCaseThree(candidate, parent, parentKeys, parentChildren, candidateIndex);
    }

    private BpNode removeLeafNodeCaseThree(BpNode candidate, BpNode parent, Vector<K> parentKeys, Vector<BpNode> parentChildren, int candidateIndex) {
        if(candidateIndex == 0) {
            mergeLeafNodes(candidate, parentChildren.elementAt(1), parent, parentKeys.elementAt(0));
            return candidate;
        } else {
            mergeLeafNodes(parentChildren.elementAt(candidateIndex-1), candidate, parent, parentKeys.elementAt(candidateIndex-1));
        }
        return parentChildren.elementAt(candidateIndex-1);
    }

    private void borrowKeyFromRightLeafNode(BpNode candidate, BpNode sibling, BpNode parent, K parentKey) {
        parent.keys.remove(parentKey);
        K key = BstUtils.postOrderSuccessor(sibling.keys.root, sibling.keys.getSentinel());
        candidate.insert(key, sibling.values.get(key));
        sibling.remove(key);
        parent.keys.insert(BstUtils.postOrderSuccessor(sibling.keys.root, sibling.keys.getSentinel()));
    }

    private void borrowKeyFromLeftLeafNode(BpNode candidate, BpNode sibling, BpNode parent, K parentKey) {
        parent.keys.remove(parentKey);
        K key = BstUtils.preOrderSuccessor(sibling.keys.root, sibling.keys.getSentinel());
        candidate.insert(key, sibling.values.get(key));
        sibling.remove(key);
        parent.keys.insert(key);
    }

    private void mergeLeafNodes(BpNode candidate, BpNode sibling, BpNode parent, K parentKey) {
        Vector<K> keys = sibling.keys.treeTraversal(TraversalOrder.IN);
        for(int i = 0; i < keys.size(); i++) {
            candidate.insert(keys.elementAt(i), sibling.values.get(keys.elementAt(i)));
        }
        parent.keys.remove(parentKey);
        if(keys.size() == 0) {
            sibling.keys.insert(parentKey);
        }
        parent.children.remove(sibling);
        candidate.nextLeaf = sibling.nextLeaf;
        if(sibling.nextLeaf != null) {
            sibling.nextLeaf.previousLeaf = candidate;
        }
    }

    private void removeInternalNodeCaseOne(BpNode candidate, BpNode parent, Vector<K> parentKeys, Vector<BpNode> parentChildren) {
        //right borrow
        int candidateIndex = parentChildren.indexOf(candidate);
        if(candidateIndex+1 < parentChildren.size()) {
            BpNode sibling = parentChildren.elementAt(candidateIndex+1);
            if(sibling.keys.size() > Math.ceil(order/2.0)-1) {
                borrowKeyFromRightInternalNode(candidate, sibling, parent, parentKeys.elementAt(candidateIndex));
                return;
            }
        }
        removeInternalNodeCaseTwo(candidate, parent, parentKeys, parentChildren, candidateIndex);
    }

    private void removeInternalNodeCaseTwo(BpNode candidate, BpNode parent, Vector<K> parentKeys, Vector<BpNode> parentChildren, int candidateIndex) {
        //borrow left
        if(candidateIndex > 0) {
            BpNode sibling = parentChildren.elementAt(candidateIndex-1);
            if(sibling.keys.size() > Math.ceil(order/2.0)-1) {
                borrowKeyFromLeftInternalNode(candidate, sibling, parent, parentKeys.elementAt(candidateIndex-1));
                return;
            }
        }
        removeInternalNodeCaseThree(candidate, parent, parentKeys, parentChildren, candidateIndex);
    }

    private void removeInternalNodeCaseThree(BpNode candidate, BpNode parent, Vector<K> parentKeys, Vector<BpNode> parentChildren, int candidateIndex) {
        if(candidateIndex == 0) {
            mergeInternalNodes(candidate, parentChildren.elementAt(1), parent, parentKeys.elementAt(0));
        } else {
            mergeInternalNodes(parentChildren.elementAt(candidateIndex-1), candidate, parent, parentKeys.elementAt(candidateIndex-1));
        }

    }


    private void borrowKeyFromRightInternalNode(BpNode candidate, BpNode sibling, BpNode parent, K parentKey) {
        parent.keys.remove(parentKey);
        K key = BstUtils.postOrderSuccessor(sibling.keys.root, sibling.keys.getSentinel());
        candidate.keys.insert(parentKey);
        parent.keys.insert(key);
        sibling.keys.remove(key);
        BpNode child = BstUtils.postOrderSuccessor(sibling.children.root, sibling.children.getSentinel());
        child.parent = candidate;
        sibling.children.remove(child);
    }

    private void borrowKeyFromLeftInternalNode(BpNode candidate, BpNode sibling, BpNode parent, K parentKey) {
        parent.keys.remove(parentKey);
        K key = BstUtils.preOrderSuccessor(sibling.keys.root, sibling.keys.getSentinel());
        candidate.keys.insert(parentKey);
        parent.keys.insert(key);
        sibling.keys.remove(key);
        BpNode child = BstUtils.preOrderSuccessor(sibling.children.root, sibling.children.getSentinel());
        child.parent = candidate;
        sibling.children.remove(child);
    }

    private void mergeInternalNodes(BpNode candidate, BpNode sibling, BpNode parent, K parentKey) {
        Vector<K> keys = sibling.keys.treeTraversal(TraversalOrder.IN);
        Vector<BpNode> children = sibling.children.treeTraversal(TraversalOrder.IN);
        candidate.keys.insert(parentKey);
        for(int i = 0; i < keys.size(); i++) {
            candidate.keys.insert(keys.elementAt(i));
        }
        parent.keys.remove(parentKey);
        if(keys.size() == 0) {
            sibling.keys.insert(parentKey);
        }
        for(int i = 0; i < children.size(); i++) {
            candidate.children.insert(children.elementAt(i));
            children.elementAt(i).parent = candidate;
        }
        parent.children.remove(sibling);
    }

    private void printTree() {
        if(root != null) {
            Queue<BpNode> queue = new Queue<>();
            queue.push(root);
            while(!queue.empty()) {
                BpNode temp = queue.pop();
                Vector<K> keys = temp.keys.treeTraversal(TraversalOrder.IN);
                Vector<BpNode> children = temp.children.treeTraversal(TraversalOrder.IN);
                for(int i = 0; i < children.size(); i++) {
                    queue.push(children.elementAt(i));
                }
                System.out.println(keys);
            }
        }
    }

    public int size() {
        return keyCount;
    }

    public V getValue(K key) {
        BpNode temp = firstLeaf;
        while(temp != null) {
            if(temp.keys.contains(key)) {
                return temp.values.get(key);
            }
            temp = temp.nextLeaf;
        }
        return null;
    }

    public boolean contains(K key) {
        BpNode temp = firstLeaf;
        while(temp != null) {
            if(temp.keys.contains(key)) return true;
            else temp = temp.nextLeaf;
        }
        return false;
    }

    public static void main(String[] args) {
        BPlusTree<Integer, Integer> tree = new BPlusTree<>(4);
        Scanner scanner = new Scanner(System.in);
        while(tree.size() < 10) {
            System.out.print(">>> ");
            tree.insert(scanner.nextInt(), scanner.nextInt()*5);
            tree.printTree();
        }
        System.out.println("remove: ");
        while(tree.size() > 0) {
            System.out.print(">>> ");
            int x = scanner.nextInt();
            System.out.println(tree.getValue(x));
            tree.remove(x);
            tree.printTree();
        }
    }

}

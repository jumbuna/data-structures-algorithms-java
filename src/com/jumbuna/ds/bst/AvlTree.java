package com.jumbuna.ds.bst;

import com.jumbuna.ds.Vector;

public class AvlTree <T extends Comparable<T>> extends BinarySearchTree<T> {

    AvlTree() {
        super(null);
    }

    private class AvlNode <T extends Comparable<T>> extends BinarySearchTreeNode<T> {
        public int height = 0, balanceFactor = 0;
        AvlNode(T element, BinarySearchTreeNode<T> parent) {
            super(element, parent);
        }
        public AvlNode<T> getLeftChildAsAvlNode() {
            return (AvlNode<T>) leftChild;
        }
        public AvlNode<T> getRightChildAsAvlNode() {
            return (AvlNode<T>) rightChild;
        }
    }

    @Override
    public void insert(T element) {
        insert(root, root, element);
        ++nodeCount;
    }

    @Override
    public void remove(T element) {
        if(contains(element)) {
            remove(root, element);
            --nodeCount;
        }
    }

    private void insert(BinarySearchTreeNode<T> candidate, BinarySearchTreeNode<T> parent, T element) {
        if(candidate == null && parent == null) {
            root = new AvlNode<>(element, null);
            return;
        }
        if(candidate == getSentinel()) {
            if(parent.element.compareTo(element) > 0) {
                parent.leftChild = new AvlNode<>(element, parent);
            }else {
                parent.rightChild = new AvlNode<>(element, parent);
            }
            return;
        }

        if (candidate.element.compareTo(element) > 0) {
            insert(candidate.leftChild, candidate, element);
        }else {
            insert(candidate.rightChild, candidate, element);
        }

        updateHeightAndBalanceFactor((AvlNode<T>) candidate);
        balance((AvlNode<T>) candidate);
    }

    private void remove(BinarySearchTreeNode<T> candidate, T element) {
        if(candidate == getSentinel()) {
            return;
        }
        BinarySearchTreeNode<T> parent = candidate.parent;
        if(candidate.element.compareTo(element) == 0) {
            if(candidate.rightChild == getSentinel()) {
                if(parent != null) {
                    if(candidate.equals(parent.leftChild)) {
                        parent.leftChild = candidate.leftChild;
                    }else {
                        parent.rightChild =  candidate.leftChild;
                    }
                }else {
                    root = candidate.leftChild;
                }
                if(candidate.leftChild != getSentinel()) {
                    candidate.leftChild.parent = parent;
                }
                return;
            } else if(candidate.leftChild == getSentinel()) {
                if(parent != null) {
                    if(candidate.equals(parent.leftChild)) {
                        parent.leftChild = candidate.rightChild;
                    }else {
                        parent.rightChild =  candidate.rightChild;
                    }
                }else {
                    root = candidate.rightChild;
                }
                if(candidate.rightChild != getSentinel()) {
                    candidate.rightChild.parent = parent;
                }
                return;
            } else {
                T successor = BstUtils.preOrderSuccessor(candidate.leftChild, getSentinel());
                candidate.element = successor;
                remove(candidate.leftChild, successor);
            }
        } else {
            if(candidate.element.compareTo(element) > 0) {
                remove(candidate.leftChild, element);
            } else {
                remove(candidate.rightChild, element);
            }
        }

        updateHeightAndBalanceFactor((AvlNode<T>) candidate);
        balance((AvlNode<T>) candidate);
    }

    private void updateHeightAndBalanceFactor(AvlNode<T> candidate) {
        //balanceFactor = lh-rh
        int leftHeight = -1 , rightHeight = -1;
        if(candidate.leftChild != getSentinel()) {
            leftHeight = candidate.getLeftChildAsAvlNode().height;
        }
        if(candidate.rightChild != getSentinel()) {
            rightHeight = candidate.getRightChildAsAvlNode().height;
        }

        candidate.balanceFactor = leftHeight - rightHeight;
        candidate.height = 1 + Math.max(leftHeight, rightHeight);
    }

    private void balance(AvlNode<T> candidate) {
        if(candidate.balanceFactor == 2) {
            //left-heavy
            if(candidate.getLeftChildAsAvlNode().balanceFactor == 1) {
                BstUtils.leftLeftCase(candidate.leftChild, this);
            }else if(candidate.getLeftChildAsAvlNode().balanceFactor == -1) {
                BstUtils.leftRightCase(candidate.rightChild, this);
            }
        }

        if (candidate.balanceFactor == -2) {
            //right-heavy
            if(candidate.getRightChildAsAvlNode().balanceFactor == 1) {
                BstUtils.rightLeftCase(candidate.leftChild, this);
            }else if(candidate.getRightChildAsAvlNode().balanceFactor == -1) {
                BstUtils.rightRightCase(candidate.rightChild, this);
            }
        }

        updateHeightAndBalanceFactor(candidate);
    }


    //test
    public static void main(String[] args) {
        AvlTree<Integer> avlTree = new AvlTree<>();
        for (int i = 0; i < 10; i++) {
            avlTree.insert(i);
        }
        avlTree.remove(7);
        avlTree.remove(2);
        avlTree.remove(6);
        avlTree.remove(3);
        avlTree.remove(8);

        Vector<Integer> vector = avlTree.treeTraversal(TraversalOrder.LEVEL);

        for (int i = 0; i < vector.size(); i++) {
            System.out.print(vector.elementAt(i) + ", ");
        }
    }
}

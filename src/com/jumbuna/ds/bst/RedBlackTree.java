package com.jumbuna.ds.bst;

import com.jumbuna.ds.Queue;

import java.util.Scanner;

public class RedBlackTree <T extends Comparable<T>> extends BinarySearchTree<T> {

    private enum Color {
        BLACK, RED
    }
    //K == T
    private class RedBlackNode<K extends Comparable<K>> extends BinarySearchTreeNode<K> {
        RedBlackNode(K element, BinarySearchTreeNode<K> parent, BinarySearchTreeNode<K> sentinel) {
            super(element, parent, sentinel);
            color = Color.RED;
        }

        RedBlackNode() {
            super(null, null, null);
            color = Color.BLACK;
        }

        Color color;

        public RedBlackNode<K> getLeftChildAsRedBlackNode() {
            return (RedBlackNode<K>) leftChild;
        }
        public RedBlackNode<K> getRightChildAsRedBlackNode() {
            return (RedBlackNode<K>) rightChild;
        }
        public RedBlackNode<K> getParentAsRedBlackNode() {
            return (RedBlackNode<K>) parent;
        }
    }

    RedBlackTree() {
        super(null);
        sentinel = new RedBlackNode<>();
        root = null;
    }

    @Override
    public void insert(T element) {
        insert(root, root, element);
        ++nodeCount;
    }

    @Override
    public void remove(T element) {
        remove(root, element);
        if(nodeCount == 0) {
            root = null;
        }
    }

    private void insert(BinarySearchTreeNode<T> candidate, BinarySearchTreeNode<T> parent, T element) {
        if(candidate == null && parent == null) {
            root = new RedBlackNode<>(element, null, sentinel);
            ((RedBlackNode<T>) root).color = Color.BLACK;
            return;
        }

        if(candidate == sentinel) {
            candidate = new RedBlackNode<>(element, parent, sentinel);
            if(parent.element.compareTo(element) > 0) {
                parent.leftChild = candidate;
            } else {
                parent.rightChild = candidate;
            }
            postInsertBalance((RedBlackNode<T>) candidate);
            return;
        }

        if(candidate.element.compareTo(element) > 0) {
            insert(candidate.leftChild, candidate, element);
        }else {
            insert(candidate.rightChild, candidate, element);
        }

    }

    private void postInsertBalance(RedBlackNode<T> candidate) {
        if(candidate.color == Color.BLACK) {
            return;
        }
        insertCaseOne(candidate);
    }

    private void insertCaseOne(RedBlackNode<T> candidate) {
        if(candidate == root) {
            candidate.color = Color.BLACK;
        }else {
            insertCaseTwo(candidate);
        }
    }

    private void insertCaseTwo(RedBlackNode<T> candidate) {
        RedBlackNode<T> parent = candidate.getParentAsRedBlackNode();
        if(parent.color == Color.RED) {
            insertCaseThree(candidate, parent);
        }
    }

    private void insertCaseThree(RedBlackNode<T> candidate, RedBlackNode<T> parent) {
        RedBlackNode<T> grandParent = parent.getParentAsRedBlackNode();
        RedBlackNode<T> uncle = grandParent.leftChild == parent ? grandParent.getRightChildAsRedBlackNode() : grandParent.getLeftChildAsRedBlackNode();
        if(uncle.color == Color.RED) {
            grandParent.color = Color.RED;
            parent.color = Color.BLACK;
            uncle.color = Color.BLACK;
            insertCaseOne(grandParent);
        } else {
            insertCaseFour(candidate, parent, grandParent, parent == grandParent.leftChild, parent.leftChild == candidate);
        }

    }

    private void insertCaseFour(RedBlackNode<T> candidate, RedBlackNode<T> parent, RedBlackNode<T> grandParent, boolean parentIsLeft, boolean candidateIsLeft) {
        if(parentIsLeft && candidateIsLeft) {
            //left-left case
            BstUtils.leftLeftCase(parent, this);
            parent.color = Color.BLACK;
        } else if(parentIsLeft) {
            //left right
            BstUtils.leftRightCase(candidate, this);
            candidate.color = Color.BLACK;
        } else if(candidateIsLeft) {
            //right left
            BstUtils.rightLeftCase(candidate, this);
            candidate.color = Color.BLACK;
        } else {
            //right right
            BstUtils.rightRightCase(parent, this);
            parent.color = Color.BLACK;
        }
        grandParent.color = Color.RED;
    }

    private void remove(BinarySearchTreeNode<T> node, T element) {
        if(node == sentinel || node == null) {
            return;
        }
        int compare = node.element.compareTo(element);

        if(compare != 0) {
            if(compare > 0) {
                remove(node.leftChild, element);
            } else {
                remove(node.rightChild, element);
            }
            return;
        }

        RedBlackNode<T> candidate = (RedBlackNode<T>) node;
        BinarySearchTreeNode<T> parent = node.parent;

        if(candidate.rightChild == sentinel || candidate.leftChild == sentinel) {
            RedBlackNode<T> child = candidate.rightChild == sentinel ? candidate.getLeftChildAsRedBlackNode() : candidate.getRightChildAsRedBlackNode();
            if(candidate.color == Color.BLACK) {
                if (child.color == Color.BLACK) {
                    removeCaseOne(candidate);
                } else {
                    child.color = Color.BLACK;
                }
            }

            if(parent != null) {
                if(parent.leftChild == candidate) {
                    parent.leftChild = child;
                }else {
                    parent.rightChild = child;
                }
            } else {
                root = child;
            }
            if(child != sentinel) {
                child.parent = parent;
            }

            //cleanup
            candidate.element = null;
            candidate.leftChild = candidate.rightChild = null;
            --nodeCount;

        } else {
            T successor = BstUtils.preOrderSuccessor(candidate.leftChild, sentinel);
            candidate.element = successor;
            remove(candidate.leftChild, successor);
        }

    }

    private void removeCaseOne(RedBlackNode<T> doubleBlackNode) {
        if(doubleBlackNode == root) {
            doubleBlackNode.color = Color.BLACK;
        }else {
            removeCaseTwo(doubleBlackNode, doubleBlackNode.getParentAsRedBlackNode());
        }
    }

    private void removeCaseTwo(RedBlackNode<T> doubleBlackNode, RedBlackNode<T> parent) {
        RedBlackNode<T> sibling = parent.leftChild == doubleBlackNode? parent.getRightChildAsRedBlackNode() : parent.getLeftChildAsRedBlackNode();
        if(sibling.color == Color.RED && parent.color == Color.BLACK) {
            if(sibling == parent.leftChild) {
                BstUtils.leftLeftCase(sibling, this);
            }else {
                BstUtils.rightRightCase(sibling, this);
            }
            parent.color = Color.RED;
            sibling.color = Color.BLACK;
            removeCaseFour(doubleBlackNode, parent);
        } else {
            removeCaseThree(doubleBlackNode, parent, sibling);
        }
    }

    private void removeCaseThree(RedBlackNode<T> doubleBlackNode, RedBlackNode<T> parent, RedBlackNode<T> sibling) {
        if(parent.color == Color.BLACK && sibling.color == Color.BLACK) {
            if(sibling.getLeftChildAsRedBlackNode().color == Color.BLACK && sibling.getRightChildAsRedBlackNode().color == Color.BLACK) {
                sibling.color = Color.RED;
                removeCaseOne(parent);
                return;
            }
        }
        removeCaseFour(doubleBlackNode, parent);
    }

    private void removeCaseFour(RedBlackNode<T> doubleBlackNode, RedBlackNode<T> parent) {
        RedBlackNode<T> sibling = parent.leftChild == doubleBlackNode? parent.getRightChildAsRedBlackNode() : parent.getLeftChildAsRedBlackNode();
        if(parent.color == Color.RED && sibling.color == Color.BLACK) {
            if(sibling.getLeftChildAsRedBlackNode().color == Color.BLACK && sibling.getRightChildAsRedBlackNode().color == Color.BLACK) {
                parent.color = Color.BLACK;
                sibling.color = Color.RED;
                return;
            }
        }
        removeCaseFive(doubleBlackNode, parent);
    }

    private void removeCaseFive(RedBlackNode<T> doubleBlackNode, RedBlackNode<T> parent) {
        RedBlackNode<T> sibling = parent.leftChild == doubleBlackNode ? parent.getRightChildAsRedBlackNode() : parent.getLeftChildAsRedBlackNode();
        RedBlackNode<T> newSibling = sibling;
        if(sibling == parent.leftChild) {
            if(sibling.getRightChildAsRedBlackNode().color == Color.RED && sibling.getLeftChildAsRedBlackNode().color == Color.BLACK) {
                newSibling = sibling.getRightChildAsRedBlackNode();
                sibling.color = Color.RED;
                sibling.getRightChildAsRedBlackNode().color = Color.BLACK;
                BstUtils.rightRightCase(sibling.rightChild, this);
            }
        }else {
            if(sibling.getLeftChildAsRedBlackNode().color == Color.RED && sibling.getRightChildAsRedBlackNode().color == Color.BLACK) {
                newSibling = sibling.getLeftChildAsRedBlackNode();
                sibling.color = Color.RED;
                sibling.getLeftChildAsRedBlackNode().color = Color.BLACK;
                BstUtils.leftLeftCase(sibling.leftChild, this);
            }
        }

        removeCaseSix(doubleBlackNode, parent, newSibling);
    }

    private void removeCaseSix(RedBlackNode<T> doubleBlackNode, RedBlackNode<T> parent, RedBlackNode<T> sibling) {
        Color parentColor = parent.color;
        if(sibling == parent.leftChild && sibling.getLeftChildAsRedBlackNode().color == Color.RED) {
            parent.color = sibling.color; //always black
            sibling.color = parentColor;
            sibling.getLeftChildAsRedBlackNode().color = Color.BLACK;
            BstUtils.leftLeftCase(sibling, this);
        }else if(sibling == parent.rightChild && sibling.getRightChildAsRedBlackNode().color == Color.RED) {
            parent.color = sibling.color; //always black
            sibling.color = parentColor;
            sibling.getRightChildAsRedBlackNode().color = Color.BLACK;
            BstUtils.rightRightCase(sibling, this);
        }
    }

    public void printTree() {
        if(size() > 0) {
            Queue<RedBlackNode<T>> nodeQueue = new Queue<>();
            nodeQueue.push((RedBlackNode<T>) root);
            while(!nodeQueue.empty()) {
                if(nodeQueue.peek().leftChild != sentinel) {
                    nodeQueue.push(nodeQueue.peek().getLeftChildAsRedBlackNode());
                }
                if(nodeQueue.peek().rightChild != sentinel) {
                    nodeQueue.push(nodeQueue.peek().getRightChildAsRedBlackNode());
                }
                System.out.print(nodeQueue.peek().element + "(" + nodeQueue.pop().color + "), ");
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        RedBlackTree<Integer> redBlackTree = new RedBlackTree<>();
        for (int i = 0; i < 20; i++) {
            redBlackTree.insert(i);
        }
        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print(">>> ");
            if(scanner.hasNextInt()) {
                redBlackTree.remove(scanner.nextInt());
            }else break;
            redBlackTree.printTree();
        }

    }
}

//10 6 7 13 4 5 0 19
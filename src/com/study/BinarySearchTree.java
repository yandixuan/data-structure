package com.study;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 排序二叉树
 * <p>
 * 1.若左子树不空，则左子树上所有结点的值均小于它的根结点的值
 * 2.若右子树不空，则右子树上所有结点的值均大于它的根结点的值
 * 3.左、右子树也分别为二叉排序树；
 *
 * @author ydx
 */
public class BinarySearchTree<E extends Comparable<E>> {

    static class TreeNode<E> {
        E element;
        TreeNode<E> parent;
        TreeNode<E> left;
        TreeNode<E> right;

        public TreeNode(E element, TreeNode<E> parent) {
            this.element = element;
            this.parent = parent;
        }
    }

    private TreeNode<E> root;
    private int size;

    public BinarySearchTree() {
    }

    /**
     * 循环插入
     *
     * @param element
     */
    public boolean insert(E element) {
        if (root == null) {
            root = new TreeNode<>(element, null);
            ++size;
            return true;
        }
        for (TreeNode<E> cur = root; ; ) {
            int comp = element.compareTo(cur.element);
            if (comp == 0) {
                return false;
            }
            TreeNode<E> p = cur;
            cur = comp < 0 ? cur.left : cur.right;
            if (cur == null) {
                if (comp < 0) {
                    p.left = new TreeNode<>(element, p);
                } else {
                    p.right = new TreeNode<>(element, p);
                }
                ++size;
                return true;
            }
        }
    }

    public void remove(E element) {

    }

    public int height() {
        return height(root);
    }

    private int height(TreeNode<E> node) {
        if (node == null) {
            return 0;
        }
        return Math.max(height(node.left), height(node.right)) + 1;
    }

    /**
     * 前序遍历迭代器
     */
    public Iterator<E> preOrderTraversal() {
        final int expectedNodeCount = size;
        final Deque<TreeNode<E>> stack = new LinkedList<>();
        stack.push(root);
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                if (expectedNodeCount != size) {
                    throw new java.util.ConcurrentModificationException();
                }
                return root != null && !stack.isEmpty();
            }

            @Override
            public E next() {
                if (expectedNodeCount != size) {
                    throw new java.util.ConcurrentModificationException();
                }
                TreeNode<E> node = stack.pop();
                if (node.right != null) {
                    stack.push(node.right);
                }
                if (node.left != null) {
                    stack.push(node.left);
                }
                return node.element;
            }
        };
    }

    public Iterator<E> inOrderTraversal() {

        final int expectedNodeCount = size;
        final Deque<TreeNode<E>> stack = new LinkedList<>();
        stack.push(root);

        return new Iterator<>() {
            TreeNode<E> curLeft = root;

            @Override
            public boolean hasNext() {
                if (expectedNodeCount != size) {
                    throw new java.util.ConcurrentModificationException();
                }
                return root != null && !stack.isEmpty();
            }

            @Override
            public E next() {
                if (expectedNodeCount != size) {
                    throw new java.util.ConcurrentModificationException();
                }
                while (curLeft != null && curLeft.left != null) {
                    stack.push(curLeft.left);
                    curLeft = curLeft.left;
                }
                TreeNode<E> node = stack.pop();
                if (node.right != null) {
                    stack.push(node.right);
                    curLeft = node.right;
                }
                return node.element;
            }
        };
    }

    public Iterator<E> postOrderTraversal() {
        final int expectedNodeCount = size;

        final Deque<TreeNode<E>> stack1 = new LinkedList<>();
        final Deque<TreeNode<E>> stack2 = new LinkedList<>();
        stack1.push(root);
        while (!stack1.isEmpty()) {
            TreeNode<E> node = stack1.pop();
            if (node != null) {
                stack2.push(node);
                if (node.left != null) {
                    stack1.push(node.left);
                }
                if (node.right != null) {
                    stack1.push(node.right);
                }
            }
        }
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                if (expectedNodeCount != size) {
                    throw new java.util.ConcurrentModificationException();
                }
                return root != null && !stack2.isEmpty();
            }

            @Override
            public E next() {
                if (expectedNodeCount != size) {
                    throw new java.util.ConcurrentModificationException();
                }
                return stack2.pop().element;
            }
        };
    }


    public static void main(String[] args) {
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.insert(10);
        tree.insert(12);
        tree.insert(123);
        tree.insert(122);
        tree.insert(19);
        tree.insert(20);
        Iterator<Integer> order = tree.postOrderTraversal();

        while (order.hasNext()) {
            Integer element = order.next();
            System.out.println(element);
        }
    }


}

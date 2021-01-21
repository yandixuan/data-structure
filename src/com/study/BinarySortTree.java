package com.study;

/**
 * definition
 * 1.若左子树不空，则左子树上所有结点的值均小于它的根结点的值
 * 2.若右子树不空，则右子树上所有结点的值均大于它的根结点的值
 * 3.左、右子树也分别为二叉排序树；
 *
 * @author ydx
 */
public class BinarySortTree<E> {

    static class TreeNode<E> {
        E element;
        TreeNode<E> parent;
        TreeNode<E> left;
        TreeNode<E> right;

        public TreeNode(E element, TreeNode<E> parent) {
            this.element = element;
            this.parent = parent;
        }

        public TreeNode<E> getLeft() {
            return left;
        }

        public void setLeft(TreeNode<E> left) {
            this.left = left;
        }

        public TreeNode<E> getRight() {
            return right;
        }

        public void setRight(TreeNode<E> right) {
            this.right = right;
        }
    }

    private TreeNode<E> root;


}

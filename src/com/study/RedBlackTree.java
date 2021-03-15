package com.study;

/**
 * 1. 每个结点的颜色为红色或者黑色。
 * <p>
 * 2. 根结点是黑色的。
 * <p>
 * 3. 每个叶结点是黑色的（叶结点是指NIL结点）。
 * <p>
 * 4. 如果一个结点是红色的，则它的两个子结点都是黑色的。（树中不存在两个连续的红色结点）。
 * <p>
 * 5. 对任意结点，从该结点到其后代叶结点的简单路径上，均包含相同数目的黑色结点。
 * ————————————————
 *
 * @author ydx
 */
public class RedBlackTree<T extends Comparable<T>> {


    static class Node<T> {
        private T value;
        private Node<T> left;
        private Node<T> right;
        private Node<T> parent;
        private boolean red;

        Node(T value) {
            this.value = value;
        }

        Node() {

        }

        boolean isRed() {
            return red;
        }

        boolean isBlack() {
            return !red;
        }


        public void setLeft(Node<T> left) {
            this.left = left;
        }

        public void setRight(Node<T> right) {
            this.right = right;
        }

        public void setParent(Node<T> parent) {
            this.parent = parent;
        }

        public void setRed(boolean red) {
            this.red = red;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Node<T> getLeft() {
            return left;
        }

        public Node<T> getRight() {
            return right;
        }

        public Node<T> getParent() {
            return parent;
        }
    }

    private Node<T> root;

    public RedBlackTree() {

    }

    /**
     * 左旋转
     * <p>
     * //      node                                       x
     * //     /    \              左旋转                /    \
     * //    T1     x           --------->            node    T3
     * //         /   \                              /    \
     * //       T2     T3                           T1    T2
     *
     * @param node
     */
    private Node<T> leftRotate(Node<T> node) {
        Node<T> x = node.right;
        node.right = x.left;
        x.left = node;
        x.red = node.red;
        return x;
    }


    public T addNode(T value) {
        Node<T> t = new Node<T>(value);
        return addNode(t);
    }

    /**
     * 因为着为红色没有破坏性质5，但可能破坏性质4，而着为黑色一定破坏了性质5，破坏了性质5修复起来较困难，所以我们选择将插入结点着为红色。
     *
     * @param node
     * @return
     */
    private T addNode(Node<T> node) {
        node.setLeft(null);
        node.setRight(null);
        node.setRed(true);
        if (root == null) {
            node.setParent(null);
            this.root = node;
            root.setRed(false);
        } else {
            Node<T> parent = findParentNode(node);
            int cmp = parent.getValue().compareTo(node.getValue());
            node.setParent(parent);
            if (cmp > 0) {
                parent.setLeft(node);
            } else {
                parent.setRight(node);
            }
            fixInsert(node);
        }
        return null;
    }

    private void fixInsert(Node<T> x) {
        Node<T> parent = x.getParent();

        while (parent != null && parent.isRed()) {
            Node<T> uncle = getUncle(x);
            if (uncle == null) {
                //need to rotate
                Node<T> ancestor = parent.getParent();
                //ancestor is not null due to before before add,tree color is balance
                if (parent == ancestor.getLeft()) {
                    boolean isRight = x == parent.getRight();
                    if (isRight) {
                        rotateLeft(parent);
                    }
                    rotateRight(ancestor);
                    if (isRight) {
                        x.setRed(false);
                        parent = null;
                        //end loop
                    } else {
                        parent.setRed(false);
                    }
                    ancestor.setRed(true);
                } else {
                    boolean isLeft = x == parent.getLeft();
                    if (isLeft) {
                        rotateRight(parent);
                    }
                    rotateLeft(ancestor);
                    if (isLeft) {
                        x.setRed(false);
                        parent = null;//end loop
                    } else {
                        parent.setRed(false);
                    }
                    ancestor.setRed(true);
                }
            } else {//uncle is red
                parent.setRed(false);
                uncle.setRed(false);
                parent.getParent().setRed(true);
                x = parent.getParent();
                parent = x.getParent();
            }
        }
        getRoot().makeBlack();
        getRoot().setParent(null);
    }

    private Node<T> getUncle(Node<T> node) {
        Node<T> parent = node.getParent();
        Node<T> ancestor = parent.getParent();
        if (ancestor == null) {
            return null;
        }
        if (parent == ancestor.getLeft()) {
            return ancestor.getRight();
        } else {
            return ancestor.getLeft();
        }
    }

    private Node<T> findParentNode(Node<T> x) {
        Node<T> dataRoot = this.root;
        Node<T> child = dataRoot;
        while (child != null) {
            int cmp = child.getValue().compareTo(x.getValue());
            if (cmp == 0) {
                return child;
            }
            // child > x
            if (cmp > 0) {
                child = child.getLeft();
            }
            // child < x
            else {
                child = child.getRight();
            }
            dataRoot = child;
        }
        return dataRoot;
    }


}
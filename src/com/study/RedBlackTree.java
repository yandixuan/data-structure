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

        @Override
        public String toString() {
            return value.toString();
        }

        Node(T value) {
            this.value = value;
        }

        Node() {

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

        public void setLeft(Node<T> left) {
            this.left = left;
        }

        public Node<T> getRight() {
            return right;
        }

        public void setRight(Node<T> right) {
            this.right = right;
        }

        public Node<T> getParent() {
            return parent;
        }

        public void setParent(Node<T> parent) {
            this.parent = parent;
        }

        public boolean isRed() {
            return red;
        }

        public void setRed(boolean red) {
            this.red = red;
        }
    }

    private Node<T> root;

    public RedBlackTree() {

    }

    /**
     * 删除节点
     * <p>
     * 1.删除红色节点，不会影响黑高，也不会违反性质4
     * 2.但是删除黑色的节点需要调整
     *
     * @param value
     * @return
     */
    public T remove(T value) {
        Node<T> dataRoot = root;
        Node<T> parent = root;

        while (dataRoot != null) {
            int cmp = dataRoot.getValue().compareTo(value);
            if (cmp < 0) {
                parent = dataRoot;
                dataRoot = dataRoot.getRight();
            } else if (cmp > 0) {
                parent = dataRoot;
                dataRoot = dataRoot.getLeft();
            } else {
                // 当匹配了节点
                if (dataRoot.getRight() != null) {
                    // 调取节点的右子树最小的节点
                    Node<T> min = removeMin(dataRoot.getRight());
                    //
                    

                }
                // 需要删除的节点的右子树为null
                else {


                }

                return dataRoot.getValue();
            }

        }
        return null;
    }


    /**
     * 当前需要调整的节点 x
     * x的兄弟s
     * x,s的父亲p
     * s的left sn
     * s的right sn
     * <p>
     * <p>
     * case1:
     *
     * @param node
     */
    private void fixRemove(Node<T> node) {


    }


    private Node<T> getSibling(Node<T> node, Node<T> parent) {
        parent = node == null ? parent : node.getParent();
        if (node == null) {
            return parent.getLeft() == null ? parent.getRight() : parent.getLeft();
        }
        if (node == parent.getLeft()) {
            return parent.getRight();
        } else {
            return parent.getLeft();
        }
    }

    /**
     * 找节点对应子树的最小值
     *
     * @param node
     * @return
     */
    private Node<T> removeMin(Node<T> node) {
        //find the min node
        Node<T> parent = node;
        while (node != null && node.getLeft() != null) {
            parent = node;
            node = node.getLeft();
        }
        //remove min node
        if (parent == node) {
            return node;
        }

        parent.setLeft(node.getRight());
        if (node.getRight() != null) {
            node.getRight().parent = parent;
        }
        return node;
    }


    /**
     * 左旋转 (eg:对node节点进行左旋转即node逆时针)
     * <p>
     * //         node                                       x
     * //        /    \              左旋转                /    \
     * //       T1     x           --------->            node    T3
     * //            /   \                              /    \
     * //          T2     T3                           T1    T2
     *
     * @param node
     */
    private Node<T> rotateLeft(Node<T> node) {
        Node<T> right = node.right;
        Node<T> parent = node.parent;

        if (right.getLeft() != null) {
            node.setRight(right.getLeft());
            right.left.parent = node;
        } else {
            node.right = null;
        }

        right.setLeft(node);
        node.setParent(right);

        if (parent == null) {
            right.parent = null;
            root = right;
        } else {
            if (parent.getLeft() == node) {
                parent.setLeft(right);
            } else {
                parent.setRight(right);
            }
            right.parent = parent;
        }
        return root;
    }

    /**
     * 右旋转
     * //            node                                       x
     * //           /    \              右旋转                /    \
     * //          x     T2           --------->             y     node
     * //        /  \                                             /    \
     * //       y   T1                                           T1    T2
     *
     * @param node
     */
    private Node<T> rotateRight(Node<T> node) {
        Node<T> left = node.left;
        Node<T> parent = node.parent;

        if (left.getRight() != null) {
            node.setLeft(left.getRight());
            left.right.parent = node;
        } else {
            node.left = null;
        }

        left.setRight(node);
        node.parent = left;
        if (parent == null) {
            left.parent = null;
            root = left;
        } else {
            if (parent.getLeft() == node) {
                parent.setLeft(left);
            } else {
                parent.setRight(left);
            }
            left.parent = parent;
        }
        return left;
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
            root = node;
            root.setRed(false);
        } else {
            Node<T> parent = findParentNode(node);
            int cmp = parent.getValue().compareTo(node.getValue());
            node.setParent(parent);
            if (cmp == 0) {
                return node.getValue();
            } else if (cmp > 0) {
                parent.setLeft(node);
            } else {
                parent.setRight(node);
            }
            fixInsert(node);
        }

        return null;
    }

    /**
     * 插入修复：
     * 1.如果插入的是根节点那么直接设置黑色 over
     * 2.如果父节点是黑色那么符合红黑树定义 over
     * 3.如果父节点是红色那么不符合红黑树定义需要做插入修复
     * <p>
     * 这里我们分别命名
     * 插入节点为x
     * x的父亲节点为p
     * p的父亲节点为g
     * g的不为p的子节点为y即x的叔叔节点
     * <p>
     * 如果 x,p为红色，根据红黑树定义g必为黑色，
     * case1: 如果y是红色 那么我们就将 p.y染成黑色，g染成红色 这样指针就回溯至g
     * case2: 如果y是黑色 x为p的右子树 左旋p x指向p 变成case 3
     * case3: 如果y是黑色 x为p的左子树 p染黑 g染红 右旋g 结束
     *
     * @param x
     */
    private void fixInsert(Node<T> x) {
        Node<T> p = x.getParent();
        // 如果p不为空且是红色循环继续
        while (p != null && p.isRed()) {
            Node<T> y = getUncle(x);
            // 如果y为null则视为黑色
            if (y == null) {
                Node<T> ancestor = p.getParent();
                // 如果p是左子树
                if (p == ancestor.getLeft()) {
                    boolean isRight = x == p.getRight();
                    // x是p的右子树 这里就是case2了
                    if (isRight) {
                        rotateLeft(p);
                    }
                    // 左旋完p后 右旋g 那么 root为p了
                    rotateRight(ancestor);
                    if (isRight) {
                        // 将x染黑
                        x.setRed(false);
                        p = null;
                        //end loop
                    } else {
                        // p染黑
                        p.setRed(false);
                    }
                    // 祖先染红（祖先节点已经成了x的子节点了）
                    ancestor.setRed(true);
                    // 这一步为止p已经是现在子树的跟借点了 而且是黑色 满足红黑树特性故循环也结束了
                }
                // p是右子树
                else {
                    boolean isLeft = x == p.getLeft();
                    // 右旋p节点
                    if (isLeft) {
                        rotateRight(p);
                    }
                    // 左旋g节点
                    rotateLeft(ancestor);
                    if (isLeft) {
                        // 因为之前右旋了p节点  x为p的父节点 将x染黑
                        x.setRed(false);
                        // 结束循环
                        p = null;
                    } else {
                        // 将p染黑
                        p.setRed(false);
                    }
                    // 祖先染红（祖先节点已经成了x的子节点了）
                    ancestor.setRed(true);
                    // 这一步为止p已经是现在子树的跟借点了 而且是黑色 满足红黑树特性故循环也结束了
                }
            } else {
                // 如果p y都是红色 p,y均染黑g染红 指针回溯至g点继续循环修正
                p.setRed(false);
                y.setRed(false);
                p.getParent().setRed(true);

                x = p.getParent();
                p = x.getParent();
            }
        }
        root.setRed(false);
    }

    /**
     * 获取叔叔节点
     *
     * @param node
     * @return
     */
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
        Node<T> dataRoot = root;
        Node<T> child = dataRoot;
        while (child != null) {
            int cmp = child.getValue().compareTo(x.getValue());
            if (cmp == 0) {
                return child;
            }
            dataRoot = child;
            // child > x
            if (cmp > 0) {
                child = child.getLeft();
            }
            // child < x
            else {
                child = child.getRight();
            }
        }
        return dataRoot;
    }

    public void printTree(Node<T> root) {
        java.util.LinkedList<Node<T>> queue = new java.util.LinkedList<>();
        java.util.LinkedList<Node<T>> queue2 = new java.util.LinkedList<>();
        if (root == null) {
            return;
        }
        queue.add(root);
        boolean firstQueue = true;

        while (!queue.isEmpty() || !queue2.isEmpty()) {
            java.util.LinkedList<Node<T>> q = firstQueue ? queue : queue2;
            Node<T> n = q.poll();

            if (n != null) {
                String pos = n.getParent() == null ? "" : (n == n.getParent().getLeft()
                        ? " LE" : " RI");
                String pstr = n.getParent() == null ? "" : n.getParent().toString();
                String cstr = n.isRed() ? "R" : "B";
                cstr = n.getParent() == null ? cstr : cstr + " ";
                System.out.print(n + "(" + (cstr) + pstr + (pos) + ")" + "\t");
                if (n.getLeft() != null) {
                    (firstQueue ? queue2 : queue).add(n.getLeft());
                }
                if (n.getRight() != null) {
                    (firstQueue ? queue2 : queue).add(n.getRight());
                }
            } else {
                System.out.println();
                firstQueue = !firstQueue;
            }
        }
    }

    public static void main(String[] args) {
        RedBlackTree<String> tree = new RedBlackTree<>();
        tree.addNode("d");
        tree.addNode("d");
        tree.addNode("c");
        tree.addNode("c");
        tree.addNode("b");
        tree.addNode("f");

        tree.addNode("a");
        tree.addNode("e");

        tree.addNode("g");
        tree.addNode("h");

        tree.printTree(tree.root);
        System.out.println("");
        tree.remove("c");
        tree.printTree(tree.root);
    }

}
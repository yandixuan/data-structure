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
     * 删除一个新的节点有以下四种情况：
     * 1. 删除的节点是叶子节点（非Nil）
     * 2. 删除的节点只有左子树
     * 3. 删除的节点只有右子树
     * 4. 删除的节点同时拥有左子树和右子树
     * 其实只有上面前三种情况，对于第四种情况，可以找到待删除节点的直接后继节点，用这个节点的值代替待删除节点，
     * 接着情况转变为删除这个直接后继节点，情况也变为前三种之一。
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
                T data = dataRoot.value;
                if (dataRoot.left == null && dataRoot.right == null) {
                    if (dataRoot == root) {
                        root = null;
                    } else {
                        if (!dataRoot.isRed()) {
                            fixRemove(dataRoot);
                        } else {
                            if (parent.getLeft() == dataRoot) {
                                parent.left = null;
                            }
                            if (parent.getRight() == dataRoot) {
                                parent.right = null;
                            }
                        }
                    }
                }
                // 删除节点存在左右子树
                else if (dataRoot.left != null && dataRoot.right != null) {
                    Node<T> x = removeMin(dataRoot.getRight());
                    // x只为叶子节点
                    T tmp = x.value;
                    x.value = dataRoot.value;
                    dataRoot.value = tmp;

                    if (!x.isRed()) {
                        fixRemove(x);
                    } else {
                        Node<T> p = x.parent;
                        if (p.getLeft() == x) {
                            p.left = null;
                        }
                        if (p.getRight() == x) {
                            p.right = null;
                        }
                        x.parent = null;
                    }
                }
                // 删除的节点只有左子树或者右子树 那么节点为了满足红黑树性质 必然满足 删除节点黑色 子节点红色 直接交换值
                // 必然是 黑->红 Null也是算黑高的
                else {
                    if (dataRoot.getRight() != null) {
                        Node<T> right = dataRoot.getRight();
                        dataRoot.value = right.value;
                        dataRoot.right = null;
                    }
                    if (dataRoot.getLeft() != null) {
                        Node<T> left = dataRoot.getLeft();
                        dataRoot.value = left.value;
                        dataRoot.left = null;
                    }
                }
                return data;
            }
        }
        return null;
    }


    /**
     * 当前需要调整的节点 x
     * x的兄弟s
     * x,s的父亲p
     * s的left ln
     * s的right rn
     * <p>
     * <p>
     * case1:
     *
     * @param node
     */
    private void fixRemove(Node<T> node) {
        Node<T> x = node;
        Node<T> p = node.parent;
        Node<T> s = getSibling(x, p);
        Node<T> ln = s.left;
        Node<T> rn = s.right;
        // 当叶子节点为黑色的时候

        for (; ; ) {
            //  case1: s为红色 s染黑 p染红左旋p
            if (s.isRed()) {
                s.setRed(false);
                p.setRed(true);
                rotateLeft(p);
            } else if (rn != null && rn.isRed()) {
                boolean isRedTmp = p.isRed();
                p.red = s.red;
                s.red = isRedTmp;
                rn.red = false;
                rotateLeft(p);
                if (p.getLeft() == x) {
                    p.left = null;
                    x.parent = null;
                }
                break;
            } else if (ln != null && ln.isRed()) {
                ln.red = false;
                s.red = true;
                rotateRight(s);
            } else if (p.isRed()) {
                p.red = false;
                s.red = true;
                if (p.getLeft() == x) {
                    p.left = null;
                    x.parent = null;
                }
                break;
            } else {
                s.red = true;
                getSibling(p, p.parent).red = true;
                break;
            }


        }
        if (root != null) {
            root.red = false;
        }
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
        while (node != null && node.getLeft() != null) {
            node = node.getLeft();
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
        tree.addNode("c");
        tree.addNode("b");
        tree.addNode("f");

        tree.addNode("a");
        tree.addNode("e");

        tree.addNode("g");
        tree.addNode("h");

        tree.printTree(tree.root);
        System.out.println("");
        tree.remove("b");
        tree.printTree(tree.root);
    }

}
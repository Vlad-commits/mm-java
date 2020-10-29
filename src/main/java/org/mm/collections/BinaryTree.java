package org.mm.collections;

import java.util.Comparator;
import java.util.Optional;

import static java.util.Optional.*;

class BinaryTree<K, V> {
  private final Comparator<? super K> comparator;

  K key;
  V value;
  BinaryTree<K, V> left;
  BinaryTree<K, V> right;
  BinaryTree<K, V> parent;
  int depth = 0;

  private BinaryTree(Comparator<? super K> comparator) {
    this(comparator, null, null, null, null);
  }

  private BinaryTree(Comparator<? super K> comparator, K key, V value) {
    this.comparator = comparator;
    this.key = key;
    this.value = value;
    this.depth = 1;
  }

  private BinaryTree(Comparator<? super K> comparator, K key, V value, BinaryTree<K, V> left, BinaryTree<K, V> right) {
    this.comparator = comparator;
    this.key = key;
    this.value = value;
    setLeft(left);
    setRight(right);
    recalculateDepthBasedOnChildren();
  }

  public static <P extends Comparable<P>, T> BinaryTree<P, T> create() {
    return new BinaryTree<>(Comparator.naturalOrder());
  }

  public static <P, T> BinaryTree<P, T> create(Comparator<? super P> comparator) {
    return new BinaryTree<>(comparator);
  }

  private static int depth(BinaryTree<?, ?> node) {
    return node == null
           ? 0
           : node.depth;
  }


  public V put(K key, V value) {
    checkNotNull(key);

    if (this.key == null) {
      this.key = key;
      this.value = value;
      this.depth++;
      return null;
    } else {
      final var compare = comparator.compare(key, this.key);
      if (compare == 0) {
        final var currentValue = this.value;
        this.key = key;
        this.value = value;
        return currentValue;
      } else if (compare < 0) {
        final var left = this.getLeft();
        if (left == null) {
          this.setLeft(new BinaryTree<K, V>(comparator, key, value));
        } else {
          left.put(key, value);
        }
      } else {
        final var right = this.getRight();
        if (right == null) {
          this.setRight(new BinaryTree<K, V>(comparator, key, value));
        } else {
          this.right.put(key, value);
        }
      }

      balance();
      return null;
    }
  }

  public V remove(K key) {
    final var nodeOpt = findExactNode(key);
    if (nodeOpt.isEmpty()) {
      return null;
    }

    final var node = nodeOpt.get();
    if (node.isLeaf()) {
      final var value = node.value;
      node.remove();
      return value;
    }

    if (depth(node.left) <= depth(node.right)) {
      final var celingNode = node.right.findCeilingNode(key).orElseThrow(IllegalStateException::new);
      final var value = node.value;
      node.key = celingNode.key;
      node.value = celingNode.value;
      celingNode.remove();
      return value;
    } else {
      final var floorNode = node.left.findFloorNode(key).orElseThrow(IllegalStateException::new);
      final var value = node.value;
      node.key = floorNode.key;
      node.value = floorNode.value;
      floorNode.remove();
      return value;
    }
  }

  public boolean contains(K key) {
    return findExactNode(key).isPresent();
  }

  public V find(K key) {
    return findExactNode(key).map(node -> node.value).orElse(null);
  }

  void balance() {
    if (simpleRightRotationRequired()) {
      simpleRightRotation();
    } else if (simpleLeftRotationRequired()) {
      simpleLeftRotation();
    } else if (doubleLeftRotationRequired()) {
      doubleLeftRotation();
    } else if (doubleRightRotationRequired()) {
      doubleRightRotation();
    }

    recalculateDepthBasedOnChildren();
  }

  void simpleLeftRotation() {
    if (!simpleLeftRotationRequired()) {
      return;
    }
    var aKey = this.key;
    var aValue = this.value;
    var bKey = right.key;
    var bValue = right.value;
    var L = left;
    var C = right.getLeft();
    var R = right.getRight();

    this.key = bKey;
    this.value = bValue;
    this.setLeft(new BinaryTree<>(comparator, aKey, aValue, L, C));
    this.setRight(R);
  }

  void doubleLeftRotation() {
    if (!doubleLeftRotationRequired()) {
      return;
    }

    var aKey = this.key;
    var aValue = this.value;
    var bKey = right.key;
    var bValue = right.value;
    var cKey = right.getLeft().key;
    var cValue = right.getLeft().value;
    var L = left;
    var M = right.getLeft().getLeft();
    var N = right.getLeft().getRight();
    var R = right.getRight();

    this.key = cKey;
    this.value = cValue;
    this.setLeft(new BinaryTree<>(comparator, aKey, aValue, L, M));
    this.setRight(new BinaryTree<>(comparator, bKey, bValue, N, R));
  }

  void simpleRightRotation() {
    if (!simpleRightRotationRequired()) {
      return;
    }
    var aKey = this.key;
    var aValue = this.value;
    var bKey = this.getLeft().key;
    var bValue = this.getLeft().value;
    var L = left.getLeft();
    var C = left.getRight();
    var R = right;
    this.key = bKey;
    this.value = bValue;
    this.setLeft(L);
    this.setRight(new BinaryTree<>(comparator, aKey, aValue, C, R));
  }

  void doubleRightRotation() {
    if (!doubleRightRotationRequired()) {
      return;
    }
    var aKey = this.key;
    var aValue = this.value;
    var bKey = left.key;
    var bValue = left.value;
    var cKey = left.getRight().key;
    var cValue = left.getRight().value;
    var L = left.getLeft();
    var M = left.getRight().getLeft();
    var N = left.getRight().getRight();
    var R = right;

    this.key = cKey;
    this.value = cValue;
    this.setLeft(new BinaryTree<>(comparator, bKey, bValue, L, M));
    this.setRight(new BinaryTree<>(comparator, aKey, aValue, N, R));
  }

  private boolean simpleLeftRotationRequired() {
    return depth(left) + 2 == depth(right) && depth(right.getLeft()) <= depth(right.getRight());
  }

  private boolean doubleLeftRotationRequired() {
    return depth(left) + 2 == depth(right) && depth(right.getLeft()) > depth(right.getRight());
  }

  private boolean simpleRightRotationRequired() {
    return depth(left) == depth(right) + 2 && depth(left.getRight()) <= depth(left.getLeft());
  }

  private boolean doubleRightRotationRequired() {
    return depth(left) == depth(right) + 2 && depth(left.getRight()) > depth(left.getLeft());
  }

  private void recalculateDepthBasedOnChildren() {
    this.depth = Math.max(depth(this.left), depth(this.right)) + (
        this.key == null
        ? 0
        : 1
    );
  }


  private Optional<BinaryTree<K, V>> findFloorNode(K element) {
    checkNotNull(element);
    if (key == null) {
      return Optional.empty();
    }

    final var compare = comparator.compare(element, key);
    if (compare == 0) {
      return of(this);
    }

    if (compare < 0) {
      return ofNullable(this.right).flatMap(node -> node.findFloorNode(element));
    } else {
      return of(ofNullable(this.right).flatMap(node -> node.findFloorNode(element)).orElse(this));
    }
  }

  private Optional<BinaryTree<K, V>> findCeilingNode(K element) {
    checkNotNull(element);
    if (key == null) {
      return Optional.empty();
    }

    final var compare = comparator.compare(element, key);
    if (compare == 0) {
      return of(this);
    }

    if (compare < 0) {
      return of(ofNullable(this.left).flatMap(node -> node.findCeilingNode(element)).orElse(this));
    } else {
      return ofNullable(this.right).flatMap(node -> node.findCeilingNode(element));
    }
  }

  private Optional<BinaryTree<K, V>> findExactNode(K element) {
    checkNotNull(element);
    if (key == null) {
      return Optional.empty();
    }

    final var compare = comparator.compare(element, key);
    if (compare == 0) {
      return of(this);
    }
    if (compare < 0) {
      return ofNullable(this.left).flatMap(node -> node.findExactNode(element));
    } else {
      return ofNullable(this.right).flatMap(node -> node.findExactNode(element));
    }
  }

  private void remove() {

    if (this.parent == null) {
      key = null;
      value = null;
      depth = 0;
      return;
    }
    if (this.parent.right == this) {
      this.parent.right = null;
    } else if (this.parent.left == this) {
      this.parent.left = null;
    }
    var p = parent;
    while (p != null) {
      p.balance();
      p = p.parent;
    }
  }

  private void checkNotNull(K element) {
    if (element == null) {
      throw new IllegalArgumentException("Binary tree doesn't support null values");
    }
  }

  private boolean isLeaf() {
    return this.left == null && this.right == null;
  }

  private BinaryTree<K, V> getLeft() {
    return left;
  }

  private void setLeft(BinaryTree<K, V> left) {
    this.left = left;
    if (left != null) {
      left.parent = this;
    }
  }

  private BinaryTree<K, V> getRight() {
    return right;
  }

  private void setRight(BinaryTree<K, V> right) {
    this.right = right;
    if (right != null) {
      right.parent = this;
    }
  }
}

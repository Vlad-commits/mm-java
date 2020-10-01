package org.mm.collections;

import java.util.*;

public class AnotherTreeMap<K, V> implements Map<K, V> {

  private final BinaryTree<K, V> tree;
  private int size = 0;

  public static <KEY, VALUE> AnotherTreeMap<KEY, VALUE> create(Comparator<? super KEY> keyComparator) {
    return new AnotherTreeMap<>(keyComparator);
  }

  public static <KEY extends Comparable<KEY>, VALUE> AnotherTreeMap<KEY, VALUE> create() {
    return new AnotherTreeMap<>(Comparator.naturalOrder());
  }

  private AnotherTreeMap(Comparator<? super K> keyComparator) {
    tree = BinaryTree.create(keyComparator);
  }


  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean containsKey(Object key) {
    return tree.contains((K) key);

  }

  @Override
  @SuppressWarnings("unchecked")
  public V get(Object key) {
    return tree.find((K) key);
  }

  @Override
  public V put(K key, V value) {
    final var add = tree.put(key, value);
    if (add == null) {
      size++;
      return null;
    }
    return add;
  }

  @Override
  @SuppressWarnings("unchecked")
  public V remove(Object key) {
    final var remove = tree.remove((K) key);
    if (remove == null) {
      return null;
    }
    size--;
    return remove;
  }

  @Override
  public boolean containsValue(Object value) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Set<K> keySet() {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Collection<V> values() {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    throw new UnsupportedOperationException("Not implemented");
  }
}

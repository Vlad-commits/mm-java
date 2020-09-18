package org.mm.collections;

import java.util.*;

public class AnotherLinkedList<T> extends AbstractList<T> implements List<T>, Queue<T> {
  private LinkedListNode<T> first;
  private LinkedListNode<T> last;
  private int size = 0;


  @Override
  public T get(int index) {
    checkElementIndex(index);
    return node(index).value;
  }

  @Override
  public T set(int index, T element) {
    checkElementIndex(index);
    final var node = node(index);
    final var old = node.value;
    node.value = element;
    return old;
  }

  @Override
  public boolean add(T t) {
    add(size(), t);
    return true;
  }

  @Override
  public void add(int index, T element) {
    checkPositionIndex(index);
    LinkedListNode<T> prev;
    LinkedListNode<T> next;
    if (index == 0) {
      prev = null;
      next = first;
    } else if (index == size) {
      prev = last;
      next = null;
    } else {
      prev = node(index - 1);
      next = prev.next;
    }
    final var newNode = new LinkedListNode<>(element, prev, next);

    if (prev != null) {
      prev.next = newNode;
    } else {
      first = newNode;
    }

    if (next != null) {
      next.prev = newNode;
    } else {
      last = newNode;
    }

    size++;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public T remove(int index) {
    checkElementIndex(index);
    final var node = node(index);
    LinkedListNode<T> next = node.next;
    LinkedListNode<T> prev = node.prev;

    if (prev != null) {
      prev.next = next;
    } else {
      first = next;
    }

    if (next != null) {
      next.prev = prev;
    } else {
      last = prev;
    }

    size--;
    return node.value;
  }


  @Override
  public boolean contains(Object o) {
    boolean result = false;
    var current = this.first;
    while (current != null) {
      if (Objects.equals(o, current.value)) {
        result = true;
        break;
      }
      current = current.next;
    }
    return result;
  }

  @Override
  public boolean offer(T t) {
    add(t);
    return true;
  }

  @Override
  public T remove() {
    try {
      return remove(0);
    } catch (IndexOutOfBoundsException e) {
      throw new NoSuchElementException("Queue is empty.");
    }
  }

  @Override
  public T poll() {
    try {
      return remove(0);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }

  @Override
  public T element() {
    try {
      return get(0);
    } catch (IndexOutOfBoundsException e) {
      throw new NoSuchElementException("Queue is empty.");
    }
  }

  @Override
  public T peek() {
    try {
      return get(0);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }

  private LinkedListNode<T> node(int index) {
    LinkedListNode<T> current = first;
    for (int i = 0; i < index; i++) {
      current = current.next;
    }
    return current;
  }

  private void checkElementIndex(int index) {
    if (!(index >= 0 && index < size)) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
  }

  private void checkPositionIndex(int index) {
    if (!(index >= 0 && index <= size)) {
      throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
  }
}

package org.mm.collections;

class LinkedListNode<T> {
  T value;
  LinkedListNode<T> prev;
  LinkedListNode<T> next;

  LinkedListNode(T value, LinkedListNode<T> prev, LinkedListNode<T> next) {
    this.value = value;
    this.prev = prev;
    this.next = next;
  }
}

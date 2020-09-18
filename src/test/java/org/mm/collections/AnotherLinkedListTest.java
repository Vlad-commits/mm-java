package org.mm.collections;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.mm.collections.TestHarness.*;

class AnotherLinkedListTest {
  @Test
  void addToEmpty() {
    List<Integer> actual = new AnotherLinkedList<>();
    List<Integer> expected = new LinkedList<>();

    testFunction(expected, actual, list -> list.add(1));
  }

  @Test
  void addToNotEmpty() {
    List<Integer> actual = new AnotherLinkedList<>();
    List<Integer> expected = new LinkedList<>();
    testFunction(expected, actual, list -> list.add(1));

    testFunction(expected, actual, list -> list.add(4));
  }

  @Test
  void isEmpty() {
    List<Integer> actual = new AnotherLinkedList<>();
    List<Integer> expected = new LinkedList<>();

    testFunction(expected, actual, List::isEmpty);
  }

  @Test
  void notIsEmpty() {
    List<Integer> actual = new AnotherLinkedList<>();
    List<Integer> expected = new LinkedList<>();
    testFunction(expected, actual, list -> list.add(1));

    testFunction(expected, actual, List::isEmpty);
  }

  @Test
  void sizeEmpty() {
    List<Integer> actual = new AnotherLinkedList<>();
    List<Integer> expected = new LinkedList<>();

    testFunction(expected, actual, List::size);
  }

  @Test
  void sizeNotEmpty() {
    List<Integer> actual = new AnotherLinkedList<>();
    List<Integer> expected = new LinkedList<>();
    testFunction(expected, actual, list -> list.add(1));

    testFunction(expected, actual, List::size);
  }


  @Test
  void getFromEmpty() {
    List<Integer> actual = new AnotherLinkedList<>();
    List<Integer> expected = new LinkedList<>();
    testFunction(expected, actual, list -> list.get(0));

    testFunction(expected, actual, List::isEmpty);
  }

  @Test
  void getFromNotEmpty() {
    List<Integer> actual = new AnotherLinkedList<>();
    List<Integer> expected = new LinkedList<>();
    testFunction(expected, actual, list -> list.add(1));
    testFunction(expected, actual, list -> list.add(4));

    testFunction(expected, actual, list -> list.get(0));
  }

  @Test
  void getNotExisting() {
    List<Integer> actual = new AnotherLinkedList<>();
    List<Integer> expected = new LinkedList<>();
    testFunction(expected, actual, list -> list.add(1));

    testFunction(expected, actual, list -> list.get(1));
  }

  @Test
  void getByNegativeIndex() {
    List<Integer> actual = new AnotherLinkedList<>();
    List<Integer> expected = new LinkedList<>();
    testFunction(expected, actual, list -> list.add(1));

    testFunction(expected, actual, list -> list.get(-1));
  }

  @Test
  void addInTheMiddle() {
    List<Integer> actual = new AnotherLinkedList<>();
    List<Integer> expected = new LinkedList<>();
    testFunction(expected, actual, list -> list.add(1));
    testFunction(expected, actual, list -> list.add(3));
    testFunction(expected, actual, list -> list.add(2));

    testAction(expected, actual, list -> list.add(1, 4));
  }

  @Test
  void addFirst() {
    List<Integer> actual = new AnotherLinkedList<>();
    List<Integer> expected = new LinkedList<>();
    testFunction(expected, actual, list -> list.add(1));
    testFunction(expected, actual, list -> list.add(3));
    testFunction(expected, actual, list -> list.add(2));

    testAction(expected, actual, list -> list.add(0, 4));
  }

  @Test
  void addLast() {
    List<Integer> actual = new AnotherLinkedList<>();
    List<Integer> expected = new LinkedList<>();
    testFunction(expected, actual, list -> list.add(1));
    testFunction(expected, actual, list -> list.add(3));
    testFunction(expected, actual, list -> list.add(2));

    testAction(expected, actual, list -> list.add(4, 4));
  }

  @Test
  void containsExisting() {
    List<Integer> actual = new AnotherLinkedList<>();
    List<Integer> expected = new LinkedList<>();
    testFunction(expected, actual, list -> list.add(1));
    testFunction(expected, actual, list -> list.add(3));
    testFunction(expected, actual, list -> list.add(2));

    testFunction(expected, actual, list -> list.contains(2));

  }

  @Test
  void containsNotExisting() {
    List<Integer> actual = new AnotherLinkedList<>();
    List<Integer> expected = new LinkedList<>();
    testFunction(expected, actual, list -> list.add(1));
    testFunction(expected, actual, list -> list.add(3));
    testFunction(expected, actual, list -> list.add(2));

    testFunction(expected, actual, list -> list.contains(7));
  }

  @Test
  void removeByIndex() {
    List<Integer> actual = new AnotherLinkedList<>();
    List<Integer> expected = new LinkedList<>();
    testFunction(expected, actual, list -> list.add(1));
    testFunction(expected, actual, list -> list.add(3));
    testFunction(expected, actual, list -> list.add(2));

    testFunction(expected, actual, list -> list.remove(1));
  }

  @Test
  void removeFirst() {
    List<Integer> actual = new AnotherLinkedList<>();
    List<Integer> expected = new LinkedList<>();
    testFunction(expected, actual, list -> list.add(1));
    testFunction(expected, actual, list -> list.add(3));
    testFunction(expected, actual, list -> list.add(2));

    testFunction(expected, actual, list -> list.remove(0));
  }

  @Test
  void removeLast() {
    List<Integer> actual = new AnotherLinkedList<>();
    List<Integer> expected = new LinkedList<>();
    testFunction(expected, actual, list -> list.add(1));
    testFunction(expected, actual, list -> list.add(3));
    testFunction(expected, actual, list -> list.add(2));

    testFunction(expected, actual, list -> list.remove(2));
  }

  @Test
  void removeByNegativeIndex() {
    List<Integer> actual = new AnotherLinkedList<>();
    List<Integer> expected = new LinkedList<>();
    testFunction(expected, actual, list -> list.add(1));
    testFunction(expected, actual, list -> list.add(3));
    testFunction(expected, actual, list -> list.add(2));

    testFunction(expected, actual, list -> list.remove(-1));
  }

  @Test
  void removeNotExisting() {
    List<Integer> actual = new AnotherLinkedList<>();
    List<Integer> expected = new LinkedList<>();
    testFunction(expected, actual, list -> list.add(1));
    testFunction(expected, actual, list -> list.add(3));
    testFunction(expected, actual, list -> list.add(2));

    testFunction(expected, actual, list -> list.remove(7));
  }

  @Test
  void offerToEmpty() {
    Queue<Integer> actual = new AnotherLinkedList<>();
    Queue<Integer> expected = new LinkedList<>();

    testFunction(expected, actual, queue -> queue.offer(1));
  }

  @Test
  void offerToNotEmpty() {
    Queue<Integer> actual = new AnotherLinkedList<>();
    Queue<Integer> expected = new LinkedList<>();
    testFunction(expected, actual, queue -> queue.offer(1));

    testFunction(expected, actual, queue -> queue.offer(3));
  }

  @Test
  void pollFromEmpty() {
    Queue<Integer> actual = new AnotherLinkedList<>();
    Queue<Integer> expected = new LinkedList<>();

    testFunction(expected, actual, Queue::poll);
  }

  @Test
  void pollFromNotEmpty() {
    Queue<Integer> actual = new AnotherLinkedList<>();
    Queue<Integer> expected = new LinkedList<>();
    testFunction(expected, actual, queue -> queue.offer(2));
    testFunction(expected, actual, queue -> queue.offer(4));
    testFunction(expected, actual, queue -> queue.offer(7));

    testFunction(expected, actual, Queue::poll);
  }
}
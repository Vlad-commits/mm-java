package org.mm.collections;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BinaryTreeTest {

  @Nested
  class add {

    @Test
    void firstElement() {
      var binaryTree = Mockito.spy(BinaryTree.<Integer, Integer>create());

      binaryTree.put(1, 1);

      assertNull(binaryTree.left);
      assertNull(binaryTree.right);
      assertEquals(1, binaryTree.key);
      assertEquals(1, binaryTree.depth);
      Mockito.verify(binaryTree, never()).balance();
    }

    @Test
    void toRightSubtree() {
      var binaryTree = Mockito.spy(BinaryTree.<Integer, Integer>create());
      binaryTree.put(1, 1);

      binaryTree.put(4, 4);

      assertNull(binaryTree.left);
      assertEquals(4, binaryTree.right.key);
      assertEquals(1, binaryTree.key);
      assertEquals(2, binaryTree.depth);
      Mockito.verify(binaryTree, never()).doubleLeftRotation();
      Mockito.verify(binaryTree, never()).doubleRightRotation();
      Mockito.verify(binaryTree, never()).simpleLeftRotation();
      Mockito.verify(binaryTree, never()).simpleRightRotation();
    }

    @Test
    void toLeftSubtree() {
      var binaryTree = Mockito.spy(BinaryTree.<Integer, Integer>create());
      binaryTree.put(6, 6);

      binaryTree.put(3, 3);

      assertEquals(3, binaryTree.left.key);
      assertNull(binaryTree.right);
      assertEquals(6, binaryTree.key);
      assertEquals(2, binaryTree.depth);
      Mockito.verify(binaryTree, never()).doubleLeftRotation();
      Mockito.verify(binaryTree, never()).doubleRightRotation();
      Mockito.verify(binaryTree, never()).simpleLeftRotation();
      Mockito.verify(binaryTree, never()).simpleRightRotation();
    }

    @Test
    void simpleLeftRotation() {
      var binaryTree = Mockito.spy(BinaryTree.<Integer, Integer>create());

      binaryTree.put(1, 1);
      binaryTree.put(3, 3);
      binaryTree.put(5, 5);

      assertEquals(1, binaryTree.left.key);
      assertEquals(3, binaryTree.key);
      assertEquals(5, binaryTree.right.key);
      assertEquals(2, binaryTree.depth);
      Mockito.verify(binaryTree, never()).doubleLeftRotation();
      Mockito.verify(binaryTree, never()).doubleRightRotation();
      Mockito.verify(binaryTree, times(1)).simpleLeftRotation();
      Mockito.verify(binaryTree, never()).simpleRightRotation();
    }

    @Test
    void simpleRightRotation() {
      var binaryTree = Mockito.spy(BinaryTree.<Integer, Integer>create());

      binaryTree.put(7, 7);
      binaryTree.put(4, 4);
      binaryTree.put(2, 2);

      assertEquals(2, binaryTree.left.key);
      assertEquals(4, binaryTree.key);
      assertEquals(7, binaryTree.right.key);
      assertSame(binaryTree, binaryTree.left.parent);
      assertSame(binaryTree, binaryTree.right.parent);
      assertEquals(2, binaryTree.depth);
      Mockito.verify(binaryTree, never()).doubleLeftRotation();
      Mockito.verify(binaryTree, never()).doubleRightRotation();
      Mockito.verify(binaryTree, never()).simpleLeftRotation();
      Mockito.verify(binaryTree, times(1)).simpleRightRotation();
    }

    @Test
    void doubleLeftRotation() {
      var binaryTree = Mockito.spy(BinaryTree.<Integer, Integer>create());

      binaryTree.put(2, 2);
      binaryTree.put(1, 1);
      binaryTree.put(7, 7);
      binaryTree.put(9, 9);
      binaryTree.put(4, 4);
      binaryTree.put(5, 5);

      assertEquals(4, binaryTree.key);
      assertEquals(2, binaryTree.left.key);
      assertEquals(1, binaryTree.left.left.key);

      assertEquals(7, binaryTree.right.key);
      assertEquals(5, binaryTree.right.left.key);
      assertEquals(9, binaryTree.right.right.key);

      assertEquals(3, binaryTree.depth);
      Mockito.verify(binaryTree, times(1)).doubleLeftRotation();
      Mockito.verify(binaryTree, never()).doubleRightRotation();
      Mockito.verify(binaryTree, never()).simpleLeftRotation();
      Mockito.verify(binaryTree, never()).simpleRightRotation();
    }

    @Test
    void doubleRightRotation() {
      var binaryTree = Mockito.spy(BinaryTree.<Integer, Integer>create());

      binaryTree.put(8, 8);
      binaryTree.put(9, 9);
      binaryTree.put(4, 4);
      binaryTree.put(3, 3);
      binaryTree.put(5, 5);
      binaryTree.put(6, 6);

      assertEquals(5, binaryTree.key);
      assertEquals(4, binaryTree.left.key);
      assertEquals(3, binaryTree.left.left.key);

      assertEquals(8, binaryTree.right.key);
      assertEquals(6, binaryTree.right.left.key);
      assertEquals(9, binaryTree.right.right.key);

      assertEquals(3, binaryTree.depth);
      Mockito.verify(binaryTree, never()).doubleLeftRotation();
      Mockito.verify(binaryTree, times(1)).doubleRightRotation();
      Mockito.verify(binaryTree, never()).simpleLeftRotation();
      Mockito.verify(binaryTree, never()).simpleRightRotation();
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForNull() {
      var binaryTree = Mockito.spy(BinaryTree.<Integer, Integer>create());
      assertThrows(IllegalArgumentException.class, () -> binaryTree.put(null, 42));
    }

    @Test
    void shouldNotAddDuplicate() {
      var binaryTree = Mockito.spy(BinaryTree.<Integer, Integer>create());
      binaryTree.put(1, 1);

      binaryTree.put(1, 1);

      assertNull(binaryTree.left);
      assertNull(binaryTree.right);
      assertEquals(1, binaryTree.key);
      assertEquals(1, binaryTree.depth);
      Mockito.verify(binaryTree, never()).balance();
    }

  }

  @Nested
  class contians {
    @Test
    void shouldReturnTrueForExisting() {
      final BinaryTree<Integer, Integer> binaryTree = BinaryTree.create();
      binaryTree.put(1, 1);

      final var contains = binaryTree.contains(1);

      assertTrue(contains);
    }

    @Test
    void shouldReturnFalseForNotExisting() {
      final BinaryTree<Integer, Integer> binaryTree = BinaryTree.create();
      binaryTree.put(1, 1);

      final var contains = binaryTree.contains(2);

      assertFalse(contains);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForNullArgument() {
      final BinaryTree<Integer, Integer> binaryTree = BinaryTree.create();
      binaryTree.put(1, 1);

      assertThrows(IllegalArgumentException.class, () -> binaryTree.contains(null));
    }
  }

  @Nested
  class find {
    @Test
    void shouldReturnExisting() {
      final BinaryTree<Integer, Integer> binaryTree = BinaryTree.create();
      binaryTree.put(1, 1);

      final var found = binaryTree.find(1);

      assertEquals(1, found);
    }

    @Test
    void shouldReturnNullForNotExisting() {
      final BinaryTree<Integer, Integer> binaryTree = BinaryTree.create();
      binaryTree.put(1, 1);

      final var found = binaryTree.find(2);

      assertNull(found);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForNullArgument() {
      final BinaryTree<Integer, Integer> binaryTree = BinaryTree.create();
      binaryTree.put(1, 1);

      assertThrows(IllegalArgumentException.class, () -> binaryTree.find(null));
    }
  }

  @Nested
  class remove {

    @Test
    void theOnlyElement() {
      var binaryTree = Mockito.spy(BinaryTree.<Integer, Integer>create());
      binaryTree.put(1, 1);

      final var result = binaryTree.remove(1);

      assertEquals(1, result);
      assertNull(binaryTree.left);
      assertNull(binaryTree.right);
      assertNull(binaryTree.parent);
      assertNull(binaryTree.key);
      assertEquals(0, binaryTree.depth);
      Mockito.verify(binaryTree, never()).doubleLeftRotation();
      Mockito.verify(binaryTree, never()).doubleRightRotation();
      Mockito.verify(binaryTree, never()).simpleLeftRotation();
      Mockito.verify(binaryTree, never()).simpleRightRotation();
    }

    @Test
    void fromRightSubtree() {
      var binaryTree = Mockito.spy(BinaryTree.<Integer, Integer>create());
      binaryTree.put(1, 1);
      binaryTree.put(4, 4);

      final var result = binaryTree.remove(4);

      assertEquals(4, result);
      assertNull(binaryTree.left);
      assertNull(binaryTree.right);
      assertEquals(1, binaryTree.key);
      assertEquals(1, binaryTree.depth);
      Mockito.verify(binaryTree, never()).doubleLeftRotation();
      Mockito.verify(binaryTree, never()).doubleRightRotation();
      Mockito.verify(binaryTree, never()).simpleLeftRotation();
      Mockito.verify(binaryTree, never()).simpleRightRotation();
    }

    @Test
    void fromLeftSubTree() {
      var binaryTree = Mockito.spy(BinaryTree.<Integer, Integer>create());
      binaryTree.put(6, 6);
      binaryTree.put(3, 3);

      final var result = binaryTree.remove(3);

      assertEquals(3, result);
      assertNull(binaryTree.right);
      assertNull(binaryTree.left);
      assertEquals(6, binaryTree.key);
      assertEquals(1, binaryTree.depth);
      Mockito.verify(binaryTree, never()).doubleLeftRotation();
      Mockito.verify(binaryTree, never()).doubleRightRotation();
      Mockito.verify(binaryTree, never()).simpleLeftRotation();
      Mockito.verify(binaryTree, never()).simpleRightRotation();
    }

    @Test
    void leafSimpleLeftRotation() {
      var binaryTree = Mockito.spy(BinaryTree.<Integer, Integer>create());
      binaryTree.put(2, 2);
      binaryTree.put(1, 1);
      binaryTree.put(3, 3);
      binaryTree.put(5, 5);

      final var result = binaryTree.remove(1);

      assertEquals(1, result);
      assertEquals(2, binaryTree.left.key);
      assertEquals(3, binaryTree.key);
      assertEquals(5, binaryTree.right.key);
      assertEquals(2, binaryTree.depth);
      Mockito.verify(binaryTree, never()).doubleLeftRotation();
      Mockito.verify(binaryTree, never()).doubleRightRotation();
      Mockito.verify(binaryTree, times(1)).simpleLeftRotation();
      Mockito.verify(binaryTree, never()).simpleRightRotation();
    }

    @Test
    void nonLeafSimpleLeftRotation() {
      var binaryTree = Mockito.spy(BinaryTree.<Integer, Integer>create());
      binaryTree.put(3, 3);
      binaryTree.put(2, 2);
      binaryTree.put(9, 9);
      binaryTree.put(1, 1);
      binaryTree.put(8, 8);
      binaryTree.put(10, 10);
      binaryTree.put(11, 11);

      final var result = binaryTree.remove(2);

      assertEquals(2, result);
      assertEquals(1, binaryTree.left.left.key);
      assertEquals(3, binaryTree.left.key);
      assertEquals(8, binaryTree.left.right.key);
      assertEquals(9, binaryTree.key);
      assertEquals(10, binaryTree.right.key);
      assertEquals(11, binaryTree.right.right.key);
      assertEquals(3, binaryTree.depth);
      Mockito.verify(binaryTree, never()).doubleLeftRotation();
      Mockito.verify(binaryTree, never()).doubleRightRotation();
      Mockito.verify(binaryTree, times(1)).simpleLeftRotation();
      Mockito.verify(binaryTree, never()).simpleRightRotation();
    }

    @Test
    void leafSimpleRightRotation() {
      var binaryTree = Mockito.spy(BinaryTree.<Integer, Integer>create());
      binaryTree.put(7, 7);
      binaryTree.put(9, 9);
      binaryTree.put(4, 4);
      binaryTree.put(2, 2);

      final var result = binaryTree.remove(9);

      assertEquals(9, result);
      assertEquals(2, binaryTree.left.key);
      assertEquals(4, binaryTree.key);
      assertEquals(7, binaryTree.right.key);
      assertEquals(2, binaryTree.depth);
      Mockito.verify(binaryTree, never()).doubleLeftRotation();
      Mockito.verify(binaryTree, never()).doubleRightRotation();
      Mockito.verify(binaryTree, never()).simpleLeftRotation();
      Mockito.verify(binaryTree, times(1)).simpleRightRotation();
    }

    @Test
    void nonLeafSimpleRightRotation() {
      var binaryTree = Mockito.spy(BinaryTree.<Integer, Integer>create());
      binaryTree.put(7, 7);
      binaryTree.put(9, 9);
      binaryTree.put(4, 4);
      binaryTree.put(10, 10);
      binaryTree.put(2, 2);
      binaryTree.put(1, 1);
      binaryTree.put(0, 0);

      final var result = binaryTree.remove(9);

      assertEquals(9, result);
      assertEquals(0, binaryTree.left.left.key);
      assertEquals(1, binaryTree.left.key);
      assertEquals(2, binaryTree.key);
      assertEquals(7, binaryTree.right.key);
      assertEquals(4, binaryTree.right.left.key);
      assertEquals(10, binaryTree.right.right.key);
      assertEquals(3, binaryTree.depth);
      Mockito.verify(binaryTree, never()).doubleLeftRotation();
      Mockito.verify(binaryTree, never()).doubleRightRotation();
      Mockito.verify(binaryTree, never()).simpleLeftRotation();
      Mockito.verify(binaryTree, times(1)).simpleRightRotation();
    }

    @Test
    void leafDoubleLeftRotation() {
      var binaryTree = Mockito.spy(BinaryTree.<Integer, Integer>create());
      binaryTree.put(2, 2);
      binaryTree.put(1, 1);
      binaryTree.put(7, 7);
      binaryTree.put(9, 9);
      binaryTree.put(4, 4);
      binaryTree.put(-1, -1);
      binaryTree.put(5, 5);

      final var result = binaryTree.remove(-1);

      assertEquals(-1, result);
      assertEquals(4, binaryTree.key);
      assertEquals(2, binaryTree.left.key);
      assertEquals(1, binaryTree.left.left.key);

      assertEquals(7, binaryTree.right.key);
      assertEquals(5, binaryTree.right.left.key);
      assertEquals(9, binaryTree.right.right.key);

      assertEquals(3, binaryTree.depth);
      Mockito.verify(binaryTree, times(1)).doubleLeftRotation();
      Mockito.verify(binaryTree, never()).doubleRightRotation();
      Mockito.verify(binaryTree, never()).simpleLeftRotation();
      Mockito.verify(binaryTree, never()).simpleRightRotation();
    }

    @Test
    void nonLeafDoubleLeftRotation() {
      var binaryTree = Mockito.spy(BinaryTree.<Integer, Integer>create());
      binaryTree.put(2, 2);
      binaryTree.put(1, 1);
      binaryTree.put(7, 7);
      binaryTree.put(9, 9);
      binaryTree.put(4, 4);
      binaryTree.put(-1, -1);
      binaryTree.put(5, 5);

      final var result = binaryTree.remove(1);

      assertEquals(1, result);
      assertEquals(4, binaryTree.key);
      assertEquals(2, binaryTree.left.key);
      assertEquals(-1, binaryTree.left.left.key);

      assertEquals(7, binaryTree.right.key);
      assertEquals(5, binaryTree.right.left.key);
      assertEquals(9, binaryTree.right.right.key);

      assertEquals(3, binaryTree.depth);
      Mockito.verify(binaryTree, times(1)).doubleLeftRotation();
      Mockito.verify(binaryTree, never()).doubleRightRotation();
      Mockito.verify(binaryTree, never()).simpleLeftRotation();
      Mockito.verify(binaryTree, never()).simpleRightRotation();
    }


    @Test
    void leafDoubleRightRotation() {
      var binaryTree = Mockito.spy(BinaryTree.<Integer, Integer>create());
      binaryTree.put(8, 8);
      binaryTree.put(9, 9);
      binaryTree.put(4, 4);
      binaryTree.put(3, 3);
      binaryTree.put(5, 5);
      binaryTree.put(10, 10);
      binaryTree.put(6, 6);

      final var result = binaryTree.remove(10);

      assertEquals(10, result);
      assertEquals(5, binaryTree.key);
      assertEquals(4, binaryTree.left.key);
      assertEquals(3, binaryTree.left.left.key);

      assertEquals(8, binaryTree.right.key);
      assertEquals(6, binaryTree.right.left.key);
      assertEquals(9, binaryTree.right.right.key);

      assertEquals(3, binaryTree.depth);
      Mockito.verify(binaryTree, never()).doubleLeftRotation();
      Mockito.verify(binaryTree, times(1)).doubleRightRotation();
      Mockito.verify(binaryTree, never()).simpleLeftRotation();
      Mockito.verify(binaryTree, never()).simpleRightRotation();
    }

    @Test
    void nonLeafDoubleRightRotation() {
      var binaryTree = Mockito.spy(BinaryTree.<Integer, Integer>create());
      binaryTree.put(8, 8);
      binaryTree.put(9, 9);
      binaryTree.put(4, 4);
      binaryTree.put(3, 3);
      binaryTree.put(5, 5);
      binaryTree.put(10, 10);
      binaryTree.put(6, 6);

      final var result = binaryTree.remove(9);

      assertEquals(9, result);
      assertEquals(5, binaryTree.key);
      assertEquals(4, binaryTree.left.key);
      assertEquals(3, binaryTree.left.left.key);

      assertEquals(8, binaryTree.right.key);
      assertEquals(6, binaryTree.right.left.key);
      assertEquals(10, binaryTree.right.right.key);

      assertEquals(3, binaryTree.depth);
      Mockito.verify(binaryTree, never()).doubleLeftRotation();
      Mockito.verify(binaryTree, times(1)).doubleRightRotation();
      Mockito.verify(binaryTree, never()).simpleLeftRotation();
      Mockito.verify(binaryTree, never()).simpleRightRotation();
    }

    @Test
    void shouldReturnFalseForNotExisting() {
      var binaryTree = Mockito.spy(BinaryTree.<Integer, Integer>create());
      binaryTree.put(8, 8);
      binaryTree.put(9, 9);
      binaryTree.put(4, 4);
      binaryTree.put(3, 3);
      binaryTree.put(5, 5);
      binaryTree.put(10, 10);
      binaryTree.put(6, 6);

      final var result = binaryTree.remove(42);

      assertNull(result);
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForNull() {
      var binaryTree = Mockito.spy(BinaryTree.<Integer, Integer>create());
      binaryTree.put(8, 8);
      binaryTree.put(9, 9);
      binaryTree.put(4, 4);
      binaryTree.put(3, 3);
      binaryTree.put(5, 5);
      binaryTree.put(10, 10);
      binaryTree.put(6, 6);
      assertThrows(IllegalArgumentException.class, () -> binaryTree.remove(null));
    }
  }
}
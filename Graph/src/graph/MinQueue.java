package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * A min priority queue of distinct elements of type `KeyType` associated with (extrinsic) integer
 * priorities, implemented using a binary heap paired with a hash table.
 */

public class MinQueue<KeyType> implements PriorityQueue<KeyType> {

    /**
     * Pairs an element `key` with its associated priority `priority`.
     */
    private record Entry<KeyType>(KeyType key, int priority) {
        // Note: This is equivalent to declaring a static nested class with fields `key` and
        //  `priority` and a corresponding constructor and observers, overriding `equals()` and
        //  `hashCode()` to depend on the fields, and overriding `toString()` to print their values.
        // https://docs.oracle.com/en/java/javase/17/language/records.html
    }

    /**
     * Associates each element in the queue with its index in `heap`.  Satisfies
     * `heap.get(index.get(e)).key().equals(e)` if `e` is an element in the queue. Only maps
     * elements that are in the queue (`index.size() == heap.size()`).
     */

    public final Map<KeyType, Integer> index;

    /**
     * Sequence representing a min-heap of element-priority pairs.  Satisfies
     * `heap.get(i).priority() >= heap.get((i-1)/2).priority()` for all `i` in `[1..heap.size()]`.
     */
    private final ArrayList<Entry<KeyType>> heap;

    /**
     * Assert that our class invariant is satisfied.  Returns true if it is (or if assertions are
     * disabled).
     */
    private boolean checkInvariant() {
        for (int i = 1; i < heap.size(); ++i) {
            int p = (i - 1) / 2;
            assert heap.get(i).priority() >= heap.get(p).priority();
            assert index.get(heap.get(i).key()) == i;
        }
        assert index.size() == heap.size();
        return true;
    }

    /**
     * Create an empty queue.
     */
    public MinQueue() {
        index = new HashMap<>();
        heap = new ArrayList<>();
        assert checkInvariant();
    }

    /**
     * Return whether this queue contains no elements.
     */
    @Override
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    /**
     * Return the number of elements contained in this queue.
     */
    @Override
    public int size() {
        return heap.size();
    }

    /**
     * Return an element associated with the smallest priority in this queue.  This is the same
     * element that would be removed by a call to `remove()` (assuming no mutations in between).
     * Throws NoSuchElementException if this queue is empty.
     */
    @Override
    public KeyType get() {
        // Propagate exception from `List::getFirst()` if empty.
        return heap.getFirst().key();
    }

    /**
     * Return the minimum priority associated with an element in this queue.  Throws
     * NoSuchElementException if this queue is empty.
     */
    @Override
    public int minPriority() {
        return heap.getFirst().priority();
    }

    /**
     * If `key` is already contained in this queue, change its associated priority to `priority`.
     * Otherwise, add it to this queue with that priority.
     */
    @Override
    public void addOrUpdate(KeyType key, int priority) {
        if (!index.containsKey(key)) {
            add(key, priority);
        } else {
            update(key, priority);
        }
    }

    public boolean contains(KeyType key) {
        return index.containsKey(key);
    }


    /**
     * Remove and return the element associated with the smallest priority in this queue.  If
     * multiple elements are tied for the smallest priority, an arbitrary one will be removed.
     * Throws NoSuchElementException if this queue is empty.
     */
    @Override
    public KeyType remove() {
        if (heap.isEmpty()) {
            throw new NoSuchElementException();
        }
        // save the current smallest priority element
        KeyType root = heap.getFirst().key();

        // swap rightmost-bottom element (heap.getLast()) with root, (heap.getFirst()), then
        // delete the previous root (now heap.getLast()) from the heap
        swap(0, heap.size()-1);
        heap.removeLast();
        index.remove(root);
        if (!heap.isEmpty()) {
            bubbleDown(heap.getFirst().key(), heap.getFirst().priority());
        }
        return root;
    }

    /**
     * Remove all elements from this queue (making it empty).
     */
    @Override
    public void clear() {
        index.clear();
        heap.clear();
        assert checkInvariant();
    }

    /**
     * Swap the Entries at indices `i` and `j` in `heap`, updating `index` accordingly.  Requires `0
     * <= i,j < heap.size()`.
     */
    private void swap(int i, int j) {
        // key is vertex, value is priority, heap is array whose vertices' indices are ordered based on
        // their position on the binary heap tree: heap.get(i) is current node, and heap.get((i-1)/2) is
        // parent, so heap.get(i) priority >= heap.get((i-1)/2)
        assert i >= 0 && i < heap.size();
        assert j >= 0 && j < heap.size();
        // given indices in heap, i and j. contents of heap.get(j) now are accessed in heap.get(i),
        // associate key of heap.get(i) with index j, and heap.get(j) with index i
        Entry<KeyType> temp = heap.get(i);
        index.replace(heap.get(i).key(), i, j);
        index.replace(heap.get(j).key(), j, i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    /**
     * Add element `key` to this queue, associated with priority `priority`.  Requires `key` is not
     * contained in this queue.
     */
    private void add(KeyType key, int priority) {
        assert !index.containsKey(key);
        // add 'key' to leftmost-empty bottom layer, then bubble-up until 'key' is in correct pos
        index.put(key, heap.size());
        heap.add(new Entry<KeyType>(key, priority));
        bubbleUp(key, priority);
        assert checkInvariant();
    }

    /**
     * Change the priority associated with element `key` to `priority`.  Requires that `key` is
     * contained in this queue.
     */
    private void update(KeyType key, int priority) {
        assert index.containsKey(key);
        // update priority in index and heap
        int priorityOld = heap.get(index.get(key)).priority();
        heap.set(index.get(key), new Entry<KeyType>(key, priority));

        // if increase priority: bubble-down
        if (priorityOld < priority) {
            bubbleDown(key, priority);
        }
        // if decrease priority: bubble-up
        else if (priorityOld > priority) {
            bubbleUp(key, priority);
        }
        assert checkInvariant();
    }

    /**
     * Continues swapping entry of the corresponding 'key' with parent entry until entry has
     * 'priority' greater or equal to the parent entry's priority.
     */
    private void bubbleUp(KeyType key, int priority) {
        // given min-heap: if parent (k-1)/2 has priority > child k, swap (k-1)/2 and k.
        // node to bubble up has index k = index.get(key)
        int priorityPar = heap.get((index.get(key)-1)/2).priority();
        if (priority >= priorityPar) {
            assert checkInvariant();
            return;
        }
        // swap if priority < priorityPar, heap ordering invariant not satisfied
        swap((index.get(key)-1)/2, index.get(key));
        // swap changes index of current child if swapped
        bubbleUp(key, priority);
    }

    /**
     * Continues swapping corresponding 'key' entry with either left or right child entry, whichever
     * entry has the smaller priority, until both left and right child entries have greater or
     * equal priorities as the key entry's 'priority'.
     */
    private void bubbleDown(KeyType key, int priority)  {
        // given min-heap: if left (2k+1) or right (2k+2) child has smaller priority than parent k,
        // swap with the child with smaller priority
        int priorityLeft = (2*index.get(key)+1 >= heap.size()) ? priority : heap.get(2*index.get(key)+1).priority;
        int priorityRight = (2*index.get(key)+2 >= heap.size()) ? priority : heap.get(2*index.get(key)+2).priority;
        // done swapping if both children have greater or equal priority as parent
        if (Math.min(priorityLeft, priorityRight) >= priority) {
            assert checkInvariant();
            return;
        }
        // if left child is the smaller priority, swap with left child
        if (priorityLeft < priorityRight) {
            swap(2 * index.get(key) + 1, index.get(key));
            bubbleDown(key, priority);
        }
        // else right child is the smaller or equal priority, swap with right child
        else {
            swap(2 * index.get(key) + 2, index.get(key));
            bubbleDown(key, priority);
        }
    }
}

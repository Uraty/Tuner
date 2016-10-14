package dnu.ks.barannik.musicpitch;

import java.util.Iterator;

public class FixedBuffer<T> implements Iterable<T>{

    private class Item {
        public T data;
        public Item next;

        public Item() {}
        public Item(T data) {
            this.data = data;
            this.next = this;
        }
        public Item(T data, Item next) {
            this.data = data;
            this.next = next;
        }
    }
    private Item tail;

    private final int MAX_SIZE;
    private int size;
    public FixedBuffer(int maxSize) {
        if (maxSize < 1) throw new IllegalArgumentException("Buffer size can't be less then 1");
        MAX_SIZE = maxSize;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return tail == null;
    }

    public boolean contains(Object o) {
        for (T item : this)
            if (item.equals(o))
                return true;
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Item current = tail;
            private int remain = size;

            @Override
            public boolean hasNext() {
                return remain != 0;
            }

            @Override
            public T next() {
                remain--;
                current = current.next;
                return current.data;
            }

            @Override
            public void remove() {

            }
        };
    }

    public void add(T data) {
        if (size == MAX_SIZE) {
            tail = tail.next;
            tail.data = data;
            return;
        } else if (size == 0) {
            tail = new Item(data);
        } else {
            tail = tail.next = new Item(data, tail.next);
        }
        size++;
    }

    public void clear() {
        tail = null;
        size = 0;
    }

    public void fillWith(T object) {
        for (int i = size; i <= MAX_SIZE; i++)
            add(object);
    }
}


package dnu.ks.barannik.musicpitch.Tests;

import org.junit.Test;

import dnu.ks.barannik.musicpitch.FixedBuffer;

import static org.junit.Assert.*;

public class FixedBufferTest {

    @Test
    public void add() {
        FixedBuffer<Integer> buffer = new FixedBuffer<>(3);
        buffer.add(1);
        buffer.add(2);
        buffer.add(3);
        Integer i = 1;
        for (Integer content : buffer) {
            assertEquals(i++, content);
        }

        buffer.add(4);
        i = 2;
        for (Integer content : buffer) {
            assertEquals(i++, content);
        }
    }

    @Test
    public void contains() {
        FixedBuffer<Integer> buffer = new FixedBuffer<>(3);
        buffer.add(1);
        buffer.add(2);
        buffer.add(3);
        Integer i = 1;
        for (Integer content : buffer) {
            assertTrue(buffer.contains(i++));
            assertFalse(buffer.contains(-i));
        }
        buffer.add(4);
        assertFalse(buffer.contains(1));
    }

    @Test
    public void size() {
        FixedBuffer<Integer> buffer = new FixedBuffer<>(3);
        assertEquals(0, buffer.size());
        buffer.add(1);
        assertEquals(1, buffer.size());
        buffer.add(2);
        assertEquals(2, buffer.size());
        buffer.add(3);
        assertEquals(3, buffer.size());
        buffer.add(4);
        assertEquals(3, buffer.size());
        buffer.add(5);
        assertEquals(3, buffer.size());
    }

}

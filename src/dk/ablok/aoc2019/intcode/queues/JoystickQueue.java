package dk.ablok.aoc2019.intcode.queues;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

public class JoystickQueue implements Queue<Long> {
    Long val = 0L;

    @Override
    public boolean add(Long o) {
        val = o;
        return true;
    }

    @Override
    public boolean offer(Long o) {
        val = o;
        return true;
    }

    @Override
    public Long remove() { return val; }

    @Override
    public Long poll() { return val; }

    @Override
    public Long element() { return val; }

    @Override
    public Long peek() { return val; }



    @Override
    public boolean addAll(Collection c) { return false; }

    @Override
    public void clear() { }

    @Override
    public boolean retainAll(Collection c) { return false; }

    @Override
    public boolean removeAll(Collection c) { return false; }

    @Override
    public boolean containsAll(Collection c) { return false; }

    @Override
    public int size() { return 1; }

    @Override
    public boolean isEmpty() { return false; }

    @Override
    public Long[] toArray(Object[] a) { return new Long[0]; }

    @Override
    public Iterator<Long> iterator() { return null; }

    @Override
    public boolean remove(Object o) { return false; }

    @Override
    public boolean contains(Object o) {
        long obj = (long)o;
        return obj==val;
    }

    @Override
    public Long[] toArray() {
        Long[] ret = new Long[1];
        ret[0] = val;
        return ret;
    }

}
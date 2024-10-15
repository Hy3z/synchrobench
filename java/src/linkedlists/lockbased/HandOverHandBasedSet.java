package linkedlists.lockbased;

import contention.abstractions.AbstractCompositionalIntSet;
import linkedlists.lockbased.lazyutils.Node;

public class HandOverHandBasedSet extends AbstractCompositionalIntSet {
    private Node head;
    public HandOverHandBasedSet() {
        head = new Node(Integer.MIN_VALUE);
        head.next = new Node(Integer.MAX_VALUE);
    }

    @Override
    public boolean addInt(int x) {
        head.lock();
        Node pred = head;
        Node curr = pred.next;
        try {
            curr.lock();
            try {
                while (curr.value < x) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }
                if (curr.value == x) return false;
                Node newNode = new Node(x);
                pred.next = newNode;
                newNode.next = curr;
                return true;
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }


    @Override
    public boolean removeInt(int x) {
        head.lock();
        Node pred = head;
        Node curr = pred.next;
        try {
            curr.lock();
            try {
                while (curr.value < x) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }
                if (curr.value != x) return false;
                pred.next = curr.next;
                return true;
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    @Override
    public boolean containsInt(int x) {
        head.lock();
        Node pred = head;
        Node curr = pred.next;
        try {
            curr.lock();
            try {
                while (curr.value < x) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }
                return curr.value == x;
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    @Override
    public int size() {
        int size = 0;
        head.lock();
        Node pred = head;
        Node curr = pred.next;
        try {
            curr.lock();
            try {
                while (curr.value != Integer.MAX_VALUE) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                    size++;
                }
                return size;
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    @Override
    public void clear() {
        head=new Node(Integer.MIN_VALUE);
        head.next=new Node(Integer.MAX_VALUE);
    }
}

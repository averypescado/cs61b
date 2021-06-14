package deque;

public class LinkedListDeque<T> {
    private class StuffNode {
        public T item;
        public StuffNode next;
        public StuffNode prev;

        public StuffNode(T x) {
            item= x;
        }
    }

    private StuffNode sentinel;
    private int size;

    public LinkedListDeque() {
        sentinel = new StuffNode(null);
        sentinel.next=sentinel;
        sentinel.prev=sentinel;
        size=0;
    }

    public void addFirst(T x) {
        StuffNode new_item = new StuffNode(x);
        new_item.prev= sentinel;
        new_item.next=sentinel.next;
        sentinel.next.prev=new_item;
        sentinel.next=new_item;
        size+=1;
    }

    public void addLast(T x) {
        StuffNode new_item = new StuffNode(x);
        new_item.prev= sentinel.prev;
        new_item.next=sentinel;
        sentinel.prev.next=new_item;
        sentinel.prev=new_item;
        size+=1;
    }
    public boolean isEmpty(){
        if (this.size==0) {
            return true;
        }
        else{
            return false;
        }
    }
    public int size(){
        return size;
    }


    public void printDeque(){
        StuffNode value=sentinel.next;
        for (int i=0; i < this.size(); i+=1){
            System.out.print(value.item + " ");
            value=value.next;
        }
    }
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        else{
            StuffNode first = sentinel.next;
            sentinel.next = first.next;
            first.next.prev = sentinel;
            size -= 1;
            return first.item;

        }
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;

        }
        else{
            StuffNode last = sentinel.prev;
            sentinel.prev = last.prev;
            last.prev.next = sentinel;
            size -= 1;
            return last.item;
        }
    }


    public StuffNode get(int x) {
        StuffNode value = sentinel.next;

        if (x >= this.size()) {
            System.out.println("Too big");
            return null;
        } else {

            int i = 0;
            while (i < x) {
                value = value.next;
                i += 1;
            }
            System.out.println(value.item);
            return value;
        }
    }

    public static void main(String[] args) {
    }
}
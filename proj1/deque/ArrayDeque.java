package deque;

public class ArrayDeque<Item> {
    private Item[] items;
    private int size;
    private int First;
    private int Last;



    public ArrayDeque() {
        items= (Item[])new Object[8];
        size=0;
        First=4;
        Last=4;
    }

    public void addFirst(Item x) {
        size+=1;
        if (First==0) {
            First=size-1;
        }
        else{
            First=First-1;
        }
        items[First]=x;
    }

    public void addLast(Item x) {
        size+=1;
        if (Last==size-1) {
            Last=0;
        }
        else {
            Last=Last+1;
        }
        items[Last]=x;
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

    public Item removeFirst() {
        int oldFirst=First;
        if (First==size-1) {
            First=0;
        }
        else{
            First=First+1;
        }
        return items[oldFirst];
    }


    public Item removeLast() {
        int oldLast=Last;
        if (Last==First) {
            Last=size-1;
        }
        else{
            Last=Last-1;
        }
        return items[oldLast];
    }

    public void printDeque() {
        int F= First;
        for (int i = F; i < size+First; i++) {
            System.out.print(items[i] + " ");
        }
    }



    public static void main(String[] args) {
        ArrayDeque A = new ArrayDeque();
        A.addFirst(9);
        A.addFirst(12);
        A.printDeque();

    }
}

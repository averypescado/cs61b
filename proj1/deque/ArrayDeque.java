package deque;

public class ArrayDeque<Item> {
    private Item[] items;
    private int size;
    private int First;
    private int Last;



    public ArrayDeque() {
        items= (Item[])new Object[8];
        size=0;
        First=3;
        Last=4;
    }

    public void addFirst(Item x) {
        size+=1;
        items[First]=x;
        First = ((First-1)+items.length) % items.length;
        }

    public void addLast(Item x) {
        size+=1;
        items[Last]=x;
        Last=(Last+1+items.length) % items.length;
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

        public void resize(int big_size) {
            Item[] big_items= (Item[])new Object[big_size];
            int middle=big_size /2;
            int start= middle-(size/2);
            System.arraycopy(items,First+1,big_items,middle,items.length-4);
            items=big_items;
        }


        public void printDeque() {
            int F = First+1;
            int j=F;
            if (F==items.length){
                j=0;

            }
            else {
                j = F;
            }
            for (int i=0; i < this.size;i+=1) {
                if (j== items.length){
                    j=0;
                }
                System.out.print(items[j] + " ");
                j+=1;
            }
        }



    public static void main(String[] args) {
        ArrayDeque A = new ArrayDeque();
        A.addLast(8);
        A.addLast(7);
        A.addLast(6);
        A.addLast(5);
        A.addLast(4);
        A.addLast(3);
        A.addLast(2);
        A.resize(16);
    }
}

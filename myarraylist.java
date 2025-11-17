public class myarraylist<T> implements Mylist<T> {

    private static final int DEFAULT_CAPACITY = 10;

    private Object[] data;
    private int size;

    public myarraylist() {
        data = new Object[DEFAULT_CAPACITY];
        size = 0;
    }

    @Override
    public void add(T value) {
        if (size == data.length) {
            grow();
        }
        data[size] = value;
        size++;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
        }
        return (T) data[index];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void grow() {
        int newCapacity = data.length * 2;
        Object[] newData = new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
        return sb.toString();
    }
}

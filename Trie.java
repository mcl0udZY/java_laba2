public class Trie {

    // простой список generic минимум методов add, get, size isEmpty.

    public interface MyList<T> {
        void add(T value);
        T get(int index);
        int size();
        boolean isEmpty();
    }

    // реализация динамического массива: массив + увеличение когда не хватает места
     
    public static class MyArrayList<T> implements MyList<T> {

        private static final int DEFAULT_CAPACITY = 10;

        private Object[] data;
        private int size;

        public MyArrayList() {
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

    //узел Trie вместо map
    
    private static class Node {
        private static final int ALPHABET_SIZE = 26;

        private Node[] children;
        private boolean isWordEnd;

        public Node() {
            children = new Node[ALPHABET_SIZE];
            isWordEnd = false;
        }

        public Node getChild(char c) {
            int index = toIndex(c);
            return children[index];
        }

        public Node getOrCreateChild(char c) {
            int index = toIndex(c);
            if (children[index] == null) {
                children[index] = new Node();
            }
            return children[index];
        }

        private static int toIndex(char c) {
            // ожидаемо только 'a'..'z'
            if (c < 'a' || c > 'z') {
                throw new IllegalArgumentException("допустимы только буквы a-z, символ: " + c);
            }
            return c - 'a';
        }
    }

    // корень дерева
    private final Node root;

    public Trie() {
        root = new Node();
    }

    // публичные методы

    // вставка слова
    public void insert(String word) {
        checkWordForInsert(word);

        Node current = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            current = current.getOrCreateChild(c);
        }
        current.isWordEnd = true;
    }

    // проверка наличия слова
    public boolean contains(String word) {
        if (word == null || word.isEmpty()) {
            return false;
        }

        Node node = findNode(word);
        return node != null && node.isWordEnd;
    }

    // есть ли слова с таким префиксом
    public boolean startsWith(String prefix) {
        if (prefix == null || prefix.isEmpty()) {
            return false;
        }

        Node node = findNode(prefix);
        return node != null;
    }

    // получить все слова по префиксу
    public MyList<String> getByPrefix(String prefix) {
        MyArrayList<String> result = new MyArrayList<>();

        if (prefix == null || prefix.isEmpty()) {
            return result;
        }

        Node node = findNode(prefix);
        if (node == null) {
            return result;
        }

        StringBuilder current = new StringBuilder(prefix);
        collect(node, current, result);

        return result;
    }

    // приватные вспомогательные

    private void checkWordForInsert(String word) {
        if (word == null) {
            throw new IllegalArgumentException("Слово не может быть null");
        }
        if (word.isEmpty()) {
            throw new IllegalArgumentException("Слово не может быть пустым");
        }
        //  можно дополнительно проверить что все буквы в диапазоне a-z
    }

    // находим узел соответствующий последнему символу строки s
    private Node findNode(String s) {
        Node current = root;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            current = current.getChild(c);
            if (current == null) {
                return null;
            }
        }

        return current;
    }

    // обходим поддерево и добавляем все слова
    private void collect(Node node, StringBuilder prefix, MyArrayList<String> result) {
        if (node.isWordEnd) {
            result.add(prefix.toString());
        }

        for (int i = 0; i < Node.ALPHABET_SIZE; i++) {
            Node child = node.children[i];
            if (child != null) {
                char nextChar = (char) ('a' + i);
                prefix.append(nextChar);

                collect(child, prefix, result);

                // откатываем последний символ
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
    }

    // простой тест
    public static void main(String[] args) {
        Trie trie = new Trie();

        trie.insert("apple");
        trie.insert("app");
        trie.insert("application");
        trie.insert("banana");

        System.out.println(trie.contains("app"));// true
        System.out.println(trie.contains("ap")); // false
        System.out.println(trie.startsWith("ap"));// true

        System.out.println(trie.getByPrefix("app"));// [app, apple, application]
        System.out.println(trie.getByPrefix("banana"));// [banana]
        System.out.println(trie.getByPrefix("ba"));// [banana]
        System.out.println(trie.getByPrefix("x")); // []
    }
}

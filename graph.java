public class graph<T> {

    // вершина значение + список соседей (по индексам)
    private static class Vertex<V> {
        V value;
        myarraylist<Integer> neighbors;

        Vertex(V value) {
            this.value = value;
            this.neighbors = new myarraylist<>();
        }
    }

    private final boolean directed;
    private final myarraylist<Vertex<T>> vertices;

    public graph(boolean directed) {
        this.directed = directed;
        this.vertices = new myarraylist<>();
    }

    // добавить вершину возвращаем её индекс
    public int addVertex(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Значение вершины не может быть null");
        }
        Vertex<T> vertex = new Vertex<>(value);
        vertices.add(vertex);
        return vertices.size() - 1;
    }

    // добавить ребро по индексам
    public void addEdge(int fromIndex, int toIndex) {
        checkIndex(fromIndex);
        checkIndex(toIndex);

        Vertex<T> from = vertices.get(fromIndex);
        Vertex<T> to = vertices.get(toIndex);

        from.neighbors.add(toIndex);

        if (!directed) {
            to.neighbors.add(fromIndex);
        }
    }

    // значение вершины
    public T getVertexValue(int index) {
        checkIndex(index);
        return vertices.get(index).value;
    }

    // соседи индексы
    public Mylist<Integer> getNeighbors(int index) {
        checkIndex(index);
        return vertices.get(index).neighbors;
    }

    // соседи значения
    public Mylist<T> getNeighborValues(int index) {
        checkIndex(index);
        Vertex<T> v = vertices.get(index);
        myarraylist<T> result = new myarraylist<>();
        for (int i = 0; i < v.neighbors.size(); i++) {
            int ni = v.neighbors.get(i);
            result.add(vertices.get(ni).value);
        }
        return result;
    }

    public int vertexCount() {
        return vertices.size();
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= vertices.size()) {
            throw new IndexOutOfBoundsException("нет вершины с индексом " + index);
        }
    }

    // тест
    public static void main(String[] args) {
        graph<String> g = new graph<>(false); // false неориентированный

        int a = g.addVertex("A");
        int b = g.addVertex("B");
        int c = g.addVertex("C");

        g.addEdge(a, b);
        g.addEdge(a, c);

        System.out.println("Вершин: " + g.vertexCount());// 3
        System.out.println("Соседи A (индексы): " + g.getNeighbors(a)); // [1, 2]
        System.out.println("Соседи A (значения): " + g.getNeighborValues(a)); // [B, C]
    }
}

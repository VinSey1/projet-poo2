import java.util.List;
import java.util.Objects;

public class Quadtree<V> {

    private final List<Quadtree<V>> childs;
    private V value;

    public Quadtree(final V value, final List<Quadtree<V>> childs) {
        if (childs != null && childs.size() > 4)
            throw new UnsupportedOperationException("Quadtree should have no more than four childs!");
        this.value = Objects.requireNonNull(value);
        this.childs = childs;
    }

    public Quadtree(V value) {
        this(value, null);
    }

    public V getValue() {
        return value;
    }

    public List<Quadtree<V>> getChilds() {
        return childs;
    }

    public void addChild(Quadtree<V> child) {
        this.childs.add(child);
    }
}

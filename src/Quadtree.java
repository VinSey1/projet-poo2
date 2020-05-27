import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @param <V> Type du quadtree
 */
public class Quadtree<V> {

    /**
     * Les enfants de l'arbre
     */
    private final List<Quadtree<V>> childs;
    /**
     * La valeur sur le noeud
     */
    private V value;

    public Quadtree(final V value, final List<Quadtree<V>> childs) {
        if (childs != null && childs.size() > 4)
            throw new UnsupportedOperationException("Quadtree should have no more than four childs!");
        this.value = Objects.requireNonNull(value);
        this.childs = childs == null ? new ArrayList<>(4) : childs;
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
        if (this.childs.size() < 4)
            this.childs.add(child);
    }
}

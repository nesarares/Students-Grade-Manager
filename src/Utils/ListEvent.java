package Utils;

import java.util.List;

public abstract class ListEvent<E> {
    private ListEventType type;

    public ListEvent(ListEventType type) {
        this.type = type;
    }

    public ListEventType getType() {
        return type;
    }

    public void setType(ListEventType type) {
        this.type = type;
    }

    /**
     * @return lista implicata in eveniment
     */
    public abstract List<E> getList();

    /**
     * @return elementul implicat in eveniment
     */
    public abstract E getElement();
}

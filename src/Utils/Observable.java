package Utils;

public interface Observable<E> {
    /**
     * Adauga un observer la lista de observeri
     * @param observer - observerul de adaugat
     */
    void addObserver(Observer<E> observer);


    /**
     * Elimina un observer din lista
     * @param observer - observerul de eliminat
     */
    void removeObserver(Observer<E> observer);

    /**
     * Notifica observerii despre un eveniment
     * @param event - evenimentul efectuat
     */
    void notifyObservers(ListEvent<E> event);
}

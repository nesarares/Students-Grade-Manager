package Utils;

import java.util.ArrayList;
import java.util.List;

public class AbstractObservable<E> implements Observable<E> {
    protected ArrayList<Observer<E>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<E> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<E> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(ListEvent<E> event) {
        observers.forEach(observer -> observer.notifyEvent(event));
    }

    protected ListEvent<E> createEvent(ListEventType type, List<E> list, E element) {
         return new ListEvent<E>(type) {
             @Override
             public List<E> getList() {
                 return list;
             }

             @Override
             public E getElement() {
                 return element;
             }
         };
    }
}

package mytools.function.object;


/**
 * Use this class when re-assigning a variable isn't possible in lambda context.
 */
public final class Holder<T> {

    private T object;

    public Holder() { }

    public Holder(T obj) {
        object = obj;
    }

    public T get() {
        return object;
    }

    public void set(T obj) {
        object = obj;
    }

    public T getAndSet(T obj) {
        T temp = object;
        object = obj;
        return temp;
    }

}

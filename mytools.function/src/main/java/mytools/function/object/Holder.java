package mytools.function.object;

public final class Holder<T> {
    
    private T object;

    public T get() {
        return object;
    }
    
    public void set(T obj) {
        object = obj;
    }
    
    public T setAndGet(T obj) {
        T temp = object;
        object = obj;
        return temp;
    }
        
}

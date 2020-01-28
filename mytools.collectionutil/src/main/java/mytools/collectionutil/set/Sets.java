package mytools.collectionutil.set;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Methods of this class do not modify their parameters and return new Set objects.
 */
public final class Sets {
    
    private Sets() {}

    public static <T> Set<T> union(Collection<T> a, Collection<T> b) {
        return union(a, b, defaultSetFactory());
    }
    
    public static <T> Set<T> union(Collection<T> a, Collection<T> b, Supplier<Set<T>> setFactory) {
        return withNewSet(a, setFactory, set -> set.addAll(b));
    }
    
    public static <T> Set<T> intersection(Collection<T> a, Collection<T> b) {
        return intersection(a,  b, defaultSetFactory());
    }
    
    public static <T> Set<T> intersection(
            Collection<T> a, Collection<T> b, Supplier<Set<T>> setFactory) {
        return withNewSet(a, setFactory, set -> set.retainAll(b));
    }

    public static <T> Set<T> difference(Collection<T> a, Collection<T> b) {
        return difference(a, b, defaultSetFactory());
    }
    
    public static <T> Set<T> difference(
            Collection<T> a, Collection<T> b, Supplier<Set<T>> setFactory) {
        return withNewSet(a, setFactory, set -> set.removeAll(b));
    }

    public static <T> Set<T> complement(
            Collection<T> a, Collection<T> b, Supplier<Set<T>> setFactory) {
        return difference(union(a, b, setFactory), intersection(a, b, setFactory), setFactory);
    }
    
    public static <T> Set<T> complement(Collection<T> a, Collection<T> b) {
        return complement(a, b, defaultSetFactory());
    }
    
    private static <T> Set<T> withNewSet(
            Collection<T> a, Supplier<Set<T>> setFactory, Consumer<Set<T>> f) {
        Set<T> result = setFactory.get();
        result.addAll(a);
        f.accept(result);
        return result;
    }
    
    private static <T> Supplier<Set<T>> defaultSetFactory() { 
        return HashSet::new;
    }

}


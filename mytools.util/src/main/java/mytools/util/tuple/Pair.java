package mytools.util.tuple;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class Pair<T,U> {
    
    private final T first;
    private final U second;
    
    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }
    
    public T _1() {
        return first;
    }

    public U _2() {
        return second;
    }

    public Object[] asArray() {
        return new Object[] {first, second};
    }
    
    public static <T> List<T> asList(Pair<T,T> pair) {
        return Arrays.<T>asList(pair._1(), pair._2());
    }
    
    // TODO add hashcode(), equals, compare()
    
    @Override
    public String toString() {
        return new StringBuilder("[").append(Objects.toString(first)).append(",")
                .append(Objects.toString(second)).append("]").toString();
    }

}


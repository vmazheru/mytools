package mytools.util.tuple;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class Pair<T, U> {

    private final T first;
    private final U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }

    public Object[] asArray() {
        return new Object[] {first, second};
    }

    public static <T> List<T> asList(Pair<T, T> pair) {
        return Arrays.<T>asList(pair.getFirst(), pair.getSecond());
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        @SuppressWarnings("rawtypes")
        Pair other = (Pair) obj;
        return Objects.equals(first, other.first) &&
               Objects.equals(second, other.second);
    }

    @Override
    public String toString() {
        return new StringBuilder("[")
                .append(Objects.toString(first)).append(",")
                .append(Objects.toString(second)).append("]")
                .toString();
    }

}


package mytools.util.tuple;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class Triple<T,U,R> {

    private final T first;
    private final U second;
    private final R third;

    public Triple(T first, U second, R third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public T _1() {
        return first;
    }

    public U _2() {
        return second;
    }

    public R _3() {
        return third;
    }

    public T getFirst() {
        return _1();
    }

    public U getSecond() {
        return _2();
    }

    public R getThird() {
        return _3();
    }

    public Object[] asArray() {
        return new Object[] {first, second, third};
    }

    public static <T> List<T> asList(Triple<T,T,T> t) {
        return Arrays.<T>asList(t._1(), t._2(), t._3());
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
        Triple other = (Triple) obj;
        return Objects.equals(first, other.first) &&
               Objects.equals(second, other.second) &&
               Objects.equals(third, other.third);
    }

    @Override
    public String toString() {
        return new StringBuilder("[")
                .append(Objects.toString(first)).append(",")
                .append(Objects.toString(second)).append(",")
                .append(Objects.toString(third)).append("]")
                .toString();
    }

}

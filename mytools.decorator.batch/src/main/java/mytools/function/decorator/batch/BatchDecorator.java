package mytools.function.decorator.batch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

import mytools.function.decorator.Decorator;



/**
 * Implementation of {@link Decorator} interface which processes collections in
 * fixed size batches.
 *
 * @param <T>  type of objects which the input collection contains
 * @param <U>  type of the second parameter passed to the decorator
 *             (beside the collection object itself)
 * @param <R>  type of the objects which the resulting list will have
 * @param <I>  type of the collection which must extend
 *             {@code java.lang.Iterable}
 */
class BatchDecorator<T, U, R, I extends Iterable<T>>
    implements Decorator<I, U, List<R>> {

    private final int batchSize;

    BatchDecorator(int batchSize) {
        this.batchSize = batchSize;
    }

    @Override
    public BiFunction<I, U, List<R>> decorate(BiFunction<I, U, List<R>> f) {
        return (iterable, u) -> {
            if (iterable == null) {
                throw new IllegalArgumentException(
                        "null list passed to a function");
            }

            if (!iterable.iterator().hasNext()) {
                return Collections.emptyList();
            }

            if (iterable instanceof List) {
                @SuppressWarnings("unchecked")
                List<T> castedIterable = (List<T>)iterable;
                return processList(castedIterable, u, f);
            }
            return processIterable(iterable, u, f);
        };
    }

    @SuppressWarnings("unchecked")
    private List<R> processIterable(
            I input, U u, BiFunction<I, U, List<R>> f) {
        List<R> result = new ArrayList<>();

        int i = 0;
        List<T> group = new ArrayList<>(batchSize);
        for (T t : input) {
            if (i >= batchSize) {
                addAllSafely(result, f.apply((I)group, u));
                group = new ArrayList<>(batchSize);
                i = 0;
            }
            group.add(t);
            i++;
        }

        if (!group.isEmpty()) {
            addAllSafely(result, f.apply((I)group, u));
        }

        return result.isEmpty() ? null : result;
    }

    @SuppressWarnings("unchecked")
    private List<R> processList(
            List<T> list, U u, BiFunction<I, U, List<R>> f) {
        List<R> result = new ArrayList<>();
        int size = list.size();
        int start = 0;
        int end = Math.min(batchSize, size);

        while (start < size) {
            addAllSafely(result, f.apply((I)list.subList(start, end), u));
            start = end;
            end = Math.min(end + batchSize, size);
        }
        return result.isEmpty() ? null : result;
    }

    protected static <T> void addAllSafely(List<T> dest, Iterable<T> src) {
        if (src != null) {
            for (T t : src) dest.add(t);
        }
    }
}

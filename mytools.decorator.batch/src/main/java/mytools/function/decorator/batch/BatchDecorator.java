package mytools.function.decorator.batch;

import static mytools.function.Conversions.toBF;
import static mytools.function.Conversions.toC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import mytools.function.decorator.Decorator;

/**
 * This decorator takes a function which maps {@code java.util.List} object
 * into a list of different type, and executes it in batches of the given size.
 *
 * @param <T> type of elements in the input list
 * @param <U> second parameter to the function
 * @param <R> type of elements in the output list
 */
class BatchDecorator<T, U, R> implements Decorator<List<T>, U, List<R>> {

    private final int batchSize;

    BatchDecorator(int batchSize) {
        this.batchSize = batchSize;
    }

    @Override
    public Consumer<List<T>> decorate(Consumer<List<T>> f) {
        return toC(decorate(toBF(f)));
    }

    @Override
    public BiFunction<List<T>, U, List<R>> decorate(
            BiFunction<List<T>, U, List<R>> f) {
        return (list, u) -> {
            if (list == null) {
                throw new IllegalArgumentException(
                        "null list passed to a function");
            }

            if (list.isEmpty()) {
                return Collections.emptyList();
            }

            List<R> result = new ArrayList<>();
            int size = list.size();
            int start = 0;
            int end = Math.min(batchSize, size);

            while (start < size) {
                result.addAll(f.apply(list.subList(start, end), u));
                start = end;
                end = Math.min(end + batchSize, size);
            }

            return result.isEmpty() ? null : result;
        };

    }
}

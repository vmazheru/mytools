package mytools.function.decorator.batch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

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

    protected final int batchSize;

    BatchDecorator(int batchSize) {
        this.batchSize = batchSize;
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
                List<R> batchResult = f.apply(list.subList(start, end), u);
                if (batchResult != null) {
                    result.addAll(batchResult);
                }
                start = end;
                end = Math.min(end + batchSize, size);
            }

            return result.isEmpty() ? null : result;
        };
    }
}

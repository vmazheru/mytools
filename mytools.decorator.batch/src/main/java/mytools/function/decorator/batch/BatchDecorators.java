package mytools.function.decorator.batch;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public interface BatchDecorators {

    //////////////////////// decorators ///////////////////////////

    /**
     * Decorate a consumer of a list with batching.
     *
     * @param <T> type of the input list element
     * @param batchSize  batch size
     * @param f a consumer which accepts an {@code List} object
     * @return  a consumer which applies batching to the code of the original
     *          consumer
     */
    static <T> Consumer<List<T>> batched(int batchSize, Consumer<List<T>> f) {
        return new BatchDecorator<T, Object, Object>(batchSize).decorate(f);
    }

    /**
     * Decorate a consumer of a list with batching.
     *
     * Every batch is guaranteed to hold elements which belong to the same group
     * according to the given group function.
     *
     * The number of elements in the batch may be
     * less then the given batch size, since the next group may not fit in.
     *
     * A batch may contain elements from more then one group, if the total
     * number of elements in these groups is less then or equal to the batch
     * size.
     *
     * Method throws run time exception if any group size is greater than the
     * given batch size (that is a group is too large comparatively to the
     * batch size).
     *
     * @param <T> type of the input list element
     * @param <C> comparable type of the group function return value
     * @param batchSize     batch size
     * @param inputSorted   true if input is sorted already according to the
     *                      group function
     * @param groupFunction a function which takes a list element and returns
     *                      a comparable value common to all elements in the
     *                      group
     * @param f             original list consumer
     * @return              a list consumer which applies batching to the logic
     *                      of the original consumer
     */
    static <T, C extends Comparable<C>> Consumer<List<T>> batched(
            int batchSize,
            boolean inputSorted,
            Function<T, C> groupFunction,
            Consumer<List<T>> f) {
        return new GroupedBatchDecorator<>(
                batchSize, inputSorted, groupFunction).decorate(f);
    }

    ////////////////// applications ///////////////////////////

    /**
     * Apply batching to the given list consumer
     *
     * @param <T>       list element type
     * @param input     input list
     * @param batchSize batch size
     * @param f         a consumer of a list
     */
    static <T> void batch(List<T> input, int batchSize, Consumer<List<T>> f) {
        batched(batchSize, f).accept(input);
    }

    /**
     * Apply grouped batching to the given list consumer
     *
     * @param <T>           list element type
     * @param <C>           type of the group function output
     * @param input         input list
     * @param batchSize     batch size
     * @param inputSorted   true if the input list is sorted according to the
     *                      group function output, false if it is not
     * @param groupFunction group function
     * @param f             a consumer of the list
     */
    static <T, C extends Comparable<C>> void batch(
            List<T> input,
            int batchSize,
            boolean inputSorted,
            Function<T, C> groupFunction,
            Consumer<List<T>> f) {
        batched(batchSize, inputSorted, groupFunction, f).accept(input);
    }

    /**
     * Apply grouped batching to the given list consumer. The input list is
     * not known as sorted according to the group function, so it will be sorted
     * first.
     *
     * @param <T>           list element type
     * @param <C>           type of the group function output
     * @param input         input list
     * @param batchSize     batch size
     * @param groupFunction group function
     * @param f             a consumer of the list
     */
    static <T, C extends Comparable<C>> void batch(
            List<T> input,
            int batchSize,
            Function<T, C> groupFunction,
            Consumer<List<T>> f) {
        batch(input, batchSize, false, groupFunction, f);
    }

}

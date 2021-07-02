package mytools.function.decorator.batch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import mytools.function.decorator.Decorator;

/**
 * Implementation of {@link Decorator} interface which processes collections in
 * fixed size batches with groups.
 * <p>
 * The decorator will try to break the given collection into batches of
 * the given size, but keep related objects together in the same batch.
 * The given "group" function defines which objects are considered "related".
 * <p>
 * If any of the groups as defined by the group function has size greater
 * than the given batch size, the decorator will throw an exception.
 * If multiple groups, on the other hand, can fit in one batch they will be
 * processed together.
 *
 * @param <T>  type of objects which the input collection contains
 * @param <U>  type of the second parameter passed to the decorator
 *             (beside the collection object itself)
 * @param <R>  type of the objects which the resulting list will have
 * @param <C>  type of the output of the group function
 */
class GroupedBatchDecorator <T, U, R, C extends Comparable<C>>
    implements Decorator<List<T>, U, List<R>> {

    private final boolean inputSorted;
    private final Function<T, C> groupFunction;
    private final int batchSize;

    GroupedBatchDecorator(
            int batchSize, boolean inputSorted, Function<T, C> groupFunction) {
        this.batchSize = batchSize;
        this.inputSorted = inputSorted;
        this.groupFunction = groupFunction;
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

            return processWithGroups(list, u, f);
        };
    }

    private List<R> processWithGroups(
            List<T> list, U u, BiFunction<List<T>, U, List<R>> f) {
        List<T> input = inputSorted ? list : sort(list);
        List<R> result = new ArrayList<>();
        List<T> part   = new ArrayList<>(batchSize);
        List<T> group  = new ArrayList<>(batchSize);
        C groupIdentifier = groupFunction.apply(input.iterator().next());

        for (T t : input) {
            C identifier = groupFunction.apply(t);
            if (identifier.compareTo(groupIdentifier) != 0) { //new group starts
                int groupSize = group.size();
                checkGroupSize(groupSize);
                if (part.size() + groupSize > batchSize) {
                    result.addAll(f.apply(part, u));
                    part.clear();
                }
                part.addAll(group);
                group.clear();
                groupIdentifier = identifier;
            }
            group.add(t);
        }

        int groupSize = group.size();
        checkGroupSize(groupSize);
        int partSize = part.size();
        if (partSize + groupSize > batchSize) {
            if (partSize  != 0) addAllSafely(result, f.apply(part, u));
            if (groupSize != 0) addAllSafely(result, f.apply(group, u));
        } else {
            if (groupSize != 0) addAllSafely(part, group);
            if (!part.isEmpty()) addAllSafely(result, f.apply(part, u));
        }

        return result.isEmpty() ? null : result;
    }

    private List<T> sort(List<T> input) {
        List<T> l = new ArrayList<>();
        for (T t : input) l.add(t);
        Comparator<T> c = (t1, t2) -> groupFunction.apply(
                t1).compareTo(groupFunction.apply(t2));
        Collections.sort(l, c);
        return l;
    }

    private void checkGroupSize(int groupSize) {
        if (groupSize > batchSize) {
            throw new RuntimeException(
                    "Cannot fit a group of items of size " + groupSize +
                    " into a batch of size " + batchSize +
                    ". Batch size is too small");
        }
    }

    private static <T> void addAllSafely(List<T> dest, List<T> src) {
        if (src != null) dest.addAll(src);
    }

}

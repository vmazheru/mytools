package mytools.function.decorator.batch;

import java.util.List;
import java.util.function.Consumer;

public interface BatchDecorators {

    ////////////////////////decorators ///////////////////////////

    /**
     * Decorate a consumer of a collection with batching.
     *
     * @param batchSize  batch size
     * @param f a consumer which accepts an {@code List} object
     * @return  a consumer which applies batching to the code of the original consumer
     */
    static <T> Consumer<List<T>> batched(int batchSize, Consumer<List<T>> f) {

        return new BatchDecorator<T, Object, Object>(batchSize).decorate(f);


    }

}

package mytools.function.decorator.batch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

public class GroupedBatchDecoratorTest {

    @Test
    public void applesAndOranges() {
        List<String> applesAndOranges = Arrays.asList(
                "Apple 1", "Apple 2", "Avocado 1",
                "Orange 1", "Orange 2", "Orange 3", "Orange 4", "Orange 5");

        GroupedBatchDecorator<String, Object, String, String> d =
                new GroupedBatchDecorator<>(5, true, s -> s.split(" ")[0]);

        List<List<String>> batches = new ArrayList<>();

        d.decorate((Consumer<List<String>>) batch -> batches.add(batch))
            .accept(applesAndOranges);

        assertEquals(List.of(
                List.of("Apple 1", "Apple 2", "Avocado 1"),
                List.of("Orange 1", "Orange 2", "Orange 3",
                        "Orange 4", "Orange 5")),
                batches);
    }

}

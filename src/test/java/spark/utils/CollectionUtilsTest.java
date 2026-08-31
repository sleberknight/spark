package spark.utils;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;

import java.util.Collection;

public class CollectionUtilsTest {

    @Test
    public void testIsEmpty_whenCollectionIsEmpty_thenReturnTrue() {

        Collection<Object> testCollection = new ArrayList<>();

        assertThat(CollectionUtils.isEmpty(testCollection)).as("Should return true because collection is empty").isTrue();

    }

    @Test
    public void testIsEmpty_whenCollectionIsNotEmpty_thenReturnFalse() {

        Collection<Integer> testCollection = new ArrayList<>();
        testCollection.add(1);
        testCollection.add(2);

        assertThat(CollectionUtils.isEmpty(testCollection)).as("Should return false because collection is not empty").isFalse();

    }

    @Test
    public void testIsEmpty_whenCollectionIsNull_thenReturnTrue() {

        Collection<Integer> testCollection = null;

        assertThat(CollectionUtils.isEmpty(testCollection)).as("Should return true because collection is null").isTrue();

    }

    @Test
    public void testIsNotEmpty_whenCollectionIsEmpty_thenReturnFalse() {

        Collection<Object> testCollection = new ArrayList<>();

        assertThat(CollectionUtils.isNotEmpty(testCollection)).as("Should return false because collection is empty").isFalse();

    }

    @Test
    public void testIsNotEmpty_whenCollectionIsNotEmpty_thenReturnTrue() {

        Collection<Integer> testCollection = new ArrayList<>();
        testCollection.add(1);
        testCollection.add(2);

        assertThat(CollectionUtils.isNotEmpty(testCollection)).as("Should return true because collection is not empty").isTrue();

    }

    @Test
    public void testIsNotEmpty_whenCollectionIsNull_thenReturnFalse() {

        Collection<Object> testCollection = null;

        assertThat(CollectionUtils.isNotEmpty(testCollection)).as("Should return false because collection is null").isFalse();

    }
}

package spark.utils;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ObjectUtilsTest {

    @Test
    public void testIsEmpty_whenArrayIsEmpty() throws Exception {

        assertThat(ObjectUtils.isEmpty(new Object[]{}))
                .describedAs("Should return false because array is empty")
                .isTrue();

    }

    @Test
    public void testIsEmpty_whenArrayIsNotEmpty() throws Exception {

        assertThat(ObjectUtils.isEmpty(new Integer[]{1,2}))
                .describedAs("Should return false because array is not empty")
                .isFalse();

    }
}
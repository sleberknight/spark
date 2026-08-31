package spark.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SparkUtilsTest {

    @Test
    public void testConvertRouteToList() throws Exception {

        List<String> expected = Arrays.asList("api", "person", ":id");

        List<String> actual = SparkUtils.convertRouteToList("/api/person/:id");

        assertThat(actual).as("Should return route as a list of individual elements that path is made of")
                .isEqualTo(expected);

    }

    @Test
    public void testIsParam_whenParameterFormattedAsParm() throws Exception {

        assertThat(SparkUtils.isParam(":param")).as("Should return true because parameter follows convention of a parameter (:paramname)").isTrue();

    }

    @Test
    public void testIsParam_whenParameterNotFormattedAsParm() throws Exception {

        assertThat(SparkUtils.isParam(".param")).as("Should return false because parameter does not follows convention of a parameter (:paramname)").isFalse();

    }


    @Test
    public void testIsSplat_whenParameterIsASplat() throws Exception {

        assertThat(SparkUtils.isSplat("*")).as("Should return true because parameter is a splat (*)").isTrue();

    }

    @Test
    public void testIsSplat_whenParameterIsNotASplat() throws Exception {

        assertThat(SparkUtils.isSplat("!")).as("Should return true because parameter is not a splat (*)").isFalse();

    }
}
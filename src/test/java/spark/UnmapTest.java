package spark;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import spark.util.SparkTestUtil;

import static spark.Spark.awaitInitialization;
import static spark.Spark.get;

import static spark.Spark.unmap;

public class UnmapTest {

    SparkTestUtil testUtil = new SparkTestUtil(4567);

    @Test
    public void testUnmap() throws Exception {
        get("/tobeunmapped", (q, a) -> "tobeunmapped");
        awaitInitialization();

        SparkTestUtil.UrlResponse response = testUtil.doMethod("GET", "/tobeunmapped", null);
        assertThat(response.status).isEqualTo(200);
        assertThat(response.body).isEqualTo("tobeunmapped");

        unmap("/tobeunmapped");

        response = testUtil.doMethod("GET", "/tobeunmapped", null);
        assertThat(response.status).isEqualTo(404);

        get("/tobeunmapped", (q, a) -> "tobeunmapped");

        response = testUtil.doMethod("GET", "/tobeunmapped", null);
        assertThat(response.status).isEqualTo(200);
        assertThat(response.body).isEqualTo("tobeunmapped");

        unmap("/tobeunmapped", "get");

        response = testUtil.doMethod("GET", "/tobeunmapped", null);
        assertThat(response.status).isEqualTo(404);
    }
}

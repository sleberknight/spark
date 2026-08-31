package spark;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class RouteImplTest {

    private final static String PATH_TEST = "/opt/test";
    private final static String ACCEPT_TYPE_TEST  = "*/test";

    private RouteImpl route;

    @Test
    public void testConstructor(){
        route = new RouteImpl(PATH_TEST) {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                return null;
            }
        };
        assertThat(route.getPath()).as("Should return path specified").isEqualTo(PATH_TEST);
    }

    @Test
    public void testGets_thenReturnGetPathAndGetAcceptTypeSuccessfully() throws Exception {
        route = RouteImpl.create(PATH_TEST, ACCEPT_TYPE_TEST, null);
        assertAll(
                () -> assertThat(route.getPath()).as("Should return path specified").isEqualTo(PATH_TEST),
                () -> assertThat(route.getAcceptType()).as("Should return accept type specified").isEqualTo(ACCEPT_TYPE_TEST)
        );
    }

    @Test
    public void testCreate_whenOutAssignAcceptTypeInTheParameters_thenReturnPathAndAcceptTypeSuccessfully(){
        route = RouteImpl.create(PATH_TEST, null);
        assertAll(
                () -> assertThat(route.getPath()).as("Should return path specified").isEqualTo(PATH_TEST),
                () -> assertThat(route.getAcceptType()).as("Should return the default accept type").isEqualTo(RouteImpl.DEFAULT_ACCEPT_TYPE)
        );
    }

    @Test
    public void testCreate_whenAcceptTypeNullValueInTheParameters_thenReturnPathAndAcceptTypeSuccessfully(){
        route = RouteImpl.create(PATH_TEST, null, null);
        assertAll(
                () -> assertThat(route.getPath()).as("Should return path specified").isEqualTo(PATH_TEST),
                () -> assertThat(route.getAcceptType()).as("Should return the default accept type").isEqualTo(RouteImpl.DEFAULT_ACCEPT_TYPE)
        );
    }

    @Test
    public void testRender_whenElementParameterValid_thenReturnValidObject() throws Exception {
        String finalObjValue = "object_value";
        route = RouteImpl.create(PATH_TEST, null);
        Object value = route.render(finalObjValue);
        assertAll(
                () -> assertThat(value).as("Should return an Object because we configured it to have one").isNotNull(),
                () -> assertThat(value.toString()).as("Should return a string object specified").isEqualTo(finalObjValue)
        );
    }

    @Test
    public void testRender_whenElementParameterIsNull_thenReturnNull() throws Exception {
        route = RouteImpl.create(PATH_TEST, null);
        Object value = route.render(null);
        assertThat(value).as("Should return null because the element from render is null").isNull();
    }
}
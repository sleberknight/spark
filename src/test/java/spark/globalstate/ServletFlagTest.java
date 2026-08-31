package spark.globalstate;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kiwiproject.reflect.KiwiReflection;

import static org.assertj.core.api.Assertions.assertThat;

public class ServletFlagTest {

    @BeforeEach
    public void setup() throws NoSuchFieldException {
        KiwiReflection.setFieldValue(null, isRunningFromServletField(), new AtomicBoolean(false));
    }

    @Test
    public void testRunFromServlet_whenDefault() throws Exception {

        AtomicBoolean isRunningFromServlet = (AtomicBoolean) KiwiReflection.getFieldValue(null, isRunningFromServletField());
        assertThat(isRunningFromServlet.get()).as("Should be false because it is the default value").isFalse();
    }

    @Test
    public void testRunFromServlet_whenExecuted() throws Exception {

        ServletFlag.runFromServlet();
        AtomicBoolean isRunningFromServlet = (AtomicBoolean) KiwiReflection.getFieldValue(null, isRunningFromServletField());

        assertThat(isRunningFromServlet.get()).as("Should be true because it flag has been set after runFromServlet").isTrue();
    }

    @Test
    public void testIsRunningFromServlet_whenDefault() throws Exception {

        assertThat(ServletFlag.isRunningFromServlet()).as("Should be false because it is the default value").isFalse();

    }

    @Test
    public void testIsRunningFromServlet_whenRunningFromServlet() throws Exception {

        ServletFlag.runFromServlet();
        assertThat(ServletFlag.isRunningFromServlet()).as("Should be true because call to runFromServlet has been made").isTrue();
    }

    private static Field isRunningFromServletField() throws NoSuchFieldException {
        Field field = ServletFlag.class.getDeclaredField("isRunningFromServlet");
        field.setAccessible(true);
        return field;
    }
}

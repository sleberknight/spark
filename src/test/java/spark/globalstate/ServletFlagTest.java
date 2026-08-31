package spark.globalstate;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kiwiproject.reflect.KiwiReflection;

import static org.assertj.core.api.Assertions.assertThat;

public class ServletFlagTest {

    @BeforeEach
    public void setUp() {
        KiwiReflection.setFieldValue(null, isRunningFromServletField(), new AtomicBoolean(false));
    }

    @Test
    public void testRunFromServlet_whenDefault() {

        AtomicBoolean isRunningFromServlet = KiwiReflection.getTypedFieldValue(null, isRunningFromServletField(), AtomicBoolean.class);
        assertThat(isRunningFromServlet.get()).describedAs("Should be false because it is the default value").isFalse();
    }

    @Test
    public void testRunFromServlet_whenExecuted() {

        ServletFlag.runFromServlet();
        AtomicBoolean isRunningFromServlet = KiwiReflection.getTypedFieldValue(null, isRunningFromServletField(), AtomicBoolean.class);

        assertThat(isRunningFromServlet.get()).describedAs("Should be true because it flag has been set after runFromServlet").isTrue();
    }

    @Test
    public void testIsRunningFromServlet_whenDefault() {

        assertThat(ServletFlag.isRunningFromServlet()).describedAs("Should be false because it is the default value").isFalse();

    }

    @Test
    public void testIsRunningFromServlet_whenRunningFromServlet() {

        ServletFlag.runFromServlet();
        assertThat(ServletFlag.isRunningFromServlet()).describedAs("Should be true because call to runFromServlet has been made").isTrue();
    }

    private static Field isRunningFromServletField() {
        return KiwiReflection.findField(new ServletFlag(), "isRunningFromServlet");
    }
}

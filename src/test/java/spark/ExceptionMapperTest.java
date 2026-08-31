package spark;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;
import org.kiwiproject.reflect.KiwiReflection;

import static org.assertj.core.api.Assertions.assertThat;

public class ExceptionMapperTest {


    @Test
    public void testGetInstance_whenDefaultInstanceIsNull() throws NoSuchFieldException {
        //given
        ExceptionMapper exceptionMapper = null;
        Field servletInstanceField = servletInstanceField();
        KiwiReflection.setFieldValue(null, servletInstanceField, exceptionMapper);

        //then
        exceptionMapper = ExceptionMapper.getServletInstance();
        assertThat(exceptionMapper).as("Should be equals because ExceptionMapper is a singleton").isEqualTo(KiwiReflection.getFieldValue(null, servletInstanceField));
    }

    @Test
    public void testGetInstance_whenDefaultInstanceIsNotNull() throws NoSuchFieldException {
        //given
        ExceptionMapper.getServletInstance(); //initialize Singleton

        //then
        ExceptionMapper exceptionMapper = ExceptionMapper.getServletInstance();
        assertThat(exceptionMapper).as("Should be equals because ExceptionMapper is a singleton").isEqualTo(KiwiReflection.getFieldValue(null, servletInstanceField()));
    }

    private static Field servletInstanceField() throws NoSuchFieldException {
        Field field = ExceptionMapper.class.getDeclaredField("servletInstance");
        field.setAccessible(true);
        return field;
    }
}

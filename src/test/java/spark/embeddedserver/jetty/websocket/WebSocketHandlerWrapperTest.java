package spark.embeddedserver.jetty.websocket;

import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class WebSocketHandlerWrapperTest {

    @Test
    public void testValidateHandlerClass_whenAnnotated_thenDoesNotThrow() {
        WebSocketHandlerWrapper.validateHandlerClass(AnnotatedHandler.class);
    }

    @Test
    public void testValidateHandlerClass_whenNotAnnotated_thenThrowIllegalArgumentException() {
        try {
            WebSocketHandlerWrapper.validateHandlerClass(PlainHandler.class);
            fail("Should have thrown an IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo("WebSocket handler must be annotated as '@WebSocket'");
        }
    }

    @WebSocket
    static class AnnotatedHandler {
    }

    static class PlainHandler {
    }
}

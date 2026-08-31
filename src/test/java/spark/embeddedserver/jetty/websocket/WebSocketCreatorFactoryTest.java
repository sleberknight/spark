package spark.embeddedserver.jetty.websocket;

import org.eclipse.jetty.ee11.websocket.server.JettyWebSocketCreator;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import spark.embeddedserver.jetty.websocket.WebSocketCreatorFactory.SparkWebSocketCreator;

public class WebSocketCreatorFactoryTest {

    @Test
    public void testCreateWebSocketHandler() {
        JettyWebSocketCreator creator =
                WebSocketCreatorFactory.create(new WebSocketHandlerClassWrapper(AnnotatedHandler.class));
        assertThat(creator instanceof SparkWebSocketCreator).isTrue();
        assertThat(SparkWebSocketCreator.class.cast(creator).getHandler() instanceof AnnotatedHandler).isTrue();
    }

    @Test
    public void testCreateWebSocket_alwaysReturnsSameHandlerInstance() throws Exception {
        JettyWebSocketCreator creator =
                WebSocketCreatorFactory.create(new WebSocketHandlerClassWrapper(AnnotatedHandler.class));
        Object handler = SparkWebSocketCreator.class.cast(creator).getHandler();

        assertThat(creator.createWebSocket(null, null)).isSameAs(handler);
        assertThat(creator.createWebSocket(null, null)).isSameAs(handler);
    }

    @Test
    public void testCannotCreateInvalidHandlers() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> WebSocketCreatorFactory.create(new WebSocketHandlerClassWrapper(InvalidHandler.class)))
                .withMessage("WebSocket handler must be annotated as '@WebSocket'");
    }

    @WebSocket
    static class AnnotatedHandler {
    }

    static class InvalidHandler {
    }
}

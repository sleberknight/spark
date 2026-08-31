package spark.embeddedserver.jetty;

import org.eclipse.jetty.server.ConnectionFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.junit.jupiter.api.Test;
import org.kiwiproject.reflect.KiwiReflection;
import spark.ssl.SslStores;

import java.lang.reflect.Field;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class SocketConnectorFactoryTest {

    @Test
    public void testCreateSocketConnector_whenServerIsNull_thenThrowException() {

        try {
            SocketConnectorFactory.createSocketConnector(null, "host", 80, true);
            fail("SocketConnector creation should have thrown an IllegalArgumentException");
        } catch(IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo("'server' must not be null");
        }
    }


    @Test
    public void testCreateSocketConnector_whenHostIsNull_thenThrowException() {

        Server server = new Server();

        try {
            SocketConnectorFactory.createSocketConnector(server, null, 80, true);
            fail("SocketConnector creation should have thrown an IllegalArgumentException");
        } catch(IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo("'host' must not be null");
        }
    }

    @Test
    public void testCreateSocketConnector() throws NoSuchFieldException {

        final String host = "localhost";
        final int port = 8888;

        Server server = new Server();
        ServerConnector serverConnector = SocketConnectorFactory.createSocketConnector(server, "localhost", 8888, true);

        String internalHost = (String) KiwiReflection.getFieldValue(serverConnector, declaredField(ServerConnector.class, "_host"));
        int internalPort = (int) KiwiReflection.getFieldValue(serverConnector, declaredField(ServerConnector.class, "_port"));
        Server internalServerConnector = (Server) KiwiReflection.getFieldValue(serverConnector, declaredField(ServerConnector.class, "_server"));

        assertThat(internalHost).as("Server Connector Host should be set to the specified server").isEqualTo(host);
        assertThat(internalPort).as("Server Connector Port should be set to the specified port").isEqualTo(port);
        assertThat(server).as("Server Connector Server should be set to the specified server").isEqualTo(internalServerConnector);
    }

    @Test
    public void testCreateSecureSocketConnector_whenServerIsNull() {

        try {
            SocketConnectorFactory.createSecureSocketConnector(null, "localhost", 80, (SslStores) null, true);
            fail("SocketConnector creation should have thrown an IllegalArgumentException");
        } catch(IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo("'server' must not be null");
        }
    }

    @Test
    public void testCreateSecureSocketConnector_whenHostIsNull() {

        Server server = new Server();

        try {
            SocketConnectorFactory.createSecureSocketConnector(server, null, 80, (SslStores) null, true);
            fail("SocketConnector creation should have thrown an IllegalArgumentException");
        } catch(IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo("'host' must not be null");
        }
    }

    @Test
    public void testCreateSecureSocketConnector_whenSslStoresIsNull() {

        Server server = new Server();

        try {
            SocketConnectorFactory.createSecureSocketConnector(server, "localhost", 80, (SslStores) null, true);
            fail("SocketConnector creation should have thrown an IllegalArgumentException");
        } catch(IllegalArgumentException ex) {
            assertThat(ex.getMessage()).isEqualTo("'sslStores' must not be null");
        }
    }


    @Test
    public void testCreateSecureSocketConnector() throws  Exception {

        final String host = "localhost";
        final int port = 8888;

        // Jetty 12 validates that the keystore/truststore paths are accessible as soon as
        // they're set (Jetty 9 deferred this until the SslContextFactory actually started),
        // so this needs a real file rather than a placeholder name.
        final String keystoreFileName = "keystore.jks";
        final String keystoreFile = "src/test/resources/" + keystoreFileName;
        final String keystorePassword = "keystorePassword";
        final String truststoreFileName = keystoreFileName;
        final String truststoreFile = keystoreFile;
        final String trustStorePassword = "trustStorePassword";

        SslStores sslStores = SslStores.create(keystoreFile, keystorePassword, truststoreFile, trustStorePassword);

        Server server = new Server();

        ServerConnector serverConnector = SocketConnectorFactory.createSecureSocketConnector(server, host, port, sslStores, true);

        String internalHost = (String) KiwiReflection.getFieldValue(serverConnector, declaredField(ServerConnector.class, "_host"));
        int internalPort = (int) KiwiReflection.getFieldValue(serverConnector, declaredField(ServerConnector.class, "_port"));

        assertThat(internalHost).as("Server Connector Host should be set to the specified server").isEqualTo(host);
        assertThat(internalPort).as("Server Connector Port should be set to the specified port").isEqualTo(port);

        @SuppressWarnings("unchecked")
        Map<String, ConnectionFactory> factories = (Map<String, ConnectionFactory>) KiwiReflection.getFieldValue(serverConnector, declaredField(ServerConnector.class, "_factories"));

        assertThat(factories.containsKey("ssl") && factories.get("ssl") != null).as("Should return true because factory for SSL should have been set").isTrue();

        SslConnectionFactory sslConnectionFactory = (SslConnectionFactory) factories.get("ssl");
        SslContextFactory sslContextFactory = sslConnectionFactory.getSslContextFactory();

        assertThat(sslContextFactory.getKeyStoreResource().getFileName()).as("Should return the Keystore file specified").isEqualTo(keystoreFileName);

        assertThat(sslContextFactory.getTrustStoreResource().getFileName()).as("Should return the Truststore file specified").isEqualTo(truststoreFileName);

    }

    // _host/_port/_server/_factories are declared on AbstractConnector, a superclass of
    // ServerConnector, so KiwiReflection's own field lookup (which only looks at the target's
    // exact runtime class) can't find them; nonStaticFieldsInHierarchy() walks the hierarchy
    // for us, though it doesn't set accessibility, so that's still on us.
    private static Field declaredField(Class<?> type, String fieldName) throws NoSuchFieldException {
        Field field = KiwiReflection.nonStaticFieldsInHierarchy(type).stream()
                .filter(f -> f.getName().equals(fieldName))
                .findFirst()
                .orElseThrow(() -> new NoSuchFieldException(fieldName));
        field.setAccessible(true);
        return field;
    }

}

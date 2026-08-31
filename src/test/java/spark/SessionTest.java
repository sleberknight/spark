package spark;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kiwiproject.reflect.KiwiReflection;

import jakarta.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class SessionTest {

    Request request;
    HttpSession httpSession;
    Session session;

    @BeforeEach
    public void setUp() {

        httpSession = mock(HttpSession.class);
        request = mock(Request.class);
        session = new Session(httpSession, request);
    }

    @Test
    public void testSession_whenHttpSessionIsNull_thenThrowException() {

        try {

            new Session(null, request);
            fail("Session instantiation with a null HttpSession should throw an IllegalArgumentException");

        } catch (IllegalArgumentException ex) {

            assertThat(ex.getMessage()).isEqualTo("session cannot be null");
        }
    }

    @Test
    public void testSession_whenRequestIsNull_thenThrowException() {

        try {

            new Session(httpSession, null);
            fail("Session instantiation with a null Request should throw an IllegalArgumentException");

        } catch (IllegalArgumentException ex) {

            assertThat(ex.getMessage()).isEqualTo("request cannot be null");
        }
    }

    @Test
    public void testSession() {

        HttpSession internalSession = KiwiReflection.getTypedFieldValue(session, "session", HttpSession.class);
        assertThat(internalSession).as("Internal session should be set to the http session provided during instantiation").isEqualTo(httpSession);
    }

    @Test
    public void testRaw() {

        assertThat(session.raw()).as("Should return the HttpSession provided during instantiation").isEqualTo(httpSession);
    }

    @Test
    public void testAttribute_whenAttributeIsRetrieved() {

        when(httpSession.getAttribute("name")).thenReturn("Jett");

        assertThat((String) session.attribute("name")).as("Should return attribute from HttpSession").isEqualTo("Jett");

    }

    @Test
    public void testAttribute_whenAttributeIsSet() {

        session.attribute("name", "Jett");

        verify(httpSession).setAttribute("name", "Jett");
    }

    @Test
    public void testAttributes() {

        Set<String> attributes = new HashSet<>(Arrays.asList("name", "location"));

        when(httpSession.getAttributeNames()).thenReturn(Collections.enumeration(attributes));

        assertThat(session.attributes()).as("Should return attributes from the HttpSession").isEqualTo(attributes);
    }

    @Test
    public void testCreationTime() {

        when(httpSession.getCreationTime()).thenReturn(10000000l);

        assertThat(session.creationTime()).as("Should return creationTime from HttpSession").isEqualTo(10000000l);
    }

    @Test
    public void testId() {

        when(httpSession.getId()).thenReturn("id");

        assertThat(session.id()).as("Should return session id from HttpSession").isEqualTo("id");
    }

    @Test
    public void testLastAccessedTime() {

        when(httpSession.getLastAccessedTime()).thenReturn(20000000l);

        assertThat(session.lastAccessedTime()).as("Should return lastAccessedTime from HttpSession").isEqualTo(20000000l);
    }

    @Test
    public void testMaxInactiveInterval_whenRetrieved() {

        when(httpSession.getMaxInactiveInterval()).thenReturn(100);

        assertThat(session.maxInactiveInterval()).as("Should return maxInactiveInterval from HttpSession").isEqualTo(100);
    }

    @Test
    public void testMaxInactiveInterval_whenSet() {

        session.maxInactiveInterval(200);

        verify(httpSession).setMaxInactiveInterval(200);
    }

    @Test
    public void testInvalidate() {

        session.invalidate();

        verify(httpSession).invalidate();
    }

    @Test
    public void testIsNew() {

        when(httpSession.isNew()).thenReturn(true);

        assertThat(session.isNew()).as("Should return isNew status from HttpSession").isEqualTo(true);
    }

    @Test
    public void testRemoveAttribute() {

        session.removeAttribute("name");

        verify(httpSession).removeAttribute("name");
    }
}
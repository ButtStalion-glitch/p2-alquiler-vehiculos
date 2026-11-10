package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ClienteTest {

    @Test
    public void cliente() {
        Cliente c = new Cliente("Tomas Sanchez", "12345678X");
        assertEquals("Tomas Sanchez", c.getNombre());
        assertEquals("12345678X", c.getDni());
    }

    @Test
    public void devuelveJsCliente() {
        Cliente c = new Cliente("Tomas Sanchez", "12345678X");
        String json = c.toString();

        assertTrue(json.contains("\"nombre\":\"Tomas Sanchez\""));
        assertTrue(json.contains("\"dni\":\"12345678X\""));
    }
}


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PruebaJUnit {

    @Test
    public void pruebaSuma() {
        int resultado = 2 + 3;

        assertEquals(5, resultado);
    }
}
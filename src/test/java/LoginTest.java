import modelos.Usuario;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    @Test
    public void pruebaLoginExitoso() {
        // 1. Instanciamos el modelo Usuario de tu proyecto
        Usuario usuarioBD = new Usuario();
        usuarioBD.setUsuario("admin");
        usuarioBD.setContraseña("12345");

        // 2. Simulamos las credenciales ingresadas en la pantalla de inicio de sesión
        String usuarioIngresado = "admin";
        String claveIngresada = "12345";

        // 3. Evaluamos la lógica de autenticación
        boolean autenticado = usuarioBD.getUsuario().equals(usuarioIngresado) 
                           && usuarioBD.getContraseña().equals(claveIngresada);

        // AFIRMACIÓN: Si el usuario y la contraseña coinciden, debe retornar true
        assertTrue(autenticado, "El inicio de sesión debe ser exitoso con credenciales correctas");
    }

    @Test
    public void pruebaLoginClaveIncorrecta() {
        // 1. Datos almacenados en el modelo
        Usuario usuarioBD = new Usuario();
        usuarioBD.setUsuario("admin");
        usuarioBD.setContraseña("12345");

        // 2. Intento de inicio de sesión con clave incorrecta
        String usuarioIngresado = "admin";
        String claveIngresada = "clave_erronea_999";

        // 3. Evaluamos la lógica
        boolean autenticado = usuarioBD.getUsuario().equals(usuarioIngresado) 
                           && usuarioBD.getContraseña().equals(claveIngresada);

        // AFIRMACIÓN: Si la clave no coincide, la autenticación debe retornar false
        assertFalse(autenticado, "El inicio de sesión debe ser denegado cuando la clave es incorrecta");
    }
}
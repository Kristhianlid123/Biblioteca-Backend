package controlador;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dao.UsuarioDAO;
import modelos.Usuario;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/api/login")
public class ApiLoginServlet extends HttpServlet {

    // Manejo de solicitudes Pre-flight para evitar bloqueos de CORS en React
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Configuración de encabezados para CORS y JSON
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();

        try {
            // Lectura del JSON enviado desde React
            
            StringBuilder jsonBuffer = new StringBuilder();
            String linea;
            while ((linea = request.getReader().readLine()) != null) {
                jsonBuffer.append(linea);
            }

            // Extraccion de las claves, usuario y contraseña del JSON
            
            JsonObject jsonObject = JsonParser.parseString(jsonBuffer.toString()).getAsJsonObject();
            
            String usuarioInput = jsonObject.has("usuario") ? jsonObject.get("usuario").getAsString() : "";
            String contrasenaInput = jsonObject.has("contrasena") ? jsonObject.get("contrasena").getAsString() : "";

            // Instancia de DAO y llamar el metodo validarLogin
            
            UsuarioDAO dao = new UsuarioDAO();
            Usuario usuarioEncontrado = dao.validarLogin(usuarioInput, contrasenaInput);

            // Si el método no devuelve null, las credenciales existen en MySQL
            if (usuarioEncontrado != null) {
                response.setStatus(HttpServletResponse.SC_OK); // Código 200
                out.print("{\"estado\":\"exito\",\"mensaje\":\"Autenticación satisfactoria\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Código 401
                out.print("{\"estado\":\"error\",\"mensaje\":\"Error en la autenticación\"}");
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // Código 500
            out.print("{\"estado\":\"error\",\"mensaje\":\"Error en el servidor: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }
}

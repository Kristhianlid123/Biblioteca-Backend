package controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
        name = "Error404Servlet",
        urlPatterns = {"/error/404"}
)
public class Error404Servlet extends HttpServlet {

    @Override
    protected void service(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // aqui React lee el mensaje de error de CORS
        
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");

        // Indicamos que la respuesta será JSON
        response.setContentType("application/json;charset=UTF-8");

        // Establecemos el código HTTP 404
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);

        // Obtenemos la URL que el usuario intentó consultar
        String uri = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (uri == null) {
            uri = request.getRequestURI();
        }

        // Construccion del mensaje de error en formato JSON
        String json = "{"
                + "\"error\":\"Endpoint no encontrado\","
                + "\"codigo\":404,"
                + "\"ruta\":\"" + uri + "\""
                + "}";

        // Enviar el JSON como respuesta
        response.getWriter().println(json);
    }
}

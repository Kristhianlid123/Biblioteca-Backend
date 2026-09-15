package controlador;

import com.google.gson.Gson;
import dao.LectorDAO;
import modelos.Lector;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ApiRegistroServlet", urlPatterns = {"/api/registro"})
public class ApiRegistroServlet extends HttpServlet {

    // Habilitar peticiones CORS  desde React
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

        // Lectura del cuerpo del JSON enviado por React
        StringBuilder jsonBuffer = new StringBuilder();
        String linea;
        while ((linea = request.getReader().readLine()) != null) {
            jsonBuffer.append(linea);
        }

        // Mapeo del texto JSON en el modelo lector con Gson
        
        Gson gson = new Gson();
        Lector lector = gson.fromJson(jsonBuffer.toString(), Lector.class);

        try {
            // Conectar con LectorDAO
            LectorDAO dao = new LectorDAO();

            if (dao.existeLector(lector.getDocumento())) {
                response.setStatus(HttpServletResponse.SC_CONFLICT); // 409
                out.print("{\"estado\":\"error\",\"mensaje\":\"El lector ya existe en la base de datos\"}");
                return;
            }

            boolean registrado = dao.registrarLector(lector);

            if (registrado) {
                response.setStatus(HttpServletResponse.SC_CREATED); // 201
                out.print("{\"estado\":\"exito\",\"mensaje\":\"Lector registrado correctamente\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST); // 400
                out.print("{\"estado\":\"error\",\"mensaje\":\"No se pudo registrar el lector\"}");
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
            out.print("{\"estado\":\"error\",\"mensaje\":\"Error en el servidor: " + e.getMessage() + "\"}");
        } finally {
            out.flush();
        }
    }
}

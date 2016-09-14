package util;

import bean.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpServletUtil {
    
    public static boolean hasAuthorization(HttpServletRequest request) {
        return (request.getHeader("Authorization") != null);
    }
    
    public static boolean isPreflightRequest(HttpServletRequest request) {
        
        if (request.getHeader("Access-Control-Request-Method") == null) {
            return false;
        }
        
        return "OPTIONS".equals(request.getMethod());
    }
    
    public static boolean isXMLHttpRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
    

    public static void addWwwAuthenticateHeader(HttpServletResponse response, String realmName) throws IOException {
        response.addHeader("WWW-Authenticate", "Basic realm=\"" + realmName + "\"");
    }
    
    public static void addAccessControlHeader(HttpServletResponse response, String allowOrigin, String allowCredentials) {
        response.addHeader("Access-Control-Allow-Origin", allowOrigin);
        response.addHeader("Access-Control-Allow-Credentials", allowCredentials);
    }
    
    public static void setJsonBody(HttpServletResponse response, ErrorResponse error) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ObjectMapper mapper = new ObjectMapper();
        PrintWriter writer = response.getWriter();
        mapper.writeValue(writer, error);
    }
    
    public static void setJsonBody(HttpServletResponse response, String jsonString) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter writer = response.getWriter();
        writer.write(jsonString);
    }

}

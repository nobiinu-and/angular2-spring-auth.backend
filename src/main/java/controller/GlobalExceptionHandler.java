package controller;

import bean.ErrorResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler({ Exception.class })
    @ResponseBody
    public ErrorResponse handleIllegalArgumentException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        LOGGER.debug(ex.toString());

        // ResponseStatus アノテーションを使っている例外は、指定されているコードを使う
        if (ex.getClass().isAnnotationPresent(ResponseStatus.class)) {
            ResponseStatus status = ex.getClass().getAnnotation(ResponseStatus.class);
            LOGGER.debug("has ResponseStatus: value = " + status.value());
            response.setStatus(status.value().value());
            return new ErrorResponse(ex.getLocalizedMessage());
        }
        
        if ( ex instanceof IllegalArgumentException ) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return new ErrorResponse("Illegal Parameter");
        }

        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return new ErrorResponse("Unknown Error");
    }
}

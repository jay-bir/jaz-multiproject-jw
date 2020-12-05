package pl.edu.pjwstk.jazapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WrongSortParamsException.class)
    public ResponseEntity<String> handleWrongSortParamsException(WrongSortParamsException e){
        return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}


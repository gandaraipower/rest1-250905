package com.rest1.global.golbaclExceptionHandler;

import com.rest1.global.rsData.RsData;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GLobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseBody
    public RsData<Void> handleException(){
        return new RsData<Void>(
                "404-1",
                "존재하지 않는 데이터입니다."
        );
    }
}

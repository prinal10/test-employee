package com.example.testemployee.exceptions.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionDTO {

    private List<Exception> exceptionList;


    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Exception {
        private String errorSource;
        private String errorTitle;
    }
}

package org.superdata.medismart.common.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    private Integer code;

    private Object[] args;

    private String message;

    private String detailMessage;

    public FileException(String message)
    {
        this.message = message;
    }

    public FileException(String message, Integer code)
    {
        this.message = message;
        this.code = code;
    }


}

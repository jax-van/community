package com.jaxvan.community.dto;

import com.jaxvan.community.exception.CustomizeErrorCode;
import com.jaxvan.community.exception.CustomizeException;
import lombok.Data;

@Data
public class ResponseDTO<T> {
    private Integer code;
    private String message;
    private T data;

    public static ResponseDTO errorOf(Integer code, String message) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(code);
        responseDTO.setMessage(message);
        return responseDTO;
    }

    public static ResponseDTO errorOf(CustomizeErrorCode errorCode) {
        return errorOf(errorCode.getCode(), errorCode.getMessage());
    }

    public static ResponseDTO errorOf(CustomizeException ex) {
        return errorOf(ex.getCode(), ex.getMessage());
    }

    public static ResponseDTO okOf() {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(200);
        responseDTO.setMessage("请求成功");
        return responseDTO;
    }


    public static <T> ResponseDTO okOf(T t) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(200);
        responseDTO.setMessage("请求成功");
        responseDTO.setData(t);
        return responseDTO;
    }
}

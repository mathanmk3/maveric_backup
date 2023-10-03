
package com.citi.train.uitils;

import org.modelmapper.ModelMapper;

import java.util.Optional;

public class CommonUtils {

    private CommonUtils(){

    }

    public static<T> boolean checkNullable(T map) {
        return Optional.ofNullable(map).isEmpty();
    }

    public static <T, R> R getMapper(T mapFrom, Class<R> mapTo) {
        ModelMapper modelMapper = new ModelMapper();
        try {
            return modelMapper.map(mapFrom, mapTo);
        } catch (Exception e) {
            e.getStackTrace();
            return null;
        }

    }

}


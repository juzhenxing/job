package com.zxkj.job.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author juzhenxing 2018/2/28 19:21
 */
@Component
public class CastUtil {

    @Autowired
    private ObjectMapper objectMapper;

    public <T> T mapToObject(Object o, Class<T> type){
        return objectMapper.convertValue(o, type);

    }

    public <T> List<T> mapToList(Object o, Class<T> type) throws IOException {
        JavaType javaType = getCollectionType(ArrayList.class, type);
        return (List<T>)objectMapper.readValue(o.toString(), javaType);
    }

    public JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    public <T> List<T> linkHashMapToList(Object o, Class<T> type) throws IOException {
        return objectMapper.readValue(objectMapper.writeValueAsString(o),new TypeReference<List<T>>() { });
    }

}
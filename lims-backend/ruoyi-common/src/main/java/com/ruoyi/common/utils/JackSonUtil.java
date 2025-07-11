package com.ruoyi.common.utils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * JSON解析处理
 *
 * @author 张宾
 */
@Component
public class JackSonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ObjectWriter OBJECT_WRITER = OBJECT_MAPPER.writerWithDefaultPrettyPrinter();

    public static void marshal(File file, Object value) throws Exception {
        try {
            OBJECT_WRITER.writeValue(file, value);
        } catch (JsonGenerationException e) {
            throw new Exception(e);
        } catch (JsonMappingException e) {
            throw new Exception(e);
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    public static void marshal(OutputStream os, Object value) throws Exception {
        try {
            OBJECT_WRITER.writeValue(os, value);
        } catch (JsonGenerationException e) {
            throw new Exception(e);
        } catch (JsonMappingException e) {
            throw new Exception(e);
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    public static String marshal(Object value) throws Exception {
        try {
            return OBJECT_WRITER.writeValueAsString(value);
        } catch (JsonGenerationException e) {
            throw new Exception(e);
        } catch (JsonMappingException e) {
            throw new Exception(e);
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    public static byte[] marshalBytes(Object value) throws Exception {
        try {
            return OBJECT_WRITER.writeValueAsBytes(value);
        } catch (JsonGenerationException e) {
            throw new Exception(e);
        } catch (JsonMappingException e) {
            throw new Exception(e);
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    public static <T> T unmarshal(File file, Class<T> valueType) throws Exception {
        try {
            return OBJECT_MAPPER.readValue(file, valueType);
        } catch (JsonParseException e) {
            throw new Exception(e);
        } catch (JsonMappingException e) {
            throw new Exception(e);
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    public static <T> T unmarshal(InputStream is, Class<T> valueType) throws Exception {
        try {
            return OBJECT_MAPPER.readValue(is, valueType);
        } catch (JsonParseException e) {
            throw new Exception(e);
        } catch (JsonMappingException e) {
            throw new Exception(e);
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    /**
     * 字符串转对象
     * @param str
     * @param valueType
     * @return
     * @param <T>
     * @throws Exception
     */
    public static <T> T unmarshal(String str, Class<T> valueType) throws Exception {
        try {
            OBJECT_MAPPER.registerModule(new JavaTimeModule());
            return OBJECT_MAPPER.readValue(str, valueType);
        } catch (JsonParseException e) {
            throw new Exception(e);
        } catch (JsonMappingException e) {
            throw new Exception(e);
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    public static <T> T unmarshal(byte[] bytes, Class<T> valueType) throws Exception {
        try {
            if (bytes == null) {
                bytes = new byte[0];
            }
            return OBJECT_MAPPER.readValue(bytes, 0, bytes.length, valueType);
        } catch (JsonParseException e) {
            throw new Exception(e);
        } catch (JsonMappingException e) {
            throw new Exception(e);
        } catch (IOException e) {
            throw new Exception(e);
        }
    }
}

package com.ruoyi.performance.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DecimalFormat;

public class CustomerDoubleSerialize extends JsonSerializer {
    private DecimalFormat df = new DecimalFormat("0.0000");

    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if(o.toString() != null && !o.toString().equals("0.0")) {
            Double dd=Double.parseDouble(o.toString());
            jsonGenerator.writeString(df.format(dd));
        } else{
            if(o.toString().equals("0.0")) {
                jsonGenerator.writeString("0");
            } else {
                jsonGenerator.writeString(o.toString());
            }
        }
    }
}

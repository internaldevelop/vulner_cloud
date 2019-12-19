package com.vulner.unify_auth.configuration.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Map;

/**
 * @author Jason
 * @create 2019/12/19
 * @since 1.0.0
 * @description 对 OAuth2 异常响应数据序列化
 */
public class MyOAuthExceptionJacksonSerializer extends StdSerializer<MyOAuth2Exception> {

    protected MyOAuthExceptionJacksonSerializer() {
        super(MyOAuth2Exception.class);
    }

    @Override
    public void serialize(MyOAuth2Exception value, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeStartObject();
        jgen.writeObjectField("code", value.getHttpErrorCode());
        jgen.writeStringField("msg", value.getMessage());
        if (value.getAdditionalInformation()!=null) {
            for (Map.Entry<String, String> entry : value.getAdditionalInformation().entrySet()) {
                String key = entry.getKey();
                String add = entry.getValue();
                jgen.writeStringField(key, add);
            }
        }
        jgen.writeEndObject();
    }
}

package saas.core.config.json;

import com.alibaba.fastjson.serializer.DateCodec;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;

public class DateTimeSerializer extends DateCodec {
		
	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features)
			throws IOException {
		
		SerializeWriter out = serializer.out;

        if (object == null) {
            out.writeString("");
            return;
        }
        		
		super.write(serializer,  object,  fieldName,  fieldType,  features);
	}


}

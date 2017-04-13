package feup.cm.traintickets.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.sql.Time;
import java.text.ParseException;

public class TimeDeserializer implements JsonDeserializer<Time> {

    @Override
    public Time deserialize(JsonElement jsonElement, Type typeOF,
                            JsonDeserializationContext context) throws JsonParseException {
        try {
            return Time.valueOf(jsonElement.getAsString());
        } catch (IllegalArgumentException ignored) { }
        throw new JsonParseException("Unparseable time: \"" + jsonElement.getAsString());
    }
}
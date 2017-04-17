package feup.cm.traintickets.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class DateDeserializer implements JsonDeserializer<java.util.Date> {

    private static final String[] DATE_FORMATS = new String[] {
            "yyyy-MM-dd hh:mm:ss",
            "yyyy-MM-dd",
            "hh:mm:ss"
    };

    @Override
    public Date deserialize(JsonElement jsonElement, Type typeOF,
                            JsonDeserializationContext context) throws JsonParseException {
        return deserialize(jsonElement.getAsString());
    }

    public Date deserialize(String date) throws JsonParseException {
        for (String format : DATE_FORMATS) {
            try {
                return new SimpleDateFormat(format, Locale.UK).parse(date);
            } catch (ParseException ignored) { }
        }
        throw new JsonParseException("Unparseable date: \"" + date
                + "\". Supported formats: " + Arrays.toString(DATE_FORMATS));
    }
}
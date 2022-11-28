package models.chatClients.fillOperations;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import models.Message;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class jsonChatFileOperations implements ChatFileOperations{

    private Gson gson;

    private static final String MESSAGE_FILE = ".messages.json";

    public jsonChatFileOperations(){
        gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimaSerializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimaDeserializer())
                .create();
    }

    @Override
    public void writeMessages(List<Message> message) {
    String jsonText = gson.toJson(message);
        System.out.println(jsonText);

        try {
            FileWriter writer = new FileWriter(MESSAGE_FILE);
            writer.write(jsonText);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Message> readMessage() {
        try{
            FileReader reader = new FileReader(MESSAGE_FILE);
            BufferedReader bufferedReader = new BufferedReader(reader);

            StringBuilder jsonText = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null){
                jsonText.append(line);
            }
            reader.close();

            Type targetType = new TypeToken<ArrayList<Message>>(){}.getType();
            List<Message> messages = gson.fromJson(jsonText.toString(),targetType);
         //GENERIKA   List<Message> messages = gson.fromJson<List<Message>>(jsonText);

            return messages;
        }catch (IOException e){
            e.printStackTrace();
        }

        return new ArrayList<>();
    }


}

class LocalDateTimaSerializer implements JsonSerializer<LocalDateTime>{

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(formatter.format(localDateTime));
    }
}

class LocalDateTimaDeserializer implements JsonDeserializer<LocalDateTime>{

    @Override
    public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return LocalDateTime.parse(jsonElement.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
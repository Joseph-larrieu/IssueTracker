package geiffel.da4.commentairetracker.commentaire;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import geiffel.da4.issuetracker.commentaire.Commentaire;

import java.io.IOException;

public class CommentaireJSONSerialize extends JsonSerializer<Commentaire> {

    @Override
    public void serialize(Commentaire commentaire, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("url","/comments/"+commentaire.getId());
        gen.writeEndObject();

    }
}

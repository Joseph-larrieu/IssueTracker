package geiffel.da4.issuetracker.issue;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class IssueJSONSerialize extends JsonSerializer<Issue> {

    @Override
    public void serialize(Issue issue, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("title",issue.getTitle());
        gen.writeStringField("url","/issues/"+issue.getId());
        gen.writeEndObject();

    }
}

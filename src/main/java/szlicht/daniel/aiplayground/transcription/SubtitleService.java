package szlicht.daniel.aiplayground.transcription;

import com.theokanning.openai.audio.CreateTranscriptionRequest;
import com.theokanning.openai.audio.TranscriptionResult;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class SubtitleService {

    private final OpenAiService openAiService;

    public SubtitleService(@Value("${openai.api.key}") String apiKey) {
        this.openAiService = new OpenAiService(apiKey);
    }

    public String transcribe(File videoFile) throws IOException {
        CreateTranscriptionRequest request = CreateTranscriptionRequest.builder()
                .model("whisper-1")
                .responseFormat("text")
                .build();

        TranscriptionResult result = openAiService.createTranscription(request, videoFile);
        // delete temp file after processing
        Files.deleteIfExists(videoFile.toPath());
        return result.getText();
    }
}

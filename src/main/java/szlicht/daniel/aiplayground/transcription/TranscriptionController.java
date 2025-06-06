package szlicht.daniel.aiplayground.transcription;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class TranscriptionController {

    private final SubtitleService subtitleService;

    public TranscriptionController(SubtitleService subtitleService) {
        this.subtitleService = subtitleService;
    }

    @PostMapping(value = "/transcribe", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String transcribe(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.getSize() > 25 * 1024 * 1024L) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "File exceeds 25MB limit enforced by the Whisper API");
        }

        // Save the uploaded file to a temporary location
        File temp = File.createTempFile("upload", ".mp4");
        file.transferTo(temp);
        return subtitleService.transcribe(temp);
    }
}

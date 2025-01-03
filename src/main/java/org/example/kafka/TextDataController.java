package org.example.kafka;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@RestController
public class TextDataController {

    private final TextDataProducer producer;

    public TextDataController(TextDataProducer producer) {
        this.producer = producer;
    }

    @PostMapping("/upload")
    public Optional<String> uploadTextFile(@RequestParam("file") MultipartFile file) throws IOException {
        Path tempFile = Files.createTempFile(file.getOriginalFilename(), null);
        file.transferTo(tempFile);
        Thread.ofVirtual().start(() -> producer.sendContentOf(tempFile.toFile()));
        return Optional.of(tempFile.toString());
    }
}

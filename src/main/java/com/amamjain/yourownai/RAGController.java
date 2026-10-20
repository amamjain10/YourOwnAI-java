
package com.amamjain.yourownai;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class RAGController {

    private final RAGService ragService;

    public RAGController(RAGService ragService) {
        this.ragService = ragService;
    }

    @PostMapping("/ask")
    public AskResponse ask(@RequestBody AskRequest request) {

        if (request == null
                || request.getQuestion() == null
                || request.getQuestion().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Question cannot be empty");
        }

        int topK = request.getTopK();

        if (topK < 1 || topK > 20) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "topK must be between 1 and 20");
        }

        RAGResponse ragResponse =
                ragService.answerQuestion(
                        request.getQuestion(), topK);

        List<AskResponse.SourceItem> sources =
                new ArrayList<>();

        for (RAGResponse.SourceCitation citation
                : ragResponse.getSources()) {
            sources.add(
                    new AskResponse.SourceItem(
                            citation.getId(),
                            citation.getText(),
                            citation.getDistance()));
        }

        return new AskResponse(
                ragResponse.getAnswer(), sources);
    }
}

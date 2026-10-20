
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
public class SemanticSearchController {

    private final SemanticSearchService searchService;

    public SemanticSearchController(
            SemanticSearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/search")
    public SearchResponse search(
            @RequestBody SearchRequest request) {

        if (request == null
                || request.getQuery() == null
                || request.getQuery().isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Query cannot be empty");
        }

        int topK = request.getTopK();

        if (topK < 1 || topK > 20) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "topK must be between 1 and 20");
        }

        List<SearchResult> searchResults =
                searchService.search(request.getQuery(), topK);

        List<SearchResponse.SearchItem> results =
                new ArrayList<>();

        for (SearchResult result : searchResults) {
            String id = result.getVector().getId();
            String text = searchService.getText(id);

            results.add(
                    new SearchResponse.SearchItem(
                            id,
                            text,
                            result.getDistance()));
        }

        return new SearchResponse(request.getQuery(), results);
    }
}

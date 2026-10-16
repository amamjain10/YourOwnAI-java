# Daily Development Log

## Day 1 — Project Foundation

### Completed
- Set up Java 21, Maven, and Git.
- Created the Maven project structure.
- Fixed the JUnit dependency issue.
- Implemented the immutable `Vector` class.
- Added unit tests for vector validation and defensive copying.
- Verified the project using `mvn clean test`.

### Validation
- Tests run: 6
- Failures: 0
- Errors: 0
- Build status: SUCCESS

### What I learned
- How Maven compiles Java code and executes unit tests.
- Why input validation and immutability matter in data structures.
- How defensive copying protects internal state.

### Next steps
- Configure Maven for Java 21.
- Implement vector distance and similarity metrics.
- Publish the project on GitHub.

---

## Day 2 — In-Memory Vector Store

### Completed
- Implemented the `VectorStore` class to manage vectors in memory.
- Added functionality to add, retrieve, check, and remove vectors.
- Implemented validation to prevent duplicate vector IDs.
- Added unit tests for vector storage, retrieval, removal, and insertion order.
- Verified the project using `mvn clean test`.

### Validation
- Tests run: 13 *(confirm with actual Maven output)*
- Failures: 0
- Errors: 0
- Build status: SUCCESS

### What I learned
- How to use Java collections to store and manage vectors.
- Why unique identifiers are important for vector storage.
- How unit tests verify the correctness of data structures.
- How to return immutable snapshots to protect stored data.

### Next steps
- Implement Euclidean distance and cosine similarity.
- Add unit tests for distance calculations.
- Build nearest-neighbor search.

---

## Day 3 — Vector Distance and Similarity Metrics

### Completed
- Implemented Euclidean distance to measure straight-line distance between vectors.
- Implemented Manhattan distance to calculate the sum of absolute coordinate differences.
- Implemented cosine similarity to measure the directional similarity between vectors.
- Added validation for null vectors and mismatched dimensions.
- Handled zero-vector input for cosine similarity.
- Added unit tests for vector metric calculations.
- Verified the implementation with Maven.

### Validation
- Build status: SUCCESS *(based on the previously reported successful build)*
- Test count: Not recorded here; verify with Maven output.

### What I learned
- The mathematical differences between distance and similarity.
- Why vector dimensions must match during comparisons.
- Why cosine similarity requires special handling for zero vectors.
- How mathematical functions support AI search and retrieval.

### Next steps
- Implement nearest-neighbor search.
- Rank stored vectors by their distance from a query vector.
- Return the top K matching vectors.

---

## Day 4 — Nearest-Neighbor Search

### Completed
- Created the `SearchResult` class to represent a matching vector and its distance.
- Implemented the `NearestNeighborSearch` class.
- Added top K nearest-neighbor retrieval using Euclidean distance.
- Sorted search results by ascending distance.
- Added vector ID as a secondary sorting criterion for deterministic results.
- Added tests for nearest results, top K, empty storage, invalid K, and dimension mismatch.

### Validation
- Build status: Reported complete; verify the latest test results if needed.
- Test count: Not recorded here.

### What I learned
- How brute-force nearest-neighbor search works.
- How to rank vectors using calculated distances.
- Why deterministic sorting is useful for consistent results.
- How to validate search parameters.

### Next steps
- Introduce selectable search metrics.
- Support Euclidean, Manhattan, and cosine-based search.
- Extend the nearest-neighbor search tests.

---

## Day 5 — Configurable Search Metrics

### Completed
- Created the `SearchMetric` enum.
- Added support for `EUCLIDEAN`, `MANHATTAN`, and `COSINE` search modes.
- Extended `NearestNeighborSearch` to accept a selected search metric.
- Preserved the existing two-argument search method with Euclidean distance as the default.
- Converted cosine similarity to cosine distance using `1 - cosineSimilarity`.
- Maintained ascending score sorting across all supported metrics.
- Planned additional tests for Manhattan ranking, cosine ranking, and null metric validation.

### Validation
- Build status: Confirm using `mvn clean test`.
- Test count: Not recorded here.

### What I learned
- How enums represent a fixed set of options in Java.
- How method overloading supports backward compatibility.
- Why cosine similarity must be converted to a distance for consistent ascending sorting.
- How different metrics can produce different search rankings.

### Next steps
- Create a runnable demonstration of the search engine.
- Display ranked results for all three metrics.
- Update project documentation.

---

## Day 6 — Runnable Vector Search Demo

### Completed
- Updated `App.java` to demonstrate vector storage and nearest-neighbor search.
- Added sample vectors with two-dimensional coordinates.
- Created a query vector to compare against the stored vectors.
- Demonstrated Euclidean, Manhattan, and cosine-distance search.
- Displayed the top three matching vectors and their scores.
- Fixed the search metric integration between `App.java` and `NearestNeighborSearch.java`.
- Ran the demo and observed results for all three metrics.

### Validation
- Demo status: SUCCESS — output was produced for all three metrics.
- Maven test status: Verify with `mvn clean test` if not already checked after the latest changes.
- Git status: `SearchMetric.java` was reported as modified; commit and push status should be verified.

### What I learned
- How to connect individual classes into a runnable Java application.
- How different distance metrics rank the same vectors.
- How cosine distance can identify vectors pointing in similar directions even when their magnitudes differ.
- How to troubleshoot Java method-signature and compilation errors.

### Next steps
- Introduce a service layer to coordinate vector storage and search.
- Add tests for service-level operations.
- Improve the separation of responsibilities within the project.

---

## Day 7 — Vector Search Service

### Completed
- Created `VectorSearchService` to provide a single entry point for vector operations.
- Integrated the service with the existing `VectorStore` and `NearestNeighborSearch` classes.
- Added methods to add, retrieve, remove, and count stored vectors.
- Added a service method for searching with a selected metric.
- Prepared unit tests for storage operations, search results, and duplicate vector IDs.

### Validation
- Tests run: Record the actual Maven test count.
- Failures: Record the actual result.
- Errors: Record the actual result.
- Build status: Update after running `mvn clean test`.

### What I learned
- How a service layer separates application-level operations from implementation details.
- How to reuse existing classes instead of duplicating logic.
- How to organize code into smaller, more maintainable components.
- How service-level tests verify interactions between components.

### Next steps
- Load text documents into the application.
- Split documents into smaller chunks.
- Prepare the foundation for generating text embeddings and building a retrieval-augmented generation (RAG) pipeline.

## Day 8 — Document Loading and Text Chunking

### Completed
- Implemented `TextDocumentLoader` to read local text files using UTF-8 encoding.
- Implemented `TextChunker` to split documents into fixed-size character chunks.
- Added configurable overlap between consecutive chunks.
- Added validation for chunk size and overlap.
- Prepared a sample text document to demonstrate the document-processing workflow.
- Updated `App.java` to demonstrate document loading and chunking.

### Validation
- Tests run: 31
- Failures: 0
- Errors: 0
- Skipped: 0
- Build status: SUCCESS

**Note:** These results are from the Maven test run on October 12, 2026. The output did not list dedicated `TextChunkerTest` or `TextDocumentLoaderTest` classes, so those tests and the document-processing demo still need to be verified separately.

### What I learned
- How to read local text files using Java NIO.
- Why large documents are split into smaller chunks for retrieval.
- How overlapping chunks help preserve context across chunk boundaries.
- How input validation improves the reliability of text-processing components.
- How document processing prepares text for embedding generation and semantic retrieval.

### Next Steps
- Verify document loading and chunking with a real text file.
- Add and run dedicated unit tests for both components.
- Generate embeddings for the resulting text chunks.
- Associate stored vectors with their original text to support retrieval-augmented generation (RAG).

## Day 9 — Embedding Service Foundation

### Completed
- Introduced the `EmbeddingService` interface for converting text into numerical vectors.
- Created a mock implementation for predictable unit testing.
- Added tests for valid text and invalid input.
- Established an abstraction for integrating a real local embedding model.

### Validation
- Tests run: Record the actual Maven output.
- Failures: Record the actual Maven output.
- Errors: Record the actual Maven output.
- Build status: Update after running `mvn clean test`.

### What I learned
- How text embeddings represent text numerically.
- How embeddings enable semantic search.
- Why a service interface separates model integration from application logic.
- Why mock embeddings are useful for testing but cannot represent semantic meaning.

### Next steps
- Integrate a real local embedding model through Ollama.
- Generate embeddings for text chunks.
- Store embeddings alongside their original text.
- Prepare for semantic document retrieval.

## Day 10 — Local Embedding Integration

**Date:** 13 October 2026

### Work completed ###
- Integrated the Java application with the local Ollama API using Java's `HttpClient`.
- Used the `nomic-embed-text` model to generate text embeddings.
- Used Jackson to parse the JSON response.
- Successfully generated a 768-dimensional embedding for a sample sentence.
- Verified that the Java application can communicate with the local Ollama service.

### Validation ###
- Sample input: `Java is a programming language.`
- Embedding dimensions returned: 768
- Ollama API integration: Successful

### Next steps ###
- Connect generated embeddings to the vector storage and search pipeline.
- Build the retrieval workflow needed for a basic local RAG system.

## Day 11: Semantic Search with Real Embeddings

**Date:** 14 October 2026

**Work Done:**
- Implemented `SemanticSearchService.java`.
- Integrated `EmbeddingService` with `VectorStore` and `NearestNeighborSearch`.
- Generated text embeddings using the local Ollama `nomic-embed-text` model.
- Stored text passages with their corresponding vector IDs.
- Implemented semantic search using cosine distance.
- Retrieved and displayed the top 3 relevant passages for a natural-language query.

**Testing:**
- Executed the semantic search demo successfully.
- Query: "Which language is used for machine learning?"
- Retrieved the Python and machine learning passage as the top result.
- Top result cosine distance: `0.3048`.

**Outcome:**
Successfully implemented semantic search using real text embeddings generated by Ollama. The system can now store text passages, convert queries into embeddings, and retrieve the most relevant passages based on cosine distance.

**Next Steps:**
- Integrate document loading and text chunking with semantic search.
- Implement document-based indexing and retrieval.
- Begin building the Retrieval-Augmented Generation (RAG) pipeline.

## Day 12: Document-Based Semantic Search

**Date:** 14 October 2026

**Work Done:**
- Integrated `TextDocumentLoader` with the semantic search pipeline.
- Loaded document content from the existing `sample.txt` file.
- Split the document into 6 overlapping text chunks using `TextChunker`.
- Generated embeddings using the local Ollama `nomic-embed-text` model.
- Indexed the chunks using `SemanticSearchService`.
- Retrieved and displayed the top 3 results for a natural-language query.

**Testing:**
- Successfully loaded a document containing 1,314 characters.
- Confirmed that all 6 chunks were indexed.
- Tested semantic retrieval using the query: "How does RAG use retrieved documents to answer questions?"
- The top result was the RAG explanation, with a cosine distance of `0.2818`.

**Outcome:**
Successfully implemented document-based semantic search using local embeddings. The application can load a text document, split it into chunks, index the chunks, and retrieve relevant passages for a natural-language query.

**Next Steps:**
- Improve text chunking to avoid splitting words and sentences unnecessarily.
- Explore persistent storage for embeddings.
- Integrate retrieved context with a language model to develop a complete RAG pipeline.
## Day 13: Improve Text Chunking

**Date:** 14 October 2026

**Work Done:**
- Improved `TextChunker.java` to prefer sentence boundaries when splitting text.
- Added word-boundary handling to reduce incomplete words in chunks.
- Preserved configurable chunk size and overlap.
- Re-ran document-based semantic search using the existing `sample.txt`.
- Verified that the RAG explanation is retrieved as the top result.

**Testing:**
- Loaded a document containing 1,314 characters.
- Indexed 6 text chunks using local Ollama embeddings.
- Tested the query: "How does RAG use retrieved documents to answer questions?"
- Top result cosine distance: `0.2126`.

**Outcome:**
Improved text chunking and confirmed that document-based semantic search continues to work with the updated chunker.

**Next Steps:**
- Strengthen chunker tests for word boundaries, overlap, and long documents.
- Implement persistent storage for vectors and document chunks.
- Continue developing the local RAG pipeline.

## Day 14: Persistent Vector Storage

**Date:** 14 October 2026

**Work Done:**
- Implemented JSON-based persistence in `SemanticSearchService.java`.
- Added functionality to save text passages and their embeddings to disk.
- Added functionality to restore indexed vectors and original text from a saved JSON file.
- Stored the index in `data/semantic-index.json`.
- Updated the application to load saved data before performing semantic search.

**Testing:**
- Successfully created and saved the semantic index.
- Restored 6 indexed passages from disk.
- Verified that semantic search returned relevant results after loading the index.

**Outcome:**
Implemented basic persistent storage for the semantic search pipeline, reducing the need to regenerate document embeddings after every application restart.

**Next Steps:**
- Add automated persistence tests.
- Improve index rebuilding and update handling.
- Integrate retrieved document context with a local language model to build the RAG pipeline.

## Day 15: Persistence Testing and Index Reliability

**Date:** 15 October 2026

**Work Done:**
- Added automated tests for `SemanticSearchService` persistence.
- Tested saving and restoring document text and embeddings.
- Verified that restored documents can still be searched.
- Confirmed that loading a saved index does not regenerate document embeddings.
- Added tests for duplicate document IDs and blank text validation.

**Testing:**
- Executed the Maven test suite.
- Verified persistence using a temporary JSON index.
- Tested semantic retrieval after restoring stored data.

**Outcome:**
Improved confidence in the persistence layer through automated tests and validation of restored search functionality.

**Next Steps:** 
- Improve safe index updates and document re-indexing.
- Integrate retrieved context with a local language model.
- Develop the question-answering stage of the RAG pipeline.

## Day 16 — RAG Answer Generation and Testing (15 October 2026)

### Work Completed
- Integrated the local Qwen 2.5 3B model for answer generation using Ollama.
- Implemented `OllamaGenerationService` to send prompts to the local generation API.
- Introduced `TextGenerationService` to make answer generation replaceable and testable.
- Implemented `RAGService` to retrieve relevant document passages and generate context-aware answers.
- Updated `App.java` to support interactive question-and-answer sessions.
- Added four unit tests covering answer generation, blank questions, invalid `topK`, and empty search results.

### Validation
- Verified relevant questions produce useful answers.
- Tested an unsupported question about space travel; the model correctly indicated that the retrieved context lacked relevant information.
- Ran `mvn clean test` successfully.
- **Total tests: 45; Failures: 0; Errors: 0.**

### Outcome
The project now supports a complete local Retrieval-Augmented Generation workflow using semantic search, persistent embeddings, and local language-model generation.

### Next Steps
- Improve retrieval quality and document chunking.
- Add source references to generated answers.
- Explore answer quality evaluation and additional RAG tests.

## Day 17 — RAG Source Citations (16 October 2026)

### Work Completed
- Created `RAGResponse.java` to represent a generated answer and its source citations.
- Updated `RAGService.java` to return the answer together with the retrieved source passages.
- Added source metadata including chunk ID, original passage text, and distance score.
- Updated `App.java` to display generated answers and their retrieved sources.
- Updated RAG unit tests for the new response structure.

### Validation
- Verified that the application generates an answer using retrieved context.
- Confirmed that three retrieved passages were displayed with their IDs and distance scores.
- Confirmed that the retrieved passages were ordered by ascending distance.
- Existing test suite: 45 tests passed, with zero failures and errors after the response-model change.

### Outcome
The local RAG application now provides source transparency by showing which document passages were retrieved for a question.

### Next Steps
- Improve chunking and retrieval quality.
- Add tests for source citations and response validation.
- Explore more advanced vector indexing and benchmarking.
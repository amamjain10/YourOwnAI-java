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
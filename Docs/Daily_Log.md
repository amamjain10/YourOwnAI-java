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

## Day 2 — In-Memory Vector Store

### Completed
- Implemented the `VectorStore` class to manage vectors in memory.
- Added functionality to add, retrieve, check, and remove vectors.
- Implemented validation to prevent duplicate vector IDs.
- Added unit tests for vector storage, retrieval, removal, and insertion order.
- Verified the project using `mvn clean test`.

### Validation
- Tests run: 13 *(confirm with your actual Maven output)*
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

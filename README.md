# YourOwnAI-Java

A Java-based vector database and Retrieval-Augmented Generation (RAG) engine built from scratch to explore vector search, semantic retrieval, and local AI integration.

## Project Goals

- Build a vector data model with input validation and immutability.
- Implement distance metrics and similarity calculations.
- Develop brute-force and approximate nearest-neighbor search.
- Integrate local embeddings and language models using Ollama.
- Build a Retrieval-Augmented Generation (RAG) pipeline.
- Benchmark search performance and retrieval quality.

## Technology Stack

- Java 21
- Maven
- JUnit 4
- Git and GitHub
- Ollama (planned)

## Project Progress

- [x] Initialize the Maven project.
- [x] Implement the immutable `Vector` model.
- [x] Add vector validation and immutability tests.
- [x] Implement the in-memory `VectorStore`.
- [x] Add unit tests for `VectorStore`.
- [x] Implement Euclidean distance.
- [x] Implement Manhattan distance.
- [x] Implement cosine similarity.
- [x] Add unit tests for vector distance metrics.
- [ ] Implement brute-force nearest-neighbor search.
- [ ] Support top-K search results.
- [ ] Implement approximate nearest-neighbor search.
- [ ] Integrate local embeddings using Ollama.
- [ ] Build the RAG pipeline.
- [ ] Benchmark search performance and retrieval quality.

> **Note:** Mark the nearest-neighbor tasks as completed after the implementation and tests pass.

## Running Tests

Run the following command from the project root:

```bash
mvn clean test
```

## Project Structure

```text
yourownai-java/
├── src/
│   ├── main/
│   │   └── java/com/amamjain/yourownai/
│   │       ├── App.java
│   │       ├── Vector.java
│   │       ├── VectorStore.java
│   │       └── VectorMetrics.java
│   └── test/
│       └── java/com/amamjain/yourownai/
│           ├── AppTest.java
│           ├── VectorTest.java
│           ├── VectorStoreTest.java
│           └── VectorMetricsTest.java
├── Docs/
│   └── Daily_Log.md
├── .gitignore
├── pom.xml
└── README.md
```

> The project structure will expand as new search and RAG components are implemented.

## Inspiration

Inspired by the concept of building a vector database and AI retrieval system. This repository is an independent Java implementation developed for learning and experimentation.

## Development Approach

This project is being developed incrementally, with a focus on understanding core concepts, writing unit tests, maintaining clean code, and documenting progress through regular Git commits.

## License

License details will be added when a license is selected.
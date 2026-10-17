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

- [x] Implement Euclidean distance.
- [x] Implement Manhattan distance.
- [x] Implement cosine similarity.
- [x] Build brute-force nearest-neighbor search.
- [x] Support top-K search results.
- [x] Support selectable search metrics.
- [ ] Implement approximate nearest-neighbor search.
- [ ] Integrate local embeddings using Ollama.
- [ ] Build the RAG pipeline.

> **Note:** Mark the nearest-neighbor tasks as completed after the implementation and tests pass.

## Running Tests


## Running the Demo

Run the unit tests:

```bash
mvn clean test
```

Build the project and run the vector search demo:

```bash
mvn -q package
java -cp target/classes com.amamjain.yourownai.App
```

The demo creates sample vectors and performs top-K searches
using Euclidean distance, Manhattan distance, and cosine similarity.


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
# YourOwnAI-Java

A Java-based vector database and Retrieval-Augmented Generation (RAG) engine built from scratch to explore vector search, semantic retrieval, and local AI integration.

## Project Goals

- Build a vector data model with input validation.
- Implement similarity and distance metrics.
- Develop exact and approximate nearest-neighbor search.
- Integrate local embeddings and language models using Ollama.
- Benchmark search performance and retrieval quality.

## Technology Stack

- Java 21
- Maven
- JUnit 4
- Git and GitHub
- Ollama (planned)

## Current Progress

## Project Progress


- [x] Initialize the Maven project.
- [x] Implement the immutable Vector model.
- [x] Add vector validation and immutability tests.
- [x] Implement the in-memory VectorStore.
- [x] Add unit tests for VectorStore.
- [x] Implement Euclidean distance.
- [x] Implement cosine similarity.
- [ ] Build nearest-neighbor search.
- [ ] Integrate local embeddings and RAG.


## Running Tests

Run the following command from the project root:

```bash
mvn test
```

## Project Structure

```text
src/
├── main/java/com/amamjain/yourownai/
│   ├── App.java
│   └── Vector.java
└── test/java/com/amamjain/yourownai/
    ├── AppTest.java
    └── VectorTest.java
```

## Inspiration

Inspired by the concept of building a vector database and AI retrieval system. This repository is an independent Java implementation developed for learning and experimentation.
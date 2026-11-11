# YourOwnAI-Java — Manufacturing SOP Copilot

A Java-based Retrieval-Augmented Generation (RAG) application that helps manufacturing supervisors and maintenance personnel find answers from machine maintenance, quality control, and safety SOP documents.

Built as an extension of my existing Java vector database and RAG engine for the BiztelAI manufacturing SOP copilot assignment.

## Problem Statement

Manufacturing supervisors often need to consult lengthy SOP documents to identify the correct machine procedure, understand alarm codes, and make decisions during production downtime.

This project provides a conversational interface that retrieves relevant information from provided PDF documents and generates answers using a locally running language model.

The application is designed to provide concise, source-grounded answers while avoiding unsupported recommendations.

## Features

- **PDF document ingestion:** Extracts text from SOP PDF documents.
- **Text chunking:** Splits documents into smaller overlapping chunks.
- **Semantic search:** Uses vector embeddings to retrieve relevant passages.
- **RAG-based question answering:** Generates answers using retrieved document context.
- **Source references:** Displays document names and page references alongside answers.
- **Unknown-answer handling:** Avoids inventing information when the provided documents do not contain an answer.
- **Local AI integration:** Uses Ollama for embeddings and language generation.
- **Web interface:** Provides a simple chat interface for asking SOP-related questions.

## Technology Stack

| Component | Technology |
|---|---|
| Language | Java 21 |
| Backend | Spring Boot |
| Build tool | Maven |
| Vector search | Custom Java vector database |
| Embeddings | Ollama — nomic-embed-text |
| Language model | Ollama — qwen2.5:3b |
| PDF processing | Apache PDFBox |
| Testing | JUnit |
| Frontend | HTML, CSS, JavaScript |
| Version control | Git and GitHub |

**Implementation note:** The assignment suggested FastAPI. I extended my existing Java/Spring Boot RAG project instead, reusing its vector database, search implementation, and retrieval pipeline.

## Architecture

1. **PDF ingestion:** Loads the provided SOP documents.
2. **Text extraction:** Extracts page-level text using PDFBox.
3. **Chunking:** Splits extracted text into overlapping chunks.
4. **Embedding generation:** Converts chunks into vectors using Ollama.
5. **Vector storage:** Stores embeddings and document metadata.
6. **Semantic retrieval:** Retrieves relevant chunks for a user question.
7. **Answer generation:** Passes retrieved context to the local language model.
8. **Source display:** Returns document and page information with the answer.

## Documents Used

The application uses the following manufacturing SOP documents:

- `MNT-MAN-CNC-02_TX-200_Preventive_Maintenance.pdf`
- `QC-SOP-014_Rev03_Incoming_Material_Inspection.pdf`
- `QC-SOP-014_Rev04_Incoming_Material_Inspection.pdf`
- `SAF-SOP-003_Lockout_Tagout_Procedure.pdf`

Place these PDF files inside `data/documents/` before starting the application.

## Setup and Installation

### Prerequisites

- Java 21
- Maven
- Ollama
- Git

### 1. Clone the repository

```bash
git clone https://github.com/amamjain10/YourOwnAI-java.git
cd YourOwnAI-java
```

### 2. Download the required Ollama models

```bash
ollama pull nomic-embed-text
ollama pull qwen2.5:3b
```

Make sure the Ollama service is running before starting the application.

### 3. Add SOP documents

Create the following directory if it does not exist:

```text
data/documents/
```

Place the four provided PDF documents inside this directory.

### 4. Run the application

```bash
mvn spring-boot:run
```

On the first startup, the application ingests the PDF documents, generates embeddings, and saves the semantic index.

### 5. Open the application

Open the following URL in your browser:

```text
http://localhost:8080
```

## Sample Questions and Results

The following five questions were written and tested as additional evaluation questions.

| Question | Expected answer / observed result |
|---|---|
| What precautions must be taken before replacing a hydraulic hose on a TX-200 CNC machine? | Isolate the machine under SAF-SOP-003, release stored hydraulic pressure, confirm the gauge reads zero, use a drip tray, replace with a hose of the same rating, and verify pressure returns to 3.5–4.0 MPa with no leaks. |
| What should happen if a supplier delivers material without an MTC? | Place the lot on HOLD. It must not be inspected or accepted until the MTC is received. |
| Where should rejected incoming material be stored, and who must notify the SQE? | Store it in Quarantine Cage Q1, Stores Bay 3. The Shift QC Supervisor must inform the SQE within four hours of rejection. |
| How frequently must authorized personnel undergo LOTO refresher training? | Every 12 months. Authorized personnel must complete LOTO training before applying locks. |
| What is the recommended spindle operating temperature for the TX-200 CNC machine? | The provided context does not contain this information. |

All five additional questions were tested successfully in the application.

The five official sample questions supplied with the assignment were also tested successfully.

## Running Tests

Run the complete test suite:

```bash
mvn clean test
```

Latest recorded test result:

- Tests run: 77
- Failures: 0
- Errors: 0
- Skipped: 0
- Build status: SUCCESS

## Project Structure

```text
yourownai-java/
├── src/
│   ├── main/
│   │   ├── java/com/amamjain/yourownai/
│   │   │   ├── Vector.java
│   │   │   ├── VectorStore.java
│   │   │   ├── VectorMetrics.java
│   │   │   ├── NearestNeighborSearch.java
│   │   │   ├── SemanticSearchService.java
│   │   │   ├── DocumentIngestionService.java
│   │   │   ├── PdfDocumentLoader.java
│   │   │   ├── TextChunker.java
│   │   │   ├── RAGService.java
│   │   │   └── ...
│   │   └── resources/
│   │       └── static/
│   └── test/
├── frontend/
├── data/
│   └── documents/
├── Docs/
│   └── Daily_Log.md
├── pom.xml
├── README.md
└── THINKING.md
```

## Limitations

- Answers depend on the information available in the supplied SOP documents.
- The application does not replace authorized maintenance or safety personnel.
- It requires a running local Ollama service.
- The implementation uses Spring Boot rather than the FastAPI framework suggested in the assignment.
- The application is a prototype and has not been validated for use in a live manufacturing environment.

## Future Improvements

- Improve retrieval accuracy for conflicting or overlapping SOP content.
- Add machine and alarm-code selection to simplify mobile usage.
- Improve document revision and effective-date handling.
- Add evaluation metrics for retrieval quality and answer accuracy.
- Explore deployment for more reliable shop-floor access.

## Original Project Background

YourOwnAI-Java began as an independent project to understand vector databases, semantic search, and Retrieval-Augmented Generation from first principles.

The original implementation included:

- Immutable vector models and validation.
- Euclidean, Manhattan, and cosine similarity metrics.
- In-memory vector storage.
- Brute-force nearest-neighbor search.
- Unit testing and performance evaluation.

The manufacturing SOP copilot extends this foundation into a practical document-question-answering application.

## Author

**Amam Jain**

GitHub: https://github.com/amamjain10

Project: https://github.com/amamjain10/YourOwnAI-java

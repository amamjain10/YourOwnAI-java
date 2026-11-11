
const form = document.getElementById("question-form");
const questionInput = document.getElementById("question");
const chat = document.getElementById("chat");
const statusText = document.getElementById("status");
const sendButton = document.getElementById("send-button");

const fileInput = document.getElementById("file-input");
const dropZone = document.getElementById("drop-zone");
const uploadStatus = document.getElementById("upload-status");

function addMessage(question, type) {
    const message = document.createElement("div");
    message.className = `message ${type}-message`;

    const avatar = document.createElement("div");
    avatar.className = `avatar ${type === "assistant" ? "ai-avatar" : "user-avatar"}`;
    avatar.textContent = type === "assistant" ? "✳" : "You";

    const content = document.createElement("div");
    content.className = "message-content";

    const title = document.createElement("strong");
    title.textContent = type === "assistant" ? "Your AI assistant" : "You";

    const paragraph = document.createElement("p");
    paragraph.textContent = question;

    content.append(title, paragraph);
    message.append(avatar, content);
    chat.appendChild(message);

    chat.scrollTop = chat.scrollHeight;

    return { message, content, paragraph };
}

function addSources(parent, sources) {
    if (!Array.isArray(sources) || sources.length === 0) {
        return;
    }

    const details = document.createElement("details");
    details.className = "sources";

    const summary = document.createElement("summary");
    summary.textContent = `📚 Sources (${sources.length})`;
    details.appendChild(summary);

    sources.forEach((source, index) => {
        const item = document.createElement("div");
        item.className = "source-item";

        const title = document.createElement("strong");
        title.textContent = `${index + 1}. ${source.id || "Source"}`;

        const text = document.createElement("p");
        text.textContent = source.text || "No source text available.";

        const score = document.createElement("small");
        const distance = Number(source.score);

        score.textContent = Number.isFinite(distance)
            ? `Distance: ${distance.toFixed(4)}`
            : "";

        item.append(title, text, score);
        details.appendChild(item);
    });

    parent.appendChild(details);
}

async function askQuestion(question) {
    const cleanedQuestion = question.trim();

    if (!cleanedQuestion) {
        statusText.textContent = "Please enter a question.";
        return;
    }

    addMessage(cleanedQuestion, "user");
    questionInput.value = "";

    sendButton.disabled = true;
    statusText.textContent = "Searching documents and generating an answer...";

    const loading = addMessage("Thinking...", "assistant");

    try {
        const response = await fetch("/api/ask", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                question: cleanedQuestion,
                topK: 3
            })
        });

        const data = await response.json().catch(() => ({}));

        if (!response.ok) {
            throw new Error(
                data.detail || data.message || `Request failed (${response.status})`
            );
        }

        loading.paragraph.textContent =
            data.answer || "No answer was returned.";

        addSources(loading.content, data.sources);

        statusText.textContent = "Answer received.";
    } catch (error) {
        loading.paragraph.textContent =
            `Unable to get an answer: ${error.message}`;

        statusText.textContent =
            "Check that Spring Boot and Ollama are running.";
    } finally {
        sendButton.disabled = false;
        questionInput.focus();
        chat.scrollTop = chat.scrollHeight;
    }
}

// Submit questions to the existing RAG endpoint.
form.addEventListener("submit", (event) => {
    event.preventDefault();
    askQuestion(questionInput.value);
});

// Make example-question buttons work.
document.querySelectorAll("[data-question]").forEach((button) => {
    button.addEventListener("click", () => {
        askQuestion(button.dataset.question || "");
    });
});

// Navigate between dashboard sections.
document.querySelectorAll("[data-page]").forEach((button) => {
    button.addEventListener("click", () => {
        const page = button.dataset.page;

        document.querySelectorAll(".page-view").forEach((section) => {
            section.classList.toggle("hidden", section.id !== `${page}-page`);
        });

        document.querySelectorAll("[data-page]").forEach((navButton) => {
            navButton.classList.toggle(
                "active",
                navButton.dataset.page === page
            );
        });
    });
});

// Open the file chooser from any Add Document button.
// Actual upload will be implemented with the backend.
[
    "browse-button",
    "documents-upload-button",
    "empty-browse-button",
    "right-upload-button"
].forEach((id) => {
    document.getElementById(id)?.addEventListener("click", () => {
        fileInput.click();
    });
});

fileInput.addEventListener("change", () => {
    const file = fileInput.files?.[0];

    if (!file) {
        return;
    }

    const extension = file.name.split(".").pop().toLowerCase();

    if (!["pdf", "txt"].includes(extension)) {
        uploadStatus.textContent = "Please select a PDF or TXT file.";
        fileInput.value = "";
        return;
    }

    uploadStatus.textContent =
        `${file.name} selected. Upload and indexing will be enabled in the next step.`;
});

// Basic drag-and-drop interaction for the upload area.
["dragenter", "dragover"].forEach((eventName) => {
    dropZone.addEventListener(eventName, (event) => {
        event.preventDefault();
        dropZone.classList.add("drag-over");
    });
});

["dragleave", "drop"].forEach((eventName) => {
    dropZone.addEventListener(eventName, (event) => {
        event.preventDefault();
        dropZone.classList.remove("drag-over");
    });
});

dropZone.addEventListener("drop", (event) => {
    const file = event.dataTransfer?.files?.[0];

    if (!file) {
        return;
    }

    const extension = file.name.split(".").pop().toLowerCase();

    if (!["pdf", "txt"].includes(extension)) {
        uploadStatus.textContent = "Please drop a PDF or TXT file.";
        return;
    }

    uploadStatus.textContent =
        `${file.name} selected. Upload and indexing will be enabled in the next step.`;
});

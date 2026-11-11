
const form = document.getElementById("question-form");
const questionInput = document.getElementById("question");
const chat = document.getElementById("chat");
const statusText = document.getElementById("status");
const sendButton = document.getElementById("send-button");

function addMessage(content, className) {
    const message = document.createElement("div");
    message.className = `message ${className}`;
    message.textContent = content;
    chat.appendChild(message);
    return message;
}

function addSources(parent, sources) {
    if (!sources || sources.length === 0) {
        return;
    }

    const section = document.createElement("section");
    section.style.marginTop = "16px";

    const heading = document.createElement("h3");
    heading.textContent = "Sources";
    heading.style.fontSize = "15px";
    heading.style.marginBottom = "10px";
    section.appendChild(heading);

    sources.forEach((source, index) => {
        const card = document.createElement("div");
        card.style.padding = "12px";
        card.style.marginTop = "8px";
        card.style.border = "1px solid #35425b";
        card.style.borderRadius = "10px";
        card.style.background = "#0d1526";

        const title = document.createElement("strong");
        title.textContent = `${index + 1}. ${source.id}`;
        card.appendChild(title);

        const text = document.createElement("p");
        text.textContent = source.text;
        text.style.marginTop = "8px";
        text.style.lineHeight = "1.6";
        card.appendChild(text);

        const score = document.createElement("p");
        score.textContent = `Distance: ${Number(source.score).toFixed(4)}`;
        score.style.marginTop = "8px";
        score.style.color = "#9caac2";
        score.style.fontSize = "12px";
        card.appendChild(score);

        section.appendChild(card);
    });

    parent.appendChild(section);
}

form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const question = questionInput.value.trim();

    if (!question) {
        statusText.textContent = "Please enter a question.";
        return;
    }

    addMessage(question, "user");

    questionInput.value = "";
    sendButton.disabled = true;
    statusText.textContent = "Searching documents and generating an answer...";

    const loadingMessage = addMessage("Thinking...", "assistant");

    try {
        const response = await fetch("/api/ask", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                question: question,
                topK: 3
            })
        });

        const data = await response.json();

        if (!response.ok) {
            throw new Error(data.detail || data.message || "Request failed.");
        }

        loadingMessage.textContent = data.answer || "No answer was returned.";
        addSources(loadingMessage, data.sources);

        statusText.textContent = "Answer received.";
    } catch (error) {
        loadingMessage.textContent =
            `Unable to get an answer: ${error.message}`;

        statusText.textContent =
            "Check that the backend is running and try again.";
    } finally {
        sendButton.disabled = false;
        questionInput.focus();
    }
});

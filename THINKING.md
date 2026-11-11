# THINKING.md
# Manufacturing SOP Copilot — Product Thinking

## 1. Understand the User

The primary user of this application is a manufacturing supervisor working on a production floor.

A supervisor may need to make quick decisions when a machine displays an alarm, production stops, or a maintenance issue occurs. In these situations, searching through lengthy PDF documents is inconvenient and can delay the next action.

The supervisor needs an assistant that:

- Provides quick and understandable answers.
- Identifies the relevant SOP document and page.
- Gives clear instructions based on the available documentation.
- Distinguishes between operator responsibilities and maintenance responsibilities.
- Does not invent answers when information is unavailable.
- Prioritizes safety over providing an answer quickly.

The application is intended to support the supervisor's decisions, not replace trained personnel or official safety procedures.

## 2. First-Draft User Journey

I wrote this initial journey before asking AI to critique it.

### Scenario 1: Machine alarm

A supervisor is on the production floor when an operator reports:

"Alarm on CNC-06."

The supervisor reads the alarm code and asks the copilot.

For example:

"CNC-06, AL-305, mid-cycle."

The copilot should understand that CNC-06 is covered by the TX-200 maintenance manual, which applies to CNC-04 through CNC-09.

It should identify the relevant document, MNT-MAN-CNC-02, and provide the documented action:

- Stop the cycle.
- Check tool wear and cutting parameters.
- Inform the shift supervisor.

It should not suggest opening the electrical cabinet or spindle, or provide instructions that are not in the manual.

### Scenario 2: Common operator alarms

For AL-104, the copilot should explain that the way-lubrication oil level is low.

It should instruct the operator to:

- Top up ISO VG 68 way-lubrication oil to MAX.
- Press RESET.
- Explain that the machine can continue after reset.

For AL-412, it should explain that the coolant level is low and that coolant should be mixed to a concentration of 6–8%, rather than using plain water.

For AL-210, it should tell the operator:

- Do not start the cycle.
- Check chuck pressure and part seating.
- Contact maintenance if the alarm repeats.

For AL-101, it should explain:

- Stop the cycle.
- Check oil level and pressure gauge.
- Contact maintenance if pressure remains below 3.5 MPa.

### Scenario 3: Maintenance and safety

If the issue involves replacing a leaking hydraulic hose, the copilot should retrieve the Lockout/Tagout procedure, SAF-SOP-003.

It should explain that the machine must be isolated, stored hydraulic pressure released, and the pressure gauge confirmed at zero before loosening a fitting.

This work must be performed by trained, authorized personnel following the LOTO procedure. An operator should not independently perform maintenance work.

### What makes the assistant trustworthy?

I believe the copilot should:

- Show the document name, revision, and effective date.
- Clearly distinguish operator actions from maintenance actions.
- Avoid confusing ISO VG 68 way-lubrication oil with ISO VG 46 hydraulic oil.
- Provide specific thresholds instead of vague instructions.
- Never suggest shortcuts around lockout procedures.
- Give short answers because production downtime matters.
- Admit when the provided documents do not contain an answer.

My expectation is that the copilot should behave like a careful senior technician: helpful, quick, safety-conscious, and honest about its limitations.

## 3. AI Challenge

After writing my first user journey, I asked AI to challenge it from the perspective of a skeptical product manager and a manufacturing supervisor.

Some of the important challenges raised were:

### Challenge 1: Mobile usability

A supervisor may be wearing gloves or standing near a noisy machine. Typing a complete question may not be convenient.

AI suggested considering machine selection and alarm-code selection instead of requiring users to type every detail.

### Challenge 2: Safety and unsupported answers

AI questioned whether the application could accidentally turn incomplete information into unsafe instructions.

For example, a generated answer should clearly distinguish documented SOP instructions from assumptions or information that the documents do not provide.

### Challenge 3: Incomplete questions

A supervisor might simply type:

"It's tripping again."

The application should not guess the alarm code or machine condition. It should ask for clarification when important information is missing.

### Challenge 4: Source reliability

Displaying a document name and page number is useful, but it does not automatically prove that the document is the correct controlled version.

AI challenged me to consider document revision and effective-date information when evaluating source reliability.

### Challenge 5: Network reliability

The supervisor may access the application from a phone over the factory's local Wi-Fi network.

AI raised questions about what should happen if the network connection is interrupted and how the application should communicate connection failures.

### My Reflection on the Critique

I agreed with most of the challenges raised by AI.

The mobile usability concern is important because the application is intended for a production-floor environment. A simpler interaction could make it easier for supervisors to use.

I also agree that the application should not guess when a question is incomplete and should clearly communicate when the available documents do not contain an answer.

Source reliability is another important concern. Showing the source document and page helps users verify the information themselves.

However, I disagreed with prioritizing offline caching for this assignment. The current prototype is intended to run on a laptop and be accessed by another device over the same local Wi-Fi network. I did not consider offline caching necessary for the current scope, although I would reconsider it in a real manufacturing deployment.

## 4. Improvement Selected and Why

Based on the critique, I selected the following improvement:

**Short, safety-first, step-by-step answers with clear operator and maintenance boundaries.**

I chose this because a supervisor working during production downtime should not have to read long paragraphs to identify the next action.

The application should prioritize:

1. The immediate action that the SOP specifies.
2. The safety precautions relevant to that action.
3. Whether the action is intended for an operator or authorized maintenance personnel.
4. The supporting source document and page.
5. An explicit statement when the information is not available.

For example, an AL-104 answer should directly explain that the way-lubrication oil level is low, instruct the operator to top up ISO VG 68 to MAX and press RESET, and avoid unnecessary maintenance instructions.

For an unknown question, such as the recommended spindle operating temperature, the application should state that the provided documents do not specify the value instead of generating an unsupported answer.

This improvement focuses on making the application more useful during real shop-floor situations while keeping safety and source-grounding central to its behaviour.

## 5. Final Reflection

Building this manufacturing SOP copilot helped me understand how a RAG application can be applied to a practical industrial problem.

I started with my existing Java vector database and RAG project, which already included vector storage, similarity metrics, and nearest-neighbour search. I extended it to support PDF ingestion, text extraction, chunking, embedding generation, semantic retrieval, and source metadata.

One of the biggest technical challenges was connecting the PDF documents to the existing retrieval pipeline while preserving document and page information. I addressed this by implementing PDF loading, text chunking, document ingestion, and metadata persistence so that retrieved passages could be linked back to their original sources.

I also integrated local Ollama models for embedding generation and answer generation. This allowed the application to work without relying on a hosted language model API.

During testing, I evaluated the application using the five official sample questions and five additional questions that I created. These included maintenance procedures, missing supplier documentation, rejected material handling, LOTO training frequency, and a question whose answer was not present in the documents.

The unknown-question test was particularly important because a manufacturing assistant should not invent technical values or procedures.

I also learned that a technically working RAG pipeline is not enough. The way information is presented matters just as much. A correct but lengthy answer may not be useful to someone trying to resolve a production issue quickly.

If I continue developing this application, I would focus on improving mobile usability, handling incomplete questions, strengthening document revision management, and evaluating retrieval quality with a larger set of questions.

I also recognize that this is a prototype. It has not been validated for use in a live manufacturing environment and should not replace official SOPs, trained personnel, or safety procedures.

Overall, this assignment helped me think beyond implementing an AI feature. It taught me to start with the user's actual problem, challenge my assumptions, evaluate the application using realistic scenarios, and prioritize trust and safety over simply generating answers.


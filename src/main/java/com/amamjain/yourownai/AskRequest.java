
package com.amamjain.yourownai;

public class AskRequest {

    private String question;
    private int topK = 3;

    public AskRequest() {
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getTopK() {
        return topK;
    }

    public void setTopK(int topK) {
        this.topK = topK;
    }
}

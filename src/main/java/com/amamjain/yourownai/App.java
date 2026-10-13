package com.amamjain.yourownai;

public class App {

    public static void main(String[] args) {
        EmbeddingService service = new OllamaEmbeddingService();

        String text = "Java is a programming language.";
        double[] embedding = service.embed(text);

        System.out.println("Input: " + text);
        System.out.println("Embedding dimensions: " + embedding.length);

        System.out.print("First five values: ");
        for (int i = 0; i < Math.min(5, embedding.length); i++) {
            System.out.printf("%.6f ", embedding[i]);
        }

        System.out.println();
    }
}
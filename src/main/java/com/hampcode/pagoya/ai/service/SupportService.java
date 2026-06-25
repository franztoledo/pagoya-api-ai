package com.hampcode.pagoya.ai.service;

import com.hampcode.pagoya.ai.dto.SupportAnswer;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Soporte con RAG (Retrieval-Augmented Generation):
 * - ingest(): lee los PDF de docs/, los trocea y los guarda como vectores en pgvector.
 * - ask():    busca los fragmentos mas parecidos a la pregunta y responde SOLO con ellos,
 *             devolviendo un DTO estructurado (structured output) listo para el frontend.
 */
@Service
@RequiredArgsConstructor
public class SupportService {

    private final VectorStore vectorStore;
    private final ChatClient supportChatClient;

    /** Salida del LLM: solo decide el texto y si pudo responder con el contexto. */
    private record SupportReply(String reply, boolean answeredFromDocs) {}

    public SupportAnswer ask(String question) {
        List<Document> docs = vectorStore.similaritySearch(
            SearchRequest.builder().query(question).topK(4).build());

        String context = docs.stream()
            .map(Document::getText)
            .collect(Collectors.joining(System.lineSeparator() + System.lineSeparator()));

        SupportReply reply = supportChatClient.prompt()
            .user("Contexto:" + System.lineSeparator() + context
                + System.lineSeparator() + System.lineSeparator() + "Pregunta: " + question)
            .call()
            .entity(SupportReply.class);

        List<String> sources = docs.stream()
            .map(doc -> doc.getMetadata().get("file_name"))
            .filter(Objects::nonNull)
            .map(Object::toString)
            .distinct()
            .toList();

        return new SupportAnswer(reply.reply(), reply.answeredFromDocs(), sources);
    }

    public int ingest() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] files = resolver.getResources("classpath:docs/*.pdf");

        List<Document> docs = new ArrayList<>();
        for (Resource file : files) {
            docs.addAll(new PagePdfDocumentReader(file).get());
        }

        List<Document> chunks = TokenTextSplitter.builder().build().apply(docs);
        vectorStore.add(chunks);
        return chunks.size();
    }
}

package com.hampcode.pagoya.ai.controller;

import com.hampcode.pagoya.ai.dto.ChatRequest;
import com.hampcode.pagoya.ai.dto.ChatResponse;
import com.hampcode.pagoya.ai.dto.ReportResponse;
import com.hampcode.pagoya.ai.dto.SupportAnswer;
import com.hampcode.pagoya.ai.service.AiService;
import com.hampcode.pagoya.ai.service.SupportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
@Tag(name = "AI", description = "Asistente con IA (tool calling y structured output)")
public class AiController {

    private final AiService aiService;
    private final SupportService supportService;

    @Operation(summary = "Cliente: realiza una transferencia desde texto (tool calling)")
    @PostMapping("/transfer")
    public ChatResponse transfer(@RequestBody ChatRequest request) {
        return new ChatResponse(aiService.transfer(request.message()));
    }

    @Operation(summary = "Admin: reporte de transferencias por moneda con resumen de IA")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/report")
    public ReportResponse report() {
        return aiService.report();
    }

    @Operation(summary = "Cliente: pregunta de soporte respondida con los documentos (RAG)")
    @PostMapping("/support/ask")
    public SupportAnswer supportAsk(@RequestBody ChatRequest request) {
        return supportService.ask(request.message());
    }

    @Operation(summary = "Admin: ingesta los documentos (PDF) al vector store")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/support/ingest")
    public Map<String, Object> supportIngest() throws IOException {
        return Map.of("chunksIngested", supportService.ingest());
    }
}

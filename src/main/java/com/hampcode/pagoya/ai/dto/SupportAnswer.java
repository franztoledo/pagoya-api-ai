package com.hampcode.pagoya.ai.dto;

import java.util.List;

/**
 * Respuesta de soporte (RAG) lista para el frontend.
 * - reply:           texto de la respuesta.
 * - answeredFromDocs: true si se respondio con los documentos; false si no habia info.
 * - sources:         documentos (PDF) de los que salio el contexto.
 */
public record SupportAnswer(
    String reply,
    boolean answeredFromDocs,
    List<String> sources
) {}

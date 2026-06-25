package com.hampcode.pagoya.ai.dto;

/**
 * Lo que la IA devuelve como STRUCTURED OUTPUT al analizar el reporte:
 * un resumen y una recomendacion, ambos en texto pero con forma garantizada.
 */
public record ReportInsight(String resumen, String recomendacion) {}

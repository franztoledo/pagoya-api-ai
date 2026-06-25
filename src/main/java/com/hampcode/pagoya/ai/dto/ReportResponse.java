package com.hampcode.pagoya.ai.dto;

import com.hampcode.pagoya.transfer.dto.TransferByCurrencyReport;

import java.util.List;

/**
 * Respuesta del reporte de transferencias por moneda.
 * - data:    las filas reales del reporte, de la BD (para graficar en el frontend).
 * - insight: resumen y recomendacion generados por la IA (structured output).
 */
public record ReportResponse(List<TransferByCurrencyReport> data, ReportInsight insight) {}

package com.hampcode.pagoya.ai.service;

import com.hampcode.pagoya.ai.dto.ReportInsight;
import com.hampcode.pagoya.ai.dto.ReportResponse;
import com.hampcode.pagoya.transfer.dto.TransferByCurrencyReport;
import com.hampcode.pagoya.transfer.service.ITransferService;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

/**
 * Asistente de IA de PagoYa con dos flujos:
 * - transfer(): cliente, realiza una transferencia desde texto usando TOOL CALLING.
 * - report():   admin, reporte de transferencias por moneda; los numeros salen reales
 *               del servicio y la IA agrega un resumen breve usando STRUCTURED OUTPUT.
 */
@Service
@RequiredArgsConstructor
public class AiService {

    private final ChatClient transferChatClient;
    private final ChatModel chatModel;
    private final ITransferService transferService;

    public String transfer(String message) {
        return transferChatClient.prompt()
            .user(message)
            .call()
            .content();
    }

    public ReportResponse report() {
        List<TransferByCurrencyReport> data = transferService.reportByCurrency();

        ReportInsight insight = ChatClient.create(chatModel)
            .prompt()
            .system("""
                You are a PagoYa analyst. Given the transfers-by-currency report,
                write a short summary and a recommendation. Use ONLY this data, do not
                invent numbers. Write the resumen and recomendacion in Spanish.
                """)
            .user(data.toString())
            .call()
            .entity(ReportInsight.class);

        return new ReportResponse(data, insight);
    }
}

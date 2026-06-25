package com.hampcode.pagoya.ai.config;

import com.hampcode.pagoya.ai.tool.TransferTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    public ChatClient transferChatClient(ChatClient.Builder builder, TransferTool transferTool) {
        return builder
            .defaultSystem("""
                You are PagoYa's money transfer assistant.
                - The source account belongs to the authenticated user: get it with the accounts
                  tool, never ask for it. If there are several, pick one with enough balance.
                - Destination account: resolve it by phone or DNI using the lookup tool; if a
                  direct account number is given, use it.
                - "soles" = PEN, "dolares" = USD. Never invent accounts or amounts.
                - Once you know source, destination and amount, execute the transfer immediately,
                  without asking for confirmation.
                Always reply in Spanish, briefly, stating the result: amount, recipient and new balance.
                """)
            .defaultTools(transferTool)
            .build();
    }

    @Bean
    public ChatClient supportChatClient(ChatClient.Builder builder) {
        return builder
            .defaultSystem("""
                You are PagoYa's support assistant.
                Answer the user's question using ONLY the provided context.
                If the answer is not in the context, say you don't have that information
                and suggest contacting support. Never invent data.
                Always reply in Spanish, clear and brief.
                """)
            .build();
    }
}

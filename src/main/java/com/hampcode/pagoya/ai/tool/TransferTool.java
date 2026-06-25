package com.hampcode.pagoya.ai.tool;

import com.hampcode.pagoya.account.dto.AccountResponse;
import com.hampcode.pagoya.account.dto.RecipientAccountResponse;
import com.hampcode.pagoya.account.service.IAccountService;
import com.hampcode.pagoya.transfer.dto.TransferRequest;
import com.hampcode.pagoya.transfer.dto.TransferResponse;
import com.hampcode.pagoya.transfer.model.TransferType;
import com.hampcode.pagoya.transfer.service.ITransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TransferTool {

    private final ITransferService transferService;
    private final IAccountService accountService;

    @Tool(description = "List the authenticated user's accounts (account number, balance, type). "
        + "Use it to find the source account without asking the user.")
    public List<AccountResponse> getMyAccounts() {
        return accountService.findMyAccounts(PageRequest.of(0, 50)).getContent();
    }

    @Tool(description = "Find a recipient's accounts by phone number. Returns the account number, "
        + "type and masked owner name. Preferred way to identify the recipient.")
    public List<RecipientAccountResponse> findRecipientByPhone(
            @ToolParam(description = "recipient phone number, 9 digits") String phone) {
        return accountService.findRecipientAccountsByPhone(phone);
    }

    @Tool(description = "Find a recipient's accounts by DNI. Use when the user identifies the "
        + "recipient by DNI instead of phone.")
    public List<RecipientAccountResponse> findRecipientByDni(
            @ToolParam(description = "recipient DNI, 8 digits") String dni) {
        return accountService.findRecipientAccountsByDni(dni);
    }

    @Tool(description = "Execute a money transfer between two PagoYa accounts. "
        + "The source account must belong to the authenticated user; do not invent accounts or amounts.")
    public TransferResponse transfer(String sourceAccountNumber, String targetAccountNumber,
                                     BigDecimal amount, String currency) {

        TransferRequest request = new TransferRequest(
            sourceAccountNumber,
            TransferType.INTERNAL,
            targetAccountNumber,
            null, null, null,
            amount,
            currency
        );
        return transferService.transfer(request);
    }
}

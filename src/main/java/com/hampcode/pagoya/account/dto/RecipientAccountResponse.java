package com.hampcode.pagoya.account.dto;

import com.hampcode.pagoya.account.model.AccountType;

/**
 * Vista mínima de una cuenta destino para elegir a quién transferir.
 * No expone saldo, id interno ni el DNI; muestra el nombre del titular
 * para que el usuario confirme que es el destinatario correcto (como en Yape/Plin).
 */
public record RecipientAccountResponse(
    String accountNumber,
    AccountType type,
    String ownerName
) {}

package com.CodeBlooded.GlobaLyncBackend.enums;
public enum TransferState {
    CREATED,
    PSP_PENDING,
    PSP_CONFIRMED,
    FX_LOCKED,
    LIQUIDITY_RESERVED,
    READY_FOR_PAYOUT,
    PAYOUT_PROCESSING,
    PAYOUT_CONFIRMED,
    CLAIMED,
    COMPLETED,
    ANCHORED,
    FAILED
}

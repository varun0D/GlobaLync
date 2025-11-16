package com.CodeBlooded.GlobaLyncBackend.repositories.jpa;

import com.CodeBlooded.GlobaLyncBackend.entities.jpa.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ReceiptRepository extends JpaRepository<Receipt, String> {
}

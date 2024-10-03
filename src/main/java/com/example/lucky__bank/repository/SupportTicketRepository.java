package com.example.lucky__bank.repository;

import com.example.lucky__bank.model.SupportTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportTicketRepository extends JpaRepository<SupportTicket,Long> {
    List<SupportTicket> findByUserId(Long userId);
    List<SupportTicket> findByAdminReplyIsNull();
}

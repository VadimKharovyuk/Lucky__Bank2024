package com.example.lucky__bank.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;  // Владелец кредита

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")  // Привязываем карту
    private Card card;

    private BigDecimal loanAmount;  //сумма кредита
    private double interestRate;  // Процентная ставка
    private int termInMonths;   //Срок кредита
    private BigDecimal monthlyPayment; /// Ежемесячный платеж
    private boolean approved;   //одобрен ?
    private String purpose;    //описания

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "credit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PaymentSchedule> paymentSchedules = new ArrayList<>();



}
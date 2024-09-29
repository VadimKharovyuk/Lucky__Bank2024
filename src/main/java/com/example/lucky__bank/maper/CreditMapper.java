package com.example.lucky__bank.maper;

import com.example.lucky__bank.dto.CreditDto;
import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.model.Credit;
import com.example.lucky__bank.model.Deposit;
import com.example.lucky__bank.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

 @Component
    public class CreditMapper {

        // Преобразование из сущности Credit в DTO
        public CreditDto toDto(Credit credit) {
            return CreditDto.builder()
                    .id(credit.getId())
                    .userId(credit.getUser() != null ? credit.getUser().getId() : null)
                    .amount(credit.getLoanAmount())
                    .cardId(credit.getCard() != null ? credit.getCard().getId() : null) // Привязываем cardId правильно
                    .createdAt(credit.getCreatedAt())
                    .updatedAt(credit.getUpdatedAt())
                    .paymentSchedules(credit.getPaymentSchedules() != null
                            ? credit.getPaymentSchedules().stream()
                            .map(PaymentScheduleMapper::toDto)
                            .collect(Collectors.toList())
                            : new ArrayList<>())
                    .build();
        }

        // Преобразование из CreditDto в сущность Credit
        public Credit toEntity(CreditDto creditDto, User user, Card card) {
            Credit credit = new Credit();
            credit.setId(creditDto.getId());
            credit.setUser(user);
            credit.setLoanAmount(creditDto.getAmount());
            credit.setCard(card); // Устанавливаем карту из параметра, а не через credit.getCard()

            // Устанавливаем график платежей, если он есть
            if (creditDto.getPaymentSchedules() != null) {
                credit.setPaymentSchedules(creditDto.getPaymentSchedules().stream()
                        .map(paymentScheduleDto -> PaymentScheduleMapper.toEntity(paymentScheduleDto, credit))
                        .collect(Collectors.toList()));
            }

            return credit;
        }
    }

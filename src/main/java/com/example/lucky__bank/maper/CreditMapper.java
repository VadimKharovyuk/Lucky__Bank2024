package com.example.lucky__bank.maper;

import com.example.lucky__bank.dto.CreditDto;
import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.model.Credit;
import com.example.lucky__bank.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

//package com.example.lucky__bank.maper;
//
//import com.example.lucky__bank.dto.CreditDto;
//import com.example.lucky__bank.model.Card;
//import com.example.lucky__bank.model.Credit;
//import com.example.lucky__bank.model.Deposit;
//import com.example.lucky__bank.model.User;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.stream.Collectors;
//
// @Component
//    public class CreditMapper {
//
//        // Преобразование из сущности Credit в DTO
//        public CreditDto toDto(Credit credit) {
//            return CreditDto.builder()
//                    .id(credit.getId())
//                    .userId(credit.getUser() != null ? credit.getUser().getId() : null)
//                    .amount(credit.getLoanAmount())
//                    .cardId(credit.getCard() != null ? credit.getCard().getId() : null) // Привязываем cardId правильно
//                    .createdAt(credit.getCreatedAt())
//                    .updatedAt(credit.getUpdatedAt())
//                    .paymentSchedules(credit.getPaymentSchedules() != null
//                            ? credit.getPaymentSchedules().stream()
//                            .map(PaymentScheduleMapper::toDto)
//                            .collect(Collectors.toList())
//                            : new ArrayList<>())
//                    .build();
//        }
//
//        // Преобразование из CreditDto в сущность Credit
//        public Credit toEntity(CreditDto creditDto, User user, Card card) {
//            Credit credit = new Credit();
//            credit.setId(creditDto.getId());
//            credit.setUser(user);
//            credit.setLoanAmount(creditDto.getAmount());
//            credit.setCard(card);  // Устанавливаем карту напрямую
//
//            // Добавляем новые поля
//            credit.setTermInMonths(creditDto.getTermInMonths());
//            credit.setPurpose(creditDto.getPurpose());
//            credit.setMonthlyPayment(creditDto.getMonthlyPayment());
//            credit.setInterestRate(creditDto.getInterestRate());
//            credit.setCreatedAt(creditDto.getCreatedAt());
//            credit.setUpdatedAt(creditDto.getUpdatedAt());
//
//            // Устанавливаем график платежей, если он есть
//            if (creditDto.getPaymentSchedules() != null) {
//                credit.setPaymentSchedules(creditDto.getPaymentSchedules().stream()
//                        .map(paymentScheduleDto -> PaymentScheduleMapper.toEntity(paymentScheduleDto, credit))
//                        .collect(Collectors.toList()));
//            }
//
//            return credit;
//        }
//    }
@Component
public class CreditMapper {

    public CreditDto toDto(Credit credit) {
        return CreditDto.builder()
                .id(credit.getId())  // id кредита
                .userId(credit.getUser() != null ? credit.getUser().getId() : null) // Получаем ID пользователя, если он существует
                .amount(credit.getLoanAmount()) // Сумма кредита
                .cardId(credit.getCard() != null ? credit.getCard().getId() : null) // ID карты, если она существует
                .createdAt(credit.getCreatedAt()) // Дата создания
                .updatedAt(credit.getUpdatedAt()) // Дата обновления
                .purpose(credit.getPurpose())
                .termInMonths(credit.getTermInMonths())
                .monthlyPayment(credit.getMonthlyPayment())
                .interestRate(credit.getInterestRate())
                .paymentSchedules(credit.getPaymentSchedules() != null
                        ? credit.getPaymentSchedules().stream()
                        .map(PaymentScheduleMapper::toDto) // Преобразуем график платежей в DTO
                        .collect(Collectors.toList())
                        : new ArrayList<>()) // Если нет графика, возвращаем пустой список
                .build();
    }

    // Преобразование из CreditDto в сущность Credit
    public Credit toEntity(CreditDto creditDto, User user, Card card) {
        Credit credit = new Credit();
        credit.setId(creditDto.getId());
        credit.setUser(user);
        credit.setLoanAmount(creditDto.getAmount());
        credit.setCard(card);  // Устанавливаем карту напрямую

        // Добавляем новые поля
        credit.setTermInMonths(creditDto.getTermInMonths());
        credit.setPurpose(creditDto.getPurpose());
        credit.setMonthlyPayment(creditDto.getMonthlyPayment());
        credit.setInterestRate(creditDto.getInterestRate());
        credit.setCreatedAt(creditDto.getCreatedAt());
        credit.setUpdatedAt(creditDto.getUpdatedAt());

        // Устанавливаем график платежей, если он есть
        if (creditDto.getPaymentSchedules() != null) {
            credit.setPaymentSchedules(creditDto.getPaymentSchedules().stream()
                    .map(paymentScheduleDto -> PaymentScheduleMapper.toEntity(paymentScheduleDto, credit))
                    .collect(Collectors.toList()));
        }

        return credit;
    }
}
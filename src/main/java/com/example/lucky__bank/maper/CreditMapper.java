package com.example.lucky__bank.maper;

import com.example.lucky__bank.dto.CreditDto;
import com.example.lucky__bank.model.Credit;
import com.example.lucky__bank.model.Deposit;
import com.example.lucky__bank.model.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class CreditMapper {


    public  CreditDto toDto(Credit credit) {
        return CreditDto.builder()
                .id(credit.getId())
                .userId(credit.getUser() != null ? credit.getUser().getId() : null)
                .amount(credit.getLoanAmount())
                .createdAt(credit.getCreatedAt())  // Убедись, что есть поле createdAt в Credit
                .updatedAt(credit.getUpdatedAt())  // Убедись, что есть поле updatedAt в Credit
                .paymentSchedules(credit.getPaymentSchedules() != null
                        ? credit.getPaymentSchedules().stream()
                        .map(PaymentScheduleMapper::toDto)
                        .collect(Collectors.toList())
                        : new ArrayList<>())
                .build();
    }

    // Преобразование из CreditDto в сущность Credit
    public  Credit toEntity(CreditDto creditDto, User user) {
        Credit credit = new Credit();
        credit.setId(creditDto.getId());
        credit.setUser(user);  // Устанавливаем пользователя
        credit.setLoanAmount(creditDto.getAmount());

        // Если PaymentSchedule можно установить на этом этапе
        if (creditDto.getPaymentSchedules() != null) {
            credit.setPaymentSchedules(creditDto.getPaymentSchedules().stream()
                    .map(paymentScheduleDto -> PaymentScheduleMapper.toEntity(paymentScheduleDto, credit))
                    .collect(Collectors.toList()));
        }

        return credit;
    }
}
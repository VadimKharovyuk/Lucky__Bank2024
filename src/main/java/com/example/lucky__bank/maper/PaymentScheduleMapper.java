package com.example.lucky__bank.maper;

import com.example.lucky__bank.dto.PaymentScheduleDto;
import com.example.lucky__bank.model.Credit;
import com.example.lucky__bank.model.PaymentSchedule;
import org.springframework.stereotype.Component;

@Component
public class PaymentScheduleMapper {

    // Преобразование из сущности PaymentSchedule в DTO PaymentScheduleDto
    public static PaymentScheduleDto toDto(PaymentSchedule paymentSchedule) {
        return PaymentScheduleDto.builder()
                .id(paymentSchedule.getId())
                .creditId(paymentSchedule.getCredit() != null ? paymentSchedule.getCredit().getId() : null)
                .paymentAmount(paymentSchedule.getPaymentAmount())
                .paymentDate(paymentSchedule.getPaymentDate())
                .paid(paymentSchedule.isPaid())
                .build();
    }


    // Преобразование из DTO PaymentScheduleDto в сущность PaymentSchedule
    public static PaymentSchedule toEntity(PaymentScheduleDto dto, Credit credit) {
        PaymentSchedule paymentSchedule = new PaymentSchedule();
        paymentSchedule.setId(dto.getId());
        paymentSchedule.setCredit(credit);  // Связь с кредитом
        paymentSchedule.setPaymentAmount(dto.getPaymentAmount());
        paymentSchedule.setPaymentDate(dto.getPaymentDate());
        paymentSchedule.setPaid(dto.isPaid());
        return paymentSchedule;
    }
}
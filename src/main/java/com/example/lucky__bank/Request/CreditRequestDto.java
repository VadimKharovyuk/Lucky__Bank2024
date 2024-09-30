package com.example.lucky__bank.Request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditRequestDto {


    private Long userId;
    private Long cardId;


//    @NotNull(message = "Сумма кредита обязательна")
//    @DecimalMin(value = "0.0", inclusive = false, message = "Сумма кредита должна быть больше 0")
    private BigDecimal loanAmount;
//
//    @NotNull(message = "Процентная ставка обязательна")
//    @Min(value = 0, message = "Процентная ставка должна быть неотрицательной")
    private double interestRate;

//    @NotNull(message = "Срок в месяцах обязателен")
//    @Min(value = 1, message = "Срок в месяцах должен быть не менее 1")
    private int termInMonths;

//    @NotBlank(message = "Цель кредита обязательна")
    private String purpose;

}

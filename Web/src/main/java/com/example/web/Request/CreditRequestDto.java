package com.example.web.Request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditRequestDto {

    private Long userId;
    private Long cardId;

    private BigDecimal loanAmount;
    private double interestRate;

    private int termInMonths;
    private String purpose;
}


//@NotNull(message = "Сумма кредита обязательна")
//private BigDecimal loanAmount;
//
//@NotNull(message = "Процентная ставка обязательна")
//@Min(value = 0, message = "Процентная ставка должна быть положительной")
//@Max(value = 100, message = "Процентная ставка не может превышать 100%")
//private double interestRate;
//
//@NotNull(message = "Срок кредита обязателен")
//@Min(value = 1, message = "Минимальный срок кредита - 1 месяц")
//private int termInMonths;
//
//@NotBlank(message = "Цель кредита обязательна")
//private String purpose;
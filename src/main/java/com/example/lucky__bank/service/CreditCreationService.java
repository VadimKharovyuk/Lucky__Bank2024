package com.example.lucky__bank.service;

import com.example.lucky__bank.dto.CreditDto;
import com.example.lucky__bank.maper.CreditMapper;
import com.example.lucky__bank.model.Credit;
import com.example.lucky__bank.model.PaymentSchedule;
import com.example.lucky__bank.model.User;
import com.example.lucky__bank.repository.CreditRepository;
import com.example.lucky__bank.repository.PaymentScheduleRepository;
import com.example.lucky__bank.repository.UserRepository;
import com.example.lucky__bank.service.Schedule.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreditCreationService {

    private final CreditRepository creditRepository;
    private final UserRepository userRepository;
    private final PaymentScheduleRepository paymentScheduleRepository;
    private final CreditMapper creditMapper;
    private final PaymentService paymentService;

    @Transactional
    public CreditDto createCredit(Long userId, BigDecimal loanAmount, double interestRate, int termInMonths, String purpose) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Credit credit = new Credit();
        credit.setUser(user);
        credit.setLoanAmount(loanAmount);
        credit.setInterestRate(interestRate);
        credit.setTermInMonths(termInMonths);
        credit.setPurpose(purpose);
        credit.setApproved(false); // По умолчанию кредит не одобрен
        credit.setCreatedAt(LocalDateTime.now());
        credit.setUpdatedAt(LocalDateTime.now());

        // Расчет ежемесячного платежа
        BigDecimal monthlyInterestRate = BigDecimal.valueOf(interestRate / 12 / 100);
        BigDecimal monthlyPayment = calculateMonthlyPayment(loanAmount, monthlyInterestRate, termInMonths);
        credit.setMonthlyPayment(monthlyPayment);

        Credit savedCredit = creditRepository.save(credit);

        // Создание графика платежей
        createPaymentSchedule(savedCredit);

        // Используем CreditMapper для преобразования сущности в DTO
        return creditMapper.toDto(savedCredit);
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal loanAmount, BigDecimal monthlyInterestRate, int termInMonths) {
        BigDecimal onePlusRate = monthlyInterestRate.add(BigDecimal.ONE);
        BigDecimal powerFactor = onePlusRate.pow(termInMonths);
        BigDecimal numerator = loanAmount.multiply(monthlyInterestRate).multiply(powerFactor);
        BigDecimal denominator = powerFactor.subtract(BigDecimal.ONE);
        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }



    private void createPaymentSchedule(Credit credit) {
        LocalDate currentDate = LocalDate.now();
        BigDecimal remainingBalance = credit.getLoanAmount();

        for (int i = 1; i <= credit.getTermInMonths(); i++) {
            PaymentSchedule payment = new PaymentSchedule();
            payment.setCredit(credit);
            payment.setPaymentDate(currentDate.plusMonths(i));
            payment.setPaymentAmount(credit.getMonthlyPayment());
            payment.setPaid(false);

            BigDecimal interest = remainingBalance.multiply(BigDecimal.valueOf(credit.getInterestRate() / 12 / 100));
            BigDecimal principal = credit.getMonthlyPayment().subtract(interest);
            remainingBalance = remainingBalance.subtract(principal);

            paymentScheduleRepository.save(payment);
        }
    }
}
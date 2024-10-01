package com.example.lucky__bank.service;

import com.example.lucky__bank.Exception.CreditNotFoundException;
import com.example.lucky__bank.Exception.InsufficientFundsException;
import com.example.lucky__bank.dto.CreditDto;
import com.example.lucky__bank.maper.CreditMapper;
import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.model.Credit;
import com.example.lucky__bank.model.PaymentSchedule;
import com.example.lucky__bank.model.User;
import com.example.lucky__bank.repository.CardRepository;
import com.example.lucky__bank.repository.CreditRepository;
import com.example.lucky__bank.repository.PaymentScheduleRepository;
import com.example.lucky__bank.repository.UserRepository;
import com.example.lucky__bank.service.Schedule.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditCreationService {

    private final CreditRepository creditRepository;
    private final UserRepository userRepository;
    private final PaymentScheduleRepository paymentScheduleRepository;
    private final CreditMapper creditMapper;
    private final PaymentService paymentService;
    private final CardRepository cardRepository;



    public void makePayment(Long creditId, BigDecimal paymentAmount) throws InsufficientFundsException, CreditNotFoundException {
        try {
            paymentService.processPayment(creditId, paymentAmount);
        } catch (InsufficientFundsException e) {
            log.error("Insufficient funds for payment on credit ID: {}", creditId, e);
            throw e;
        } catch (RuntimeException e) {
            log.error("Error processing payment for credit ID: {}", creditId, e);
            throw new CreditNotFoundException("Credit not found or error processing payment: " + e.getMessage());
        }
    }

    @Transactional
    public CreditDto createCredit(Long userId, Long cardId, BigDecimal loanAmount, double interestRate, int termInMonths, String purpose) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Credit credit = new Credit();
        credit.setUser(user);
        credit.setCard(cardId != null ? cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("Card not found with id: " + cardId)) : null);
        credit.setLoanAmount(loanAmount);
        credit.setInterestRate(interestRate);
        credit.setTermInMonths(termInMonths);
        credit.setPurpose(purpose);
        credit.setApproved(false);
        credit.setCreatedAt(LocalDateTime.now());
        credit.setUpdatedAt(LocalDateTime.now());

        BigDecimal monthlyInterestRate = BigDecimal.valueOf(interestRate / 12 / 100);
        BigDecimal monthlyPayment = calculateMonthlyPayment(loanAmount, monthlyInterestRate, termInMonths);
        credit.setMonthlyPayment(monthlyPayment);

        Credit savedCredit = creditRepository.save(credit);

        createPaymentSchedule(savedCredit);
        System.out.println("Saved Credit: " + savedCredit);
        return creditMapper.toDto(savedCredit);
    }



    private BigDecimal calculateMonthlyPayment(BigDecimal loanAmount, BigDecimal monthlyInterestRate, int termInMonths) {
        BigDecimal onePlusRate = monthlyInterestRate.add(BigDecimal.ONE);
        BigDecimal powerFactor = onePlusRate.pow(termInMonths);
        BigDecimal numerator = loanAmount.multiply(monthlyInterestRate).multiply(powerFactor);
        BigDecimal denominator = powerFactor.subtract(BigDecimal.ONE);
        return numerator.divide(denominator, 10, RoundingMode.HALF_UP);
    }



    private void createPaymentSchedule(Credit credit) {
        LocalDate currentDate = LocalDate.now();
        BigDecimal remainingBalance = credit.getLoanAmount();
        BigDecimal monthlyInterestRate = BigDecimal.valueOf(credit.getInterestRate() / 12 / 100);

        for (int i = 1; i <= credit.getTermInMonths(); i++) {
            PaymentSchedule payment = new PaymentSchedule();
            payment.setCredit(credit);
            payment.setPaymentDate(currentDate.plusMonths(i));

            BigDecimal interest = remainingBalance.multiply(monthlyInterestRate)
                    .setScale(10, RoundingMode.HALF_UP);

            BigDecimal principal = credit.getMonthlyPayment().subtract(interest);

            if (i == credit.getTermInMonths()) {
                payment.setPaymentAmount(remainingBalance.add(interest));
            } else {
                payment.setPaymentAmount(credit.getMonthlyPayment());
            }

            payment.setInterestAmount(interest);
            payment.setPrincipalAmount(principal);

            remainingBalance = remainingBalance.subtract(principal);
            if (remainingBalance.compareTo(BigDecimal.ZERO) < 0) {
                remainingBalance = BigDecimal.ZERO;
            }

            payment.setPaid(false);

            paymentScheduleRepository.save(payment);
        }
    }

}
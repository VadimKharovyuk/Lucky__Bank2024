package com.example.lucky__bank.service.Schedule;

import com.example.lucky__bank.Exception.InsufficientFundsException;
import com.example.lucky__bank.dto.CreditDto;
import com.example.lucky__bank.dto.PaymentScheduleDto;
import com.example.lucky__bank.maper.CreditMapper;
import com.example.lucky__bank.maper.PaymentScheduleMapper;
import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.model.Credit;
import com.example.lucky__bank.model.PaymentSchedule;
import com.example.lucky__bank.model.User;
import com.example.lucky__bank.repository.CreditRepository;
import com.example.lucky__bank.repository.UserRepository;
import com.example.lucky__bank.service.CardService;
import com.example.lucky__bank.service.CreditService;
import com.example.lucky__bank.repository.PaymentScheduleRepository; // Не забудьте импортировать
import com.example.lucky__bank.service.EmailService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final CreditService creditService;
    private final CreditMapper creditMapper;
    private final CreditRepository creditRepository ;
    private final  EmailService emailService ;
    private final PaymentScheduleRepository paymentScheduleRepository;
    private final CardService cardService;


    @Transactional
    public void processPayment(Long creditId, BigDecimal paymentAmount) throws InsufficientFundsException {
        Credit credit = creditRepository.findById(creditId)
                .orElseThrow(() -> new RuntimeException("Credit not found for ID: " + creditId));

        User user = credit.getUser();
        Card card = credit.getCard();

        if (card == null) {
            throw new RuntimeException("No card linked to this credit");
        }

        // Проверяем, достаточно ли средств на карте
        if (card.getBalance().compareTo(paymentAmount) < 0) {
            throw new InsufficientFundsException("Insufficient funds on the card");
        }

        boolean paymentProcessed = false;

        for (PaymentSchedule payment : credit.getPaymentSchedules()) {
            if (!payment.isPaid() && paymentAmount.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal paymentToApply = payment.getPaymentAmount().min(paymentAmount);

                payment.setPaid(true);
                payment.setPaymentAmount(paymentToApply);

                BigDecimal newLoanAmount = credit.getLoanAmount().subtract(paymentToApply);
                if (newLoanAmount.compareTo(BigDecimal.ZERO) < 0) {
                    newLoanAmount = BigDecimal.ZERO;
                }
                credit.setLoanAmount(newLoanAmount);

                // Снимаем деньги с карты
                cardService.withdrawMoney(card.getId(), paymentToApply);

                paymentAmount = paymentAmount.subtract(paymentToApply);
                paymentProcessed = true;

                // Отправляем уведомление о каждом обработанном платеже
                emailService.sendPaymentNotification(user, payment);
            }

            if (paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }
        }

        if (!paymentProcessed) {
            throw new RuntimeException("Не найдено неоплаченных платежей для обработки");
        }

        if (credit.getLoanAmount().compareTo(BigDecimal.ZERO) < 0) {
            credit.setLoanAmount(BigDecimal.ZERO);
        }

        credit.setUpdatedAt(LocalDateTime.now());
        creditRepository.save(credit);
    }

    public List<PaymentScheduleDto> getPaymentSchedulesByCreditId(Long creditId) {
        List<PaymentSchedule> schedule = paymentScheduleRepository.findAllByCreditId(creditId);
        return schedule.stream()
                .map(PaymentScheduleMapper::toDto)  // Используем ссылку на статический метод
                .collect(Collectors.toList());
    }




    // Проверка всех кредитов
    @Scheduled(cron = "0 0 1 * * ?") // Каждый первый день месяца в 1:00
    public void checkForLatePayments() {
        List<CreditDto> credits = creditService.getAllCredits(); // Получаем все кредиты

        LocalDate today = LocalDate.now();
        for (CreditDto creditDto : credits) {
            Credit credit = creditMapper.toEntity(creditDto, null, null); // Преобразование DTO в Credit сущность

            for (PaymentScheduleDto paymentDto : creditDto.getPaymentSchedules()) {
                if (!paymentDto.isPaid() && paymentDto.getPaymentDate().isBefore(today)) {
                    // Преобразование PaymentScheduleDto обратно в сущность PaymentSchedule
                    PaymentSchedule paymentSchedule = PaymentScheduleMapper.toEntity(paymentDto, credit);

                    // Применение штрафа или дополнительные действия
                    creditService.applyLateFee(paymentSchedule); // Передача сущности
                }
            }
        }
    }
}

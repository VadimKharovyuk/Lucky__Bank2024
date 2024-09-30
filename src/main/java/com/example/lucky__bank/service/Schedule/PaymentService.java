package com.example.lucky__bank.service.Schedule;

import com.example.lucky__bank.dto.CreditDto;
import com.example.lucky__bank.dto.PaymentScheduleDto;
import com.example.lucky__bank.maper.CreditMapper;
import com.example.lucky__bank.maper.PaymentScheduleMapper;
import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.model.Credit;
import com.example.lucky__bank.model.PaymentSchedule;
import com.example.lucky__bank.model.User;
import com.example.lucky__bank.repository.CardRepository;
import com.example.lucky__bank.repository.CreditRepository;
import com.example.lucky__bank.repository.UserRepository;
import com.example.lucky__bank.service.CreditService;
import com.example.lucky__bank.repository.PaymentScheduleRepository; // Не забудьте импортировать
import com.example.lucky__bank.service.EmailService;
import com.example.lucky__bank.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final CreditService creditService;
    private final CreditMapper creditMapper;
    private final CreditRepository creditRepository ;
    private final  EmailService emailService ;

    public void processPayment(Long creditId, BigDecimal paymentAmount) {
        Credit credit = creditRepository.findById(creditId)
                .orElseThrow(() -> new RuntimeException("Credit not found for ID: " + creditId));

        boolean paymentProcessed = false;

        // Работаем напрямую с коллекцией PaymentSchedule
        for (PaymentSchedule payment : credit.getPaymentSchedules()) {
            if (!payment.isPaid() && paymentAmount.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal paymentToApply = payment.getPaymentAmount().min(paymentAmount); // Определяем сумму, которая будет использована

                payment.setPaid(true);
                payment.setPaymentAmount(paymentToApply); // Обновляем сумму платежа

                BigDecimal newLoanAmount = credit.getLoanAmount().subtract(paymentToApply);
                credit.setLoanAmount(newLoanAmount);

                paymentAmount = paymentAmount.subtract(paymentToApply); // Уменьшаем сумму оставшегося платежа
                paymentProcessed = true;

                // Здесь можно добавить логику отправки уведомления
                // emailService.sendPaymentNotification(user, payment);
            }

            if (paymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
                break; // Если весь платёж обработан, выходим из цикла
            }
        }

        if (!paymentProcessed) {
            throw new RuntimeException("Не найдено неоплаченных платежей для обработки");
        }

        credit.setUpdatedAt(LocalDateTime.now());
        creditRepository.save(credit); // Сохраняем изменения в кредите и связанных платежах
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

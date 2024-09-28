package com.example.lucky__bank.service.Schedule;

import com.example.lucky__bank.dto.CreditDto;
import com.example.lucky__bank.dto.PaymentScheduleDto;
import com.example.lucky__bank.maper.CreditMapper;
import com.example.lucky__bank.maper.PaymentScheduleMapper;
import com.example.lucky__bank.model.Credit;
import com.example.lucky__bank.model.PaymentSchedule;
import com.example.lucky__bank.repository.CreditRepository;
import com.example.lucky__bank.service.CreditService;
import com.example.lucky__bank.repository.PaymentScheduleRepository; // Не забудьте импортировать
import com.example.lucky__bank.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final CreditService creditService;
    private final PaymentScheduleRepository paymentRepository;
    private final CreditMapper creditMapper;
    private final CreditRepository creditRepository ;
    private final  EmailService emailService ;



    //оплата кредита

    public void processPayment(Long creditId, BigDecimal paymentAmount) {
        CreditDto creditDto = creditService.getCreditById(creditId);
        Credit credit = creditMapper.toEntity(creditDto, null); // Преобразуем CreditDto в Credit для работы с сущностью

        // Найдем первый неоплаченный платеж
        for (PaymentScheduleDto paymentDto : creditDto.getPaymentSchedules()) {
            if (!paymentDto.isPaid()) {
                // Проверяем, достаточно ли суммы для оплаты
                if (paymentAmount.compareTo(paymentDto.getPaymentAmount()) >= 0) {
                    // Преобразуем DTO в сущность для сохранения
                    PaymentSchedule payment = PaymentScheduleMapper.toEntity(paymentDto, credit);
                    payment.setPaid(true); // Отметить платеж как оплаченный

                    // Сохраняем изменения в базе данных
                    paymentRepository.save(payment);

                    // Уменьшаем оставшийся долг по кредиту
                    credit.setLoanAmount(credit.getLoanAmount().subtract(payment.getPaymentAmount()));
                    creditRepository.save(credit); // Сохраняем обновленный кредит

                    // Логика уведомления клиента
                    emailService.sendPaymentNotification(credit.getUser(), payment); // Передаем User вместо User ID
                    break; // Выходим после первого успешного платежа
                } else {
                    // Логика обработки недостаточной суммы
                    throw new RuntimeException("Недостаточная сумма для оплаты. Требуется: " + paymentDto.getPaymentAmount());
                }
            }
        }
    }
//проверка всех кредитов
    @Scheduled(cron = "0 0 1 * * ?") // Каждый первый день месяца в 1:00
    public void checkForLatePayments() {
        List<CreditDto> credits = creditService.getAllCredits(); // Получаем все кредиты

        LocalDate today = LocalDate.now();
        for (CreditDto creditDto : credits) {
            Credit credit = creditMapper.toEntity(creditDto, null); // Преобразование DTO в Credit сущность

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


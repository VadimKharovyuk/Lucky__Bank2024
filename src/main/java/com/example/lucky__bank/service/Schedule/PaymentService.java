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
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final CreditService creditService;
    private final PaymentScheduleRepository paymentRepository;
    private final CreditMapper creditMapper;
    private final CreditRepository creditRepository ;
    private final  EmailService emailService ;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    public void processPayment(Long creditId, BigDecimal paymentAmount) {
        CreditDto creditDto = creditService.getCreditById(creditId);

        // Получаем пользователя
        User user = userRepository.findById(creditDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found for credit ID: " + creditId));

        // Получите карту по cardId, если необходимо
        Card card = cardRepository.findById(creditDto.getCardId())
                .orElseThrow(() -> new RuntimeException("Card not found for card ID: " + creditDto.getCardId()));

        // Преобразуем CreditDto в Credit
        Credit credit = creditMapper.toEntity(creditDto, user, card);

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
                    System.out.println("Проверка платежа: " + paymentDto.getPaymentAmount() + " на сумму " + paymentAmount);

                    // Уменьшаем оставшийся долг по кредиту
                    BigDecimal newLoanAmount = credit.getLoanAmount().subtract(payment.getPaymentAmount());
                    credit.setLoanAmount(newLoanAmount);

                    // Обновляем дату последнего обновления кредита
                    credit.setUpdatedAt(LocalDateTime.now());

                    // Установите поля кредита напрямую, если они уже установлены через creditMapper
                    // Например, creditMapper устанавливает card, loanAmount и другие поля, нет необходимости их устанавливать еще раз.
                    // credit.setCard(card); // Это уже будет установлено в creditMapper
                    // credit.setLoanAmount(newLoanAmount); // Это также уже будет установлено, если у вас есть логика в creditMapper.

                    // Сохраняем обновленный кредит
                    creditRepository.save(credit);

                    // Логика уведомления клиента
                    emailService.sendPaymentNotification(user, payment);
                    break; // Выходим после первого успешного платежа
                } else {
                    // Логика обработки недостаточной суммы
                    throw new RuntimeException("Недостаточная сумма для оплаты. Требуется: " + paymentDto.getPaymentAmount());
                }
            }
        }
    }


//    public void processPayment(Long creditId, BigDecimal paymentAmount) {
//        CreditDto creditDto = creditService.getCreditById(creditId);
//
//        // Получаем пользователя
//        User user = userRepository.findById(creditDto.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found for credit ID: " + creditId));
//
//        // Получите карту, если нужно (или передайте null)
//        Card card = null; // Замените на реальную карту, если у вас есть доступ к ней
//
//        // Преобразуем CreditDto в Credit
//        Credit credit = creditMapper.toEntity(creditDto, user, card);
//
//        // Найдем первый неоплаченный платеж
//        for (PaymentScheduleDto paymentDto : creditDto.getPaymentSchedules()) {
//            if (!paymentDto.isPaid()) {
//                // Проверяем, достаточно ли суммы для оплаты
//                if (paymentAmount.compareTo(paymentDto.getPaymentAmount()) >= 0) {
//                    // Преобразуем DTO в сущность для сохранения
//                    PaymentSchedule payment = PaymentScheduleMapper.toEntity(paymentDto, credit);
//                    payment.setPaid(true); // Отметить платеж как оплаченный
//
//                    // Сохраняем изменения в базе данных
//                    paymentRepository.save(payment);
//                    System.out.println("Проверка платежа: " + paymentDto.getPaymentAmount() + " на сумму " + paymentAmount);
//
//                    // Уменьшаем оставшийся долг по кредиту
//                    BigDecimal newLoanAmount = credit.getLoanAmount().subtract(payment.getPaymentAmount());
//                    credit.setLoanAmount(newLoanAmount);
//
//                    // Обновляем другие поля кредита
//                    credit.setUpdatedAt(LocalDateTime.now());
//                    // Убедитесь, что эти поля не null в CreditDto
//                    credit.setCardId(creditDto.getCardId());
//                    credit.setTermInMonths(creditDto.getTermInMonths());
//                    credit.setPurpose(creditDto.getPurpose());
//                    credit.setMonthlyPayment(creditDto.getMonthlyPayment());
//                    credit.setInterestRate(creditDto.getInterestRate());
//
//                    // Сохраняем обновленный кредит
//                    creditRepository.save(credit);
//
//                    // Логика уведомления клиента
//                    emailService.sendPaymentNotification(user, payment);
//                    break; // Выходим после первого успешного платежа
//                } else {
//                    // Логика обработки недостаточной суммы
//                    throw new RuntimeException("Недостаточная сумма для оплаты. Требуется: " + paymentDto.getPaymentAmount());
//                }
//            }
//        }
//    }

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

package com.example.lucky__bank.service;

import com.example.lucky__bank.dto.CardDTO;
import com.example.lucky__bank.dto.CreditDto;
import com.example.lucky__bank.dto.PaymentScheduleDto;
import com.example.lucky__bank.dto.UserDTO;
import com.example.lucky__bank.maper.CreditMapper;
import com.example.lucky__bank.model.Card;
import com.example.lucky__bank.model.Credit;
import com.example.lucky__bank.model.PaymentSchedule;
import com.example.lucky__bank.model.User;
import com.example.lucky__bank.repository.CreditRepository;
import com.example.lucky__bank.repository.PaymentScheduleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreditService {
    private final PaymentScheduleRepository paymentRepository;
    private final CreditRepository creditRepository;
    private final CreditMapper creditMapper;
    private final EmailService emailService;
    private final UserService userService;
    private final CardService cardService;


    //штраф
    public void applyLateFee(PaymentSchedule payment) {
        // Определите фиксированный штраф за просрочку или процент
//        BigDecimal lateFee = new BigDecimal("50.00"); // Фиксированный штраф
        // Или можно использовать процент от платежа
        BigDecimal lateFee = payment.getPaymentAmount().multiply(new BigDecimal("0.05")); // 5% от суммы

        // Обновите сумму платежа, добавив штраф
        payment.setPaymentAmount(payment.getPaymentAmount().add(lateFee));

        // Сохраните обновленный платеж в базе данных
        paymentRepository.save(payment);
        // Например, отправка уведомления о просрочке
        notifyCustomer(payment);
    }

    private void notifyCustomer(PaymentSchedule payment) {
        // Логика уведомления клиента о просроченном платеже и применении штрафа
        Credit credit = payment.getCredit();
        User user = credit.getUser();
        // Отправка уведомления (например, по электронной почте или через систему уведомлений)
        emailService.sendLateFeeNotification(user.getEmail(), payment);
    }

    public List<CreditDto> getAllCredits() {
        return creditRepository.findAll().stream()
                .map(creditMapper::toDto)
                .toList();
    }

    public CreditDto getCreditById(Long creditId) {
        Credit credit = creditRepository.findById(creditId)
                .orElseThrow(() -> new EntityNotFoundException("Credit not found with id: " + creditId));
        return creditMapper.toDto(credit);
    }


    //одобрения кредита
    public CreditDto approveCredit(Long creditId) {
        // Получаем кредит по его ID
        Credit credit = creditRepository.findById(creditId)
                .orElseThrow(() -> new EntityNotFoundException("Credit not found with id: " + creditId));

        // Устанавливаем кредит как одобренный
        credit.setApproved(true);

        // Сохраняем изменения в базе данных
        Credit savedCredit = creditRepository.save(credit);

        // Преобразуем сущность в DTO и возвращаем
        return creditMapper.toDto(savedCredit);
    }


    @Transactional
    public void deleteCredit(Long creditId) {
        Credit credit = creditRepository.findById(creditId)
                .orElseThrow(() -> new RuntimeException("Credit not found with id: " + creditId));

        credit.getPaymentSchedules().clear();

        creditRepository.delete(credit);
    }

    public void updateCredit(CreditDto updatedCreditDto) {
        // Получаем существующий кредит из базы данных
        Credit existingCredit = creditRepository.findById(updatedCreditDto.getId())
                .orElseThrow(() -> new RuntimeException("Credit not found with id: " + updatedCreditDto.getId()));

        // Обновляем поля кредита
        existingCredit.setMonthlyPayment(updatedCreditDto.getAmount());
        existingCredit.setTermInMonths(updatedCreditDto.getTermInMonths());
        existingCredit.setPurpose(updatedCreditDto.getPurpose());
        existingCredit.setMonthlyPayment(updatedCreditDto.getMonthlyPayment());
        existingCredit.setInterestRate(updatedCreditDto.getInterestRate());
        existingCredit.setUpdatedAt(LocalDateTime.now());

        // Обновляем график платежей
        List<PaymentSchedule> updatedPaymentSchedules = new ArrayList<>();
        for (PaymentScheduleDto scheduleDto : updatedCreditDto.getPaymentSchedules()) {
            PaymentSchedule schedule = existingCredit.getPaymentSchedules().stream()
                    .filter(ps -> ps.getId().equals(scheduleDto.getId()))
                    .findFirst()
                    .orElseGet(PaymentSchedule::new);

            schedule.setPaymentDate(scheduleDto.getPaymentDate());
            schedule.setPaymentAmount(scheduleDto.getPaymentAmount());
            schedule.setPaid(scheduleDto.isPaid());
            schedule.setCredit(existingCredit);

            updatedPaymentSchedules.add(schedule);
        }
        existingCredit.setPaymentSchedules(updatedPaymentSchedules);

        // Сохраняем обновленный кредит в базе данных
        Credit savedCredit = creditRepository.save(existingCredit);

    }


    public List<CreditDto> getCreditsByUserAndCard(UserDTO userDto, CardDTO cardDto) {
        // Получаем сущности User и Card по их идентификаторам
        User user = userService.findEntityById(userDto.getId());
        Card card = cardService.findEntityById(cardDto.getId());

        // Проверяем, что пользователь и карта не null
        if (user == null || card == null) {
            throw new RuntimeException("User or Card not found");
        }

        // Получаем кредиты из репозитория
        List<Credit> credits = creditRepository.findByUserAndCard(user, card);

        // Преобразуем кредиты в CreditDto
        return credits.stream()
                .map(creditMapper::toDto)
                .collect(Collectors.toList());
    }
}
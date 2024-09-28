package com.example.lucky__bank.service;

import com.example.lucky__bank.model.PaymentSchedule;
import com.example.lucky__bank.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {


    private final JavaMailSender mailSender;

    public void sendRegistrationEmail(String to) {
        String subject = "Добро пожаловать в наш банк!";
        String text = "Уважаемый клиент,\n\n" +
                "Поздравляем вас с регистрацией в нашем банке! Мы рады приветствовать вас в нашей большой и дружной семье.\n\n" +
                "В нашем банке мы ценим каждого клиента и стремимся предоставить лучшие финансовые услуги. Если у вас есть какие-либо вопросы или нужна помощь, не стесняйтесь обращаться к нашей службе поддержки.\n\n" +
                "Спасибо, что выбрали нас! Мы надеемся, что ваше взаимодействие с нами будет исключительно положительным.\n\n" +
                "С наилучшими пожеланиями,\n" +
                "Команда вашего банка";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("vadimkh17@gmail.com");

        mailSender.send(message);
    }

    public void sendPaymentNotification(User user, PaymentSchedule payment) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail()); // Получаем адрес электронной почты пользователя
        message.setSubject("Уведомление о платеже");
        message.setText("Уважаемый(ая) " + user.getUsername() + ",\n\n" +
                "Ваш платеж на сумму " + payment.getPaymentAmount() + " был успешно обработан.\n" +
                "Дата платежа: " + payment.getPaymentDate() + "\n" +
                "С уважением,\nВаш банк.");

        // Отправляем сообщение
        mailSender.send(message);
    }
}

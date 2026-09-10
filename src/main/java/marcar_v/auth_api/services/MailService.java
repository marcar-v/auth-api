package marcar_v.auth_api.services;


import marcar_v.auth_api.services.exceptions.EmailDeliveryException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class MailService {
    private final JavaMailSender mailSender;

    private final String senderAddress;


    public MailService(JavaMailSender mailSender, @Value("${app.mail.from}") String senderAddress) {
        this.mailSender = mailSender;
        this.senderAddress = senderAddress;
    }

    public void sendPasswordRecoveryEmail(String to, String recoveryCode){
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(this.senderAddress);
        msg.setTo(to);
        msg.setSubject("Dual Cycle Password Recovery");
        msg.setText("Here is the code to recovery your password: " + recoveryCode);

        try {
            this.mailSender.send(msg);
        } catch (MailException exception){
            throw new EmailDeliveryException("Error sending recovery code.", exception);
        }
    }
}

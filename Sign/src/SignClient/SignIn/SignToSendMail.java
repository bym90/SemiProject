package SignClient.SignIn;
import java.io.IOException;
/*
 * ���̹� ���� �׽�Ʈ - ����(����Ȯ�� �Ϸ�)
 *
 */
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class SignToSendMail {
	public static String recipient = "";
	public static String subject = "";
	public static String body = "";
	public static String filename="";
	public String password2;
	public SignToSendMail() throws AddressException, MessagingException{
		// ���� ���� ����
        String host = "smtp.naver.com";
      final String username = "java3333@naver.com";
      final String password = "cm9xa2YzMzMz";

        int port=465;
        BASE64Encoder encoder = new BASE64Encoder();
        BASE64Decoder decoder = new BASE64Decoder();

        try {
			password2 = new String(decoder.decodeBuffer(password));
		} catch (IOException e) {
			e.printStackTrace();
		}

        // ���� ����
	    Properties props = System.getProperties();

	    props.put("mail.smtp.host", host);
	    props.put("mail.smtp.port", port);
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.ssl.enable", "true");
	    props.put("mail.smtp.ssl.trust", host);

	    Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
	        String un=username;
	        String pw=password2;
	        protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(un, pw);
	        }
	    });
	    session.setDebug(true); //for debug

	    // �޼��� �ۼ�
	    Message msg = new MimeMessage(session);
	    msg.setFrom(new InternetAddress("java3333@naver.com"));
	    msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
	    msg.setSubject(subject);
	    msg.setSentDate(new Date());

	    // ����÷�θ� ���� Multipart
	    Multipart multipart = new MimeMultipart();

	    // BodyPart�� ����
	    BodyPart bodyPart = new MimeBodyPart();
	    bodyPart.setText(body);
	    // 1. Multipart�� BodyPart�� ���δ�.
	    multipart.addBodyPart(bodyPart);
	    // 2. �̹����� ÷���Ѵ�.
	    bodyPart = new MimeBodyPart();
//	    String filename = "src/images/welcome.gif";
	    System.out.println("ȸ������ �̹������:"+filename);
	    DataSource source = new FileDataSource(filename);
	    bodyPart.setDataHandler(new DataHandler(source));
	    bodyPart.setFileName(filename);
	    //Trick is to add the content-id header here
	    bodyPart.setHeader("Content-ID", "image_id");
	    multipart.addBodyPart(bodyPart);

	    //third part for displaying image in the email body
	    bodyPart = new MimeBodyPart();
	    bodyPart.setContent("=====================================", "text/html");
	    multipart.addBodyPart(bodyPart);

	    // �̸��� �޽����� ���뿡 Multipart�� ���δ�.
	    msg.setContent(multipart);
	    Transport.send(msg);
	    System.out.println("���� �߼� �Ϸ�!");
	}

//	public static void main(String args[]) throws MessagingException{
//		new SignToSendMail();
//    }
}
package SignClient.SignIn;
import java.io.IOException;
/*
 * 네이버 메일 테스트 - 파일(구동확인 완료)
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
		// 메일 관련 정보
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

        // 세션 생성
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

	    // 메세지 작성
	    Message msg = new MimeMessage(session);
	    msg.setFrom(new InternetAddress("java3333@naver.com"));
	    msg.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
	    msg.setSubject(subject);
	    msg.setSentDate(new Date());

	    // 파일첨부를 위한 Multipart
	    Multipart multipart = new MimeMultipart();

	    // BodyPart를 생성
	    BodyPart bodyPart = new MimeBodyPart();
	    bodyPart.setText(body);
	    // 1. Multipart에 BodyPart를 붙인다.
	    multipart.addBodyPart(bodyPart);
	    // 2. 이미지를 첨부한다.
	    bodyPart = new MimeBodyPart();
//	    String filename = "src/images/welcome.gif";
	    System.out.println("회원가입 이미지경로:"+filename);
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

	    // 이메일 메시지의 내용에 Multipart를 붙인다.
	    msg.setContent(multipart);
	    Transport.send(msg);
	    System.out.println("메일 발송 완료!");
	}

//	public static void main(String args[]) throws MessagingException{
//		new SignToSendMail();
//    }
}
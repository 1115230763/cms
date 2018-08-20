package com.cms.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.cms.utils.vo.InputStreamDataVO;
import com.cms.utils.vo.MailVO;

public class MailUtil {
	private static final Logger logger = Logger.getLogger(MailUtil.class);
	
	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth", "true");// 必须 普通客户端
		props.setProperty("mail.transport.protocol", "smtp");// 必须选择协议
		props.put("mail.smtp.ssl.enable", "true");

		Session session = Session.getDefaultInstance(props);
		session.setDebug(true);// 设置debug模式 在控制台看到交互信息
		Message msg = new MimeMessage(session); // 建立一个要发送的信息
		msg.setText("li72  welcome ");// 设置简单的发送内容
		
		Transport transport = session.getTransport();// 发送信息的工具
		transport.connect("smtp.exmail.qq.com", 465, "erkang.qi@bmlchina.com", "Qierkang123");// 发件人邮箱号
		msg.setFrom(new InternetAddress("erkang.qi@bmlchina.com"));// 发件人邮箱号
		
		msg.setSubject("test");
		transport.sendMessage(msg, new Address[] {new InternetAddress("473103093@qq.com")});// 对方的地址
		transport.close();
	}
	
	/**
	 * 寄信 for spring mail
	 * 
	 * @throws Exception
	 */
	public static void sendMail(MailVO mailVO, JavaMailSender mailSender) throws Exception {
		MimeMessage message = mailSender.createMimeMessage();
		
		InternetAddress[] addressArray = null;
		InternetAddress address = null;
		String to = mailVO.getTo();
		if(to.indexOf(";") > 0){
			List<InternetAddress> addressList = new ArrayList<InternetAddress>();
			for(String mailAddress : to.split(";")){
				address = new InternetAddress(mailAddress);
				addressList.add(address);
			}
			addressArray = (InternetAddress[]) addressList.toArray(new InternetAddress[addressList.size()]);
		}else{
			addressArray = new InternetAddress[1];
			addressArray[0] = new InternetAddress(to);
		}
		
		//信件內容
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setFrom(new InternetAddress(ResourceUtil.getMailUser()));
		helper.setTo(addressArray);
		helper.setSubject(mailVO.getSubject());  
		helper.setText(mailVO.getContent());
		
		//夾帶檔案
		if(mailVO.getAttchFileData() != null){
			FileSystemResource file = null;
			for(String filePath : mailVO.getAttchFileData().keySet()){
				try {
					file = new FileSystemResource(filePath);
					if(file != null && file.getInputStream().available() > 0){
						helper.addAttachment(mailVO.getAttchFileData().get(filePath), file);
					}
				} catch (Exception e) {
					logger.error(e);
					continue;
				}
			}
		}
		
		mailSender.send(message);  
	}
	
	/**
	 * 寄信 for javamail
	 * 
	 * @throws Exception
	 */
	public static void sendMail(MailVO mailVO, Session mailSession) throws Exception {
		MimeMessage msg = new MimeMessage(mailSession);
		msg.setSubject(mailVO.getSubject(), "UTF-8");
		msg.setFrom(new InternetAddress(ResourceUtil.getMailUser()));
		
		InternetAddress[] addressArray = null;
		InternetAddress address = null;
		String to = mailVO.getTo();
		if(to.indexOf(";") > 0){
			List<InternetAddress> addressList = new ArrayList<InternetAddress>();
			for(String mailAddress : to.split(";")){
				address = new InternetAddress(mailAddress);
				addressList.add(address);
			}
			addressArray = (InternetAddress[]) addressList.toArray(new InternetAddress[addressList.size()]);
		}else{
			addressArray = new InternetAddress[1];
			addressArray[0] = new InternetAddress(to);
		}
		msg.setRecipients(RecipientType.TO, addressArray);
		
		//信件內容
		Multipart multipart = new MimeMultipart("mixed");
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setText(mailVO.getContent());
		multipart.addBodyPart(messageBodyPart);
		
		//夾帶檔案
		if(mailVO.getAttchInputStreamDataList() != null){
			BodyPart fileBodyPart = null;
			DataSource source = null;
			for(InputStreamDataVO vo : mailVO.getAttchInputStreamDataList()){
				fileBodyPart = new MimeBodyPart();
				try {
					source = new ByteArrayDataSource(vo.getInputStream(), vo.getMimeType());
					if(source != null && source.getInputStream().available() > 0){
						fileBodyPart.setDataHandler(new DataHandler(source));
						fileBodyPart.setFileName(MimeUtility.encodeText(vo.getFilename(), "UTF-8", "B"));
						multipart.addBodyPart(fileBodyPart);
					}
				} catch (Exception e) {
					logger.error(e);
					continue;
				}
			}
		}
		
		if(mailVO.getAttchFileData() != null){
			BodyPart fileBodyPart = null;
			DataSource source = null;
			for(String filePath : mailVO.getAttchFileData().keySet()){
				fileBodyPart = new MimeBodyPart();
				try {
					source = new FileDataSource(filePath);
					if(source != null && source.getInputStream().available() > 0){
						fileBodyPart.setDataHandler(new DataHandler(source));
						fileBodyPart.setFileName(MimeUtility.encodeText(mailVO.getAttchFileData().get(filePath), "UTF-8", "B"));
						multipart.addBodyPart(fileBodyPart);
					}
				} catch (Exception e) {
					logger.error(e);
					continue;
				}
			}
		}
		msg.setContent(multipart);
		
		Transport.send(msg);
	}
}

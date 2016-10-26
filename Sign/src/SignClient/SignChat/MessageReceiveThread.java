package SignClient.SignChat;
import		java.net.*;

public class MessageReceiveThread extends Thread {
	ChatCard	main;
	public MessageReceiveThread(ChatCard m) {
		main = m;
	}
	public void showDlg(DatagramPacket p) {
			
		byte[]	buff = p.getData();					
		String	msg = new String(buff, 0, buff.length);
		InetAddress	addr = p.getAddress();		//	���� �ּ�
		String	ip = addr.getHostAddress();
		
		//���� �̸��� ���ۿ� �־ �־��� �и��۾�
		String temp = msg.trim();
		String temp2 = temp.substring(temp.length()-13,temp.length());
		String	name = temp2.trim();
			
		ReceiveDlg	dlg = new ReceiveDlg(main);
		String temp3 =msg.trim();
		String msg2 = temp3.substring(0,temp3.length()-13);
		
		//��� ������ �߰� 
		dlg.otherIp = ip;
		dlg.whoF.setText(name);
		dlg.area.setText(msg2);
		dlg.area.setEditable(false);
		dlg.setSize(400, 300);
		dlg.setVisible(true);
	}
	
	public void run() {
		try {
			while(true) {
				//	��Ŷ�� �޴´�.
				byte[]	buff = new byte[1024 * 1024];
				DatagramPacket	packet = new DatagramPacket(buff, buff.length);
				main.rSocket.receive(packet);
				showDlg(packet);
			}
		}
		catch(Exception e) {}
	}
}



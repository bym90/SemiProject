package SemiProject;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

class MainForm_OneTab extends JPanel{
	MainForm_OneTab(){
		
		this.setLayout(new BorderLayout());
		JTextArea receiveText = new JTextArea(20, 38);
		JScrollPane sp1 = new JScrollPane(receiveText);
		
		JTextArea sendText = new JTextArea(5, 32);
		JScrollPane sp = new JScrollPane(sendText);
		JButton send = new JButton("Enter");
		JButton attach = new JButton("Ã·ºÎ");
		
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(2, 1, 10, 10));
		p1.add(send);
		p1.add(attach);
		
		JPanel p2 = new JPanel();
		p2.add(sp);
		
		JPanel p3 = new JPanel();
		p3.add(p2);
		p3.add(p1);
		
		JPanel p4 = new JPanel();
		p4.add(sp1);
		this.add("South", p3);
		this.add("North", p4);
		
	}
}
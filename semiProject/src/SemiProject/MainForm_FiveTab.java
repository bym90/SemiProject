package SemiProject;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

class MainForm_FiveTab extends JPanel{
	MainForm_FiveTab(){
		JLabel l1 = new JLabel("ȣ��");
		JComboBox box1= new JComboBox();
		box1.addItem("1");
		box1.addItem("2");
		box1.addItem("3");
		box1.addItem("4");
		box1.addItem("5");
		
		JLabel l2 = new JLabel("��߿�");
		JComboBox box2= new JComboBox();
		box2.addItem("A");
		box2.addItem("B");
		box2.addItem("C");
		box2.addItem("D");
		
		JLabel l3 = new JLabel("������");
		JComboBox box3= new JComboBox();
		box3.addItem("A");
		box3.addItem("B");
		box3.addItem("C");
		box3.addItem("D");
		
		
		JButton btn1 = new JButton("ã��");
		JButton btn2 = new JButton("�����ٷ� �Է�");
		
		this.add(l1);
		this.add(box1);
		this.add(l2);
		this.add(box2);
		this.add(l3);
		this.add(box3);
		this.add(btn1);	
		this.add(btn2);	
	}
}
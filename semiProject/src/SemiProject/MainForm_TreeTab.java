package SemiProject;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class ThreeTab extends JPanel{
	ThreeTab(){
		JLabel l1 = new JLabel("����");
		JComboBox box1= new JComboBox();
		box1.addItem("����");
		box1.addItem("�Ⱦ�");
		box1.addItem("����");
		box1.addItem("ȫ��");
		box1.addItem("�ŵ���");
		
		JLabel l2 = new JLabel("�޴�");
		JComboBox box2= new JComboBox();
		box2.addItem("�ѽ�");
		box2.addItem("�߽�");
		box2.addItem("���");
		box2.addItem("�Ͻ�");
		JTextField field = new JTextField(5);
		JButton btn1 = new JButton("ã��");
		JButton btn2 = new JButton("�����ٷ� �Է�");
		
		this.add(l1);
		this.add(box1);
		this.add(l2);
		this.add(box2);
		this.add(field);
		this.add(btn1);	
		this.add(btn2);	
	}

}
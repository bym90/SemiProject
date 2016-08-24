package SemiProject;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class ThreeTab extends JPanel{
	ThreeTab(){
		JLabel l1 = new JLabel("지역");
		JComboBox box1= new JComboBox();
		box1.addItem("구로");
		box1.addItem("안양");
		box1.addItem("강남");
		box1.addItem("홍대");
		box1.addItem("신도림");
		
		JLabel l2 = new JLabel("메뉴");
		JComboBox box2= new JComboBox();
		box2.addItem("한식");
		box2.addItem("중식");
		box2.addItem("양식");
		box2.addItem("일식");
		JTextField field = new JTextField(5);
		JButton btn1 = new JButton("찾기");
		JButton btn2 = new JButton("스케줄러 입력");
		
		this.add(l1);
		this.add(box1);
		this.add(l2);
		this.add(box2);
		this.add(field);
		this.add(btn1);	
		this.add(btn2);	
	}

}
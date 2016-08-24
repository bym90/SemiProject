package SemiProject;

import java.awt.Dialog;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

class JoinForm extends Dialog {
	JoinForm(Start p) {
		super(p.frame, true);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});

		// 이름 아이디 비밀번호 생년월일 메일주소

		this.setLayout(null);

		JLabel jl1 = new JLabel("이름", JLabel.LEFT);
		JTextField t1 = new JTextField(10);
		jl1.setBounds(70, 50, 50, 30);
		t1.setBounds(180, 50, 163, 30);

		JLabel jl2 = new JLabel("아이디", JLabel.LEFT);
		JTextField t2 = new JTextField(15);
		jl2.setBounds(70, 100, 50, 30);
		t2.setBounds(180, 100, 163, 30);

		JLabel jl3 = new JLabel("비밀번호", JLabel.LEFT);
		JTextField t31 = new JTextField(20);
		jl3.setBounds(70, 150, 60, 30);
		t31.setBounds(180, 150, 163, 30);
		JLabel jl4 = new JLabel("비밀번호확인", JLabel.LEFT);
		JTextField t32 = new JTextField(20);
		jl4.setBounds(70, 200, 80, 30);
		t32.setBounds(180, 200, 163, 30);

		JLabel jl5 = new JLabel("생년월일", JLabel.LEFT);
		String[] year = { "1990", "1991", "1992" };
		JComboBox yearbox = new JComboBox(year);
		String[] month = { "1", "2", "3" };
		JComboBox monthbox = new JComboBox(month);
		String[] day = { "1", "2", "3" };
		JComboBox daybox = new JComboBox(day);
		jl5.setBounds(70, 250, 80, 30);
		yearbox.setBounds(180, 250, 70, 30);
		monthbox.setBounds(250, 250, 40, 30);
		daybox.setBounds(290, 250, 50, 30);

		JLabel jl6 = new JLabel("메일주소", JLabel.LEFT);
		JTextField t6 = new JTextField(20);
		jl6.setBounds(70, 300, 60, 30);
		t6.setBounds(180, 300, 163, 30);

		JButton yes = new JButton("가입");
		yes.setBounds(100, 350, 100, 30);
		JButton no = new JButton("취소");
		no.setBounds(220, 350, 100, 30);

		this.add(jl1);
		this.add(t1);
		this.add(jl2);
		this.add(t2);
		this.add(jl3);
		this.add(t31);
		this.add(jl4);
		this.add(t32);
		this.add(jl5);
		this.add(yearbox);
		this.add(monthbox);
		this.add(daybox);
		this.add(jl6);
		this.add(t6);
		this.add(yes);
		this.add(no);
	}
}
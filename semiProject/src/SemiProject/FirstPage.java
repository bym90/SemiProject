package SemiProject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import SemiProject.FirstPage.ButtonEvent;

class FirstPage extends JPanel {
	JTextField login;
	JPasswordField password;
	Start main;
	JButton jb1, jb2;

	FirstPage() {
	}

	FirstPage(Start p) {
		main = p;
		jb1 = new JButton("회원가입");
		jb2 = new JButton("로그인");
		login = new JTextField("아이디");
		password = new JPasswordField("****");

		this.setLayout(null);
		login.setBounds(40, 50, 120, 30);
		password.setBounds(40, 100, 120, 30);
		jb1.setBounds(170, 50, 90, 30);
		jb2.setBounds(170, 100, 90, 30);

		ButtonEvent evt = new ButtonEvent();
		jb1.addActionListener(evt);
		jb2.addActionListener(evt);

		this.add(login);
		this.add(password);
		this.add(jb1);
		this.add(jb2);
	}

	class ButtonEvent implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JButton target = (JButton) e.getSource();
			if (target == jb1) {
				JoinForm jform = new JoinForm(main);
				jform.setSize(400, 450);
				jform.setVisible(true);
			} else if (target == jb2) {
				main.card.show(main.mainP, "Page2");
				main.frame.setSize(480, 480);
				main.frame.setResizable(true);
				main.frame.setJMenuBar(main.bar);
			}
		}
	}
}
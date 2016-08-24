package SemiProject;

import GUI.*;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.*;
import java.awt.event.*;

public class Start {
	JPanel mainP;
	CJFrame frame;
	FirstPage one;
	MainForm mf;
	CardLayout card;
	JoinForm jf;

	JMenuBar bar;
	JMenuItem logout;
	JMenuItem exit;
	public Start() {
		frame = new CJFrame("�����");
		// ���� JFrame�� 3������ ���־ CardLayout()�� �ڿ� ǥ�õǹǷ� JPanel�� CJFrame�� ������
		// JPanel�� CardLayout()�� �ѷ��ָ� �ȴ�.
		mainP = new JPanel();
		frame.add(mainP);
		card = new CardLayout();
		mainP.setLayout(card);

		one = new FirstPage(this);
		mf = new MainForm(this);
		jf = new JoinForm(this);

		mainP.add(one, "Page1");
		mainP.add(mf, "Page2");

		frame.setSize(300, 200);
		frame.setVisible(true);

		frame.setResizable(false);
		
		bar = new JMenuBar();
		JMenu inform = new JMenu("����");
		logout = new JMenuItem("�α׾ƿ�");
		exit = new JMenuItem("����");
		
		bar.add(inform);
		inform.add(logout);
		inform.addSeparator();
		inform.add(exit);
	}

	public static void main(String[] args) {
		new Start();

	}

}

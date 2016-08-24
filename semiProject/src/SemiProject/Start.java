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
		frame = new CJFrame("연결고리");
		// 뻘짓 JFrame은 3겹으로 되있어서 CardLayout()이 뒤에 표시되므로 JPanel을 CJFrame에 입혀서
		// JPanel에 CardLayout()을 뿌려주면 된다.
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
		JMenu inform = new JMenu("정보");
		logout = new JMenuItem("로그아웃");
		exit = new JMenuItem("종료");
		
		bar.add(inform);
		inform.add(logout);
		inform.addSeparator();
		inform.add(exit);
	}

	public static void main(String[] args) {
		new Start();

	}

}

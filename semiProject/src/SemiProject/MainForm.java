package SemiProject;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

class MainForm extends JPanel {
	Start main;

	MainForm() {
	}

	MainForm(Start p) {

		
		main = p;
		MainForm_OneTab one = new MainForm_OneTab();
		MainForm_TwoTab two = new MainForm_TwoTab();
		ThreeTab three = new ThreeTab();
		MainForm_FourTab four = new MainForm_FourTab();
		MainForm_FiveTab five = new MainForm_FiveTab();
		
		JTabbedPane tab = new JTabbedPane();
		tab.setTabPlacement(JTabbedPane.TOP);
		tab.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tab.add(one, "ä��");
		tab.add(two, "�����ٷ�");
		tab.add(three, "��������");
		tab.add(four, "����Ʈ���");
		tab.add(five, "��ã��");
		
	 	this.setLayout(new BorderLayout());
		this.add(tab);
		
		
	}
}
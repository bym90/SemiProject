package SemiProject;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

class MainForm_TwoTab extends JPanel{
	MainForm_TwoTab(){
		String[] title = {"일", "월", "화", "수", "목", "금", "토"};
		String[][] data = {{"1", "2", "3", "4", "5", "6", "7"}, 
						{"8", "9", "10", "11", "12", "13", "14"},
						{"15", "16", "17", "18", "19", "20", "21"},
						{"21", "22", "23", "24", "25", "26", "27"},
						{"28", "29", "30", "31", "", "", ""}};
		JTable table = new JTable(data, title);
		JScrollPane sp = new JScrollPane(table);
		this.add(sp);
	}
}

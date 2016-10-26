package SignClient.SignNotice;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import NetData.*;
import SignMainFrame.MainFrame;
import javautil.*;

public class SignNotice extends JDialog {
	public DefaultTableModel model;
	public JTable table;
	JCheckBox checkB;
	JButton writeB, modifyB, deleteB, exitB;
	MainFrame main;
	Integer nNum;
	String title;
	public SignNoticeWrite Snw;
	public SignNoticeRead Snread;
	public Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	public SignNotice(MainFrame m) {
		super(m, "공지사항");
		main = m;
		this.addWindowListener(new CloseEvent());
		this.setResizable(false);

		Snw = new SignNoticeWrite(this);
		Snread = new SignNoticeRead(this);
		this.setLayout(new BorderLayout());
		String[] title = { "글번호", "제목", "작성자", "작성일" };

		model = new DefaultTableModel(title, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
		table.addMouseListener(new TableEvent());
		JScrollPane sp = new JScrollPane(table);

		writeB = new JButton("글쓰기");
		modifyB = new JButton("수정");
		deleteB = new JButton("삭제");
		exitB = new JButton("종료");

		ButtonEvent evt = new ButtonEvent();
		writeB.addActionListener(evt);
		modifyB.addActionListener(evt);
		deleteB.addActionListener(evt);
		exitB.addActionListener(evt);

		JPanel southP2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		southP2.add(writeB);
		southP2.add(modifyB);
		southP2.add(deleteB);
		southP2.add(exitB);

		this.add("Center", sp);
		this.add("South", southP2);
		this.setLocation(((screenSize.width) / 2) - 300, ((screenSize.height) / 2) - 300);

	}

	// public static void main(String[] args) {
	// new SignNotice();
	//
	// }

	public class CloseEvent extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			clearTable();
			setVisible(false);
		}
	}

	public void clearTable() {
		int row = model.getRowCount();
		for (int i = 0; i < row; i++) {
			model.removeRow(0);
		}
	}

	class TableEvent extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 1) {
				int row = table.getSelectedRow();
				if (row == -1) {
					return;
				}
				nNum = (Integer) table.getValueAt(row, 0);
				title = (String) table.getValueAt(row, 1);
			} else if (e.getClickCount() == 2) {
				MainData data = new MainData();
				data.protocol = 4501;
				data.name = main.name;
				NoticeData temp = new NoticeData();
				temp.nNum = nNum;
				data.nData = temp;
				try {
					main.oos.writeObject(data);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			} else {
				return;
			}
		}
	}

	class ButtonEvent implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton target = (JButton) e.getSource();
			if (target == writeB) {
				if (main.crank.equals("과장") || main.crank.equals("부장")) {
					MainData data = new MainData();
					data.protocol = 4101;
					data.name = main.name;
					data.empno = main.empno;
					NoticeData temp = new NoticeData();
					temp.writeInfo = writeB.getText();
					data.nData = temp;
					System.out.println("-----글쓰기를 위한 empno 저장" + main.empno);
					System.out.println("-----글쓰기를 위한 name 저장" + main.name);
					try {
						main.oos.writeObject(data);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "부장 또는 과장 직급만 글작성이 가능합니다.");
				}
			} else if (target == modifyB) {
				if (main.crank.equals("과장") || main.crank.equals("부장")) {
					MainData data = new MainData();
					data.protocol = 4301;
					data.name = main.name;
					System.out.println("-----선택한 행" + nNum);
					NoticeData temp = new NoticeData();
					temp.nNum = nNum;
					temp.modifyInfo = modifyB.getText();
					data.nData = temp;
					try {
						main.oos.writeObject(data);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "부장 또는 과장 직급만 글수정이 가능합니다.");
				}
			} else if (target == deleteB) {
				if (main.crank.equals("과장") || main.crank.equals("부장")) {
					MainData data = new MainData();
					data.protocol = 4601;
					NoticeData temp = new NoticeData();
					temp.nNum = nNum;
					data.nData = temp;
					try {
						main.oos.writeObject(data);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				} else {
					JOptionPane.showMessageDialog(null, "부장 또는 과장 직급만 글삭제가 가능합니다.");
				}
			} else {
				clearTable();
				dispose();
			}
		}
	}
}

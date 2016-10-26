package SignClient.SignAttendance;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.synth.SynthScrollBarUI;
import javax.swing.table.DefaultTableModel;

import NetData.MainData;
import SignMainFrame.MainFrame;
import SignThread.SignClientThread;
import javautil.*;

public class AttendanceForm extends JDialog {
	public DefaultTableModel model;
	public JButton attendB, outB, earlyB, lateB, homeB, attendInfoB, attendSelfB;
	public Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public JLabel l1, l2, l3, l4, l5, v1, v2, v3, v4, v5;
	JTable table;
	JPanel p1;
	MyJDBC db;
	Connection con;
	PreparedStatement attendS, outS, earlyS, lateS, homeS;
	ResultSet rs;
	MainFrame main;
	JScrollPane sp;

	public AttendanceForm(MainFrame m) {
		super(m, "근태관리");
		main = m;
		this.addWindowListener(new CloseEvent());
		this.setResizable(false);

		String[] info = { "날짜", "시간", "형태" };
		model = new DefaultTableModel(info, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
		sp = new JScrollPane(table);

		attendInfoB = new JButton("전체 근태 정보");
		attendSelfB = new JButton("돌아가기");
		attendB = new JButton("출근");
		outB = new JButton("외근");
		earlyB = new JButton("조퇴");
		lateB = new JButton("야근");
		homeB = new JButton("퇴근");

		ButtonEvent evt = new ButtonEvent();
		attendB.addActionListener(evt);
		outB.addActionListener(evt);
		earlyB.addActionListener(evt);
		lateB.addActionListener(evt);
		homeB.addActionListener(evt);
		attendInfoB.addActionListener(evt);
		attendSelfB.addActionListener(evt);

		l1 = new JLabel("●사원정보");
		l2 = new JLabel("이름 :");
		l3 = new JLabel("사원번호 :");
		l4 = new JLabel("부서 :");
		l5 = new JLabel("직책 :");

		v1 = new JLabel("");
		v2 = new JLabel("");
		v3 = new JLabel("");
		v4 = new JLabel("");
		v5 = new JLabel("");

		JPanel p1 = new JPanel(new GridLayout(5, 1));
		p1.add(l1);
		p1.add(l2);
		p1.add(l3);
		p1.add(l4);
		p1.add(l5);

		JPanel p2 = new JPanel(new GridLayout(5, 1));
		p2.add(v1);
		p2.add(v2);
		p2.add(v3);
		p2.add(v4);
		p2.add(v5);

		JPanel p3 = new JPanel(new BorderLayout());
		p3.add("West", p1);
		p3.add("Center", p2);

		JPanel p4 = new JPanel(new GridLayout(7, 1));
		p4.add(attendInfoB);
		p4.add(attendSelfB);
		p4.add(attendB);
		p4.add(outB);
		p4.add(earlyB);
		p4.add(lateB);
		p4.add(homeB);

		JPanel p5 = new JPanel(new BorderLayout());
		p5.add("North", p3);
		p5.add("South", p4);

		JPanel p6 = new JPanel(new BorderLayout());
		p6.add("Center", sp);
		p6.add("East", p5);

		this.add(p6);
		this.setLocation(((screenSize.width) / 2) - 300, ((screenSize.height) / 2) - 300);

	}

	// public static void main(String[] args) {
	// new AttendanceForm();
	// }

	public void clearTable() {
		int row = model.getRowCount();
		for (int i = 0; i < row; i++) {
			model.removeRow(0);
		}
	}

	public class CloseEvent extends WindowAdapter {
		@Override
		public void windowClosing(WindowEvent e) {
			clearTable();
			dispose();
		}
	}

	class ButtonEvent implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JButton target = (JButton) e.getSource();
			if (target == attendInfoB) {
				MainData data = new MainData();
				data.protocol = 2222;
				data.empno = main.empno;
				data.cdept = main.cdept;
				try {
					main.oos.writeObject(data);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			} else if (target == attendSelfB) {
				MainData data = new MainData();
				data.protocol = 2000;
				data.empno = main.empno;
				try {
					main.oos.writeObject(data);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			} else if (target == attendB) {
				int kind = JOptionPane.showConfirmDialog(null, "출근도장 찍으시겠습니까?", "출근여부 확인", JOptionPane.YES_NO_OPTION);
				if (kind == JOptionPane.YES_OPTION) {
					MainData data = new MainData();
					data.protocol = 2101;
					data.empno = main.empno;

					try {
						main.oos.writeObject(data);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					attendB.setEnabled(false);
				} else {
					return;
				}
			} else if (target == outB) {
				int kind = JOptionPane.showConfirmDialog(null, "외근도장 찍으시겠습니까?", "외근여부 확인", JOptionPane.YES_NO_OPTION);
				if (kind == JOptionPane.YES_OPTION) {
					MainData data = new MainData();
					data.protocol = 2102;
					System.out.println("메인에 저장된 사원번호" + main.empno);
					data.empno = main.empno;
					try {
						main.oos.writeObject(data);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				} else {
					return;
				}
			} else if (target == earlyB) {
				int kind = JOptionPane.showConfirmDialog(null, "조퇴도장 찍으시겠습니까?", "조퇴여부 확인", JOptionPane.YES_NO_OPTION);
				if (kind == JOptionPane.YES_OPTION) {
					MainData data = new MainData();
					data.protocol = 2103;
					data.empno = main.empno;
					try {
						main.oos.writeObject(data);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					attendB.setEnabled(true);
					outB.setEnabled(true);
					earlyB.setEnabled(true);
					lateB.setEnabled(true);
					homeB.setEnabled(true);
				} else {
					return;
				}
			} else if (target == lateB) {
				int kind = JOptionPane.showConfirmDialog(null, "야근도장 찍으시겠습니까?", "야근여부 확인", JOptionPane.YES_NO_OPTION);
				if (kind == JOptionPane.YES_OPTION) {
					MainData data = new MainData();
					data.protocol = 2104;
					data.empno = main.empno;
					try {
						main.oos.writeObject(data);
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				} else {
					return;
				}
			} else if (target == homeB) {
				if (main.hour >= 18) {
					int kind = JOptionPane.showConfirmDialog(null, "퇴근도장 찍으시겠습니까?", "퇴근여부 확인",
							JOptionPane.YES_NO_OPTION);
					if (kind == JOptionPane.YES_OPTION) {
						MainData data = new MainData();
						data.protocol = 2105;
						data.empno = main.empno;
						try {
							main.oos.writeObject(data);
						} catch (Exception e2) {
							e2.printStackTrace();
						}
						attendB.setEnabled(true);
						outB.setEnabled(true);
						earlyB.setEnabled(true);
						lateB.setEnabled(true);
						homeB.setEnabled(true);
					} else {
						return;
					}
				} else {
					JOptionPane.showConfirmDialog(null, "18시 이후에만 가능합니다.", "할 일 다했나?", JOptionPane.DEFAULT_OPTION);
				}
			}

		}
	}
}

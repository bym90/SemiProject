package javautil;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

/*
 * JFrame�� ����� �� ���� ����ؼ� setDefaultClostOperatio()��
 * ����ؼ� ���� ó���� �ؾ��Ѵ�.
 */
import javax.swing.*;
public class CJFrame extends JFrame {
	public CJFrame() {
		this("");
	}
	public CJFrame(String title){
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}

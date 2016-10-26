package javautil;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

/*
 * JFrame을 사용할 때 마다 계속해서 setDefaultClostOperatio()을
 * 사용해서 종료 처리를 해야한다.
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

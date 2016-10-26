package javautil;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

public class EPanel2 extends JPanel{
	public int width;
	public int height;
	public EPanel2(int w, int h) {
		this.width = w;
		this.height = h;
		this.setOpaque(true);
//		this.setBackground(Color.white);
	}
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}
}

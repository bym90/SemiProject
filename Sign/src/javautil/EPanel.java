package javautil;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;

public class EPanel extends JPanel{
	public int width;
	public int height;
	public EPanel(int w, int h) {
		this.width = w;
		this.height = h;
		this.setOpaque(true);
		this.setBackground(Color.white);
	}
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}
}

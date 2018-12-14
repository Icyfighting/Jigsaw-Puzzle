package com.bjsxt.puzzle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PuzzlePiece extends JButton implements FocusListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int number;
	Color c = new Color(255, 245, 170);
	Font font = new Font("宋体", Font.BOLD, 12);

	PuzzlePiece(int number, String s) {
		super(s);
		//this.setIcon(icon);
		setBackground(c);
		setFont(font);
		this.number = number;
		c = getBackground();
		addFocusListener(this);
		setBorder(BorderFactory.createLineBorder(Color.gray));
	}

	public void focusGained(FocusEvent e) {// 获得焦点调用，到时候改成加一个边框之类的
		setBorder(BorderFactory.createLineBorder(Color.green));
	}

	public void focusLost(FocusEvent e) {// 失去焦点调用
		setBorder(BorderFactory.createLineBorder(Color.gray));
	}
}
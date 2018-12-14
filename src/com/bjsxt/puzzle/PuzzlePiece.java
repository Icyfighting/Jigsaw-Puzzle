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
	Font font = new Font("����", Font.BOLD, 12);

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

	public void focusGained(FocusEvent e) {// ��ý�����ã���ʱ��ĳɼ�һ���߿�֮���
		setBorder(BorderFactory.createLineBorder(Color.green));
	}

	public void focusLost(FocusEvent e) {// ʧȥ�������
		setBorder(BorderFactory.createLineBorder(Color.gray));
	}
}
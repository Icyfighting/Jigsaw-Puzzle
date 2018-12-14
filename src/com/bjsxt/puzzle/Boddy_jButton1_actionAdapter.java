package com.bjsxt.puzzle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Boddy_jButton1_actionAdapter implements ActionListener {
	private Puzzle adaptee;

	Boddy_jButton1_actionAdapter(Puzzle adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent eb) {
		adaptee.jButton1_actionPerformed(eb);
	}
}

class Boddy_jButton2_actionAdapter implements ActionListener {
	private Puzzle adaptee;

	Boddy_jButton2_actionAdapter(Puzzle adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent ea) {
		adaptee.jButton2_actionPerformed(ea);
	}
}


class Boddy_jButton3_actionAdapter implements ActionListener {
	private Puzzle adaptee;

	Boddy_jButton3_actionAdapter(Puzzle adaptee) {
		this.adaptee = adaptee;
	}

	public void actionPerformed(ActionEvent ea) {
		adaptee.jButton3_actionPerformed(ea);
	}
}


package com.bjsxt.puzzle;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

@SuppressWarnings({ "unchecked" })
public class Puzzle extends JFrame implements MouseListener, KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PuzzlePiece puzzle[] = new PuzzlePiece[25];
	Rectangle[] puzzleLocation = new Rectangle[25];// ��¼���ʱ��ÿ��button��getBounds
	JButton left, right, above, below;
	JButton restart = new JButton("���¿�ʼ");
	JButton level = new JButton("ѡ�񼶱�");
	JButton picture = new JButton("ѡ��ͼƬ");
	JButton showCount = new JButton("����ͳ��");
	JButton saveCount = new JButton("�����¼");
	Map<String, String> picMap = new HashMap<>();
	String picNum = "0";// ��Ҫѡ��ͼƬ�������ť�¼�������ѭ��֮��ġ�
	int picCount = 0;// ��¼ͼƬѡ��ť�������
	int levelCount = 0;// ��¼����ѡ��ť�������
	JLabel label = null;
	static File file = new File("SaveScore.txt");
	static TreeSet<User> set = new TreeSet<>(new UserCom());// Ϊ�����򣬻�����treeset,�ȽϹ�����ڱȽ�����
	JLabel rankLabel = new JLabel();
	JLabel rankShowLabel = new JLabel();
	long startTime = 0;// ��Ϸ��ʼʱ��
	long stopTime = 0;// ��Ϸ����ʱ��
	JButton congraButton = new JButton();
	JLabel picNameField = new JLabel();
	JLabel gameLevelField = new JLabel();
	JLabel moveCountField = new JLabel();
	JTextField saveNameField = new JTextField(10);
	int moveCount = 0;
	JLabel congratulation = new JLabel();
	JLabel hero = new JLabel();
	boolean alreadySave = false;

	private int gameLevel = 0;// ���崳�ؼ������ڿ����Ѷȡ�
	int ImageWidth;
	int ImageHeight;

	static {
		// ��ʼ��list���ݣ����ļ��ж�ȡ��
		if (file.exists()) {
			ObjectInputStream ois = null;
			try {
				ois = new ObjectInputStream(new FileInputStream(file));
				set = (TreeSet<User>) ois.readObject();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					ois.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	public Puzzle() {
		super("ƴͼ��Ϸ");
		init();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 10, 900, 700);
		setVisible(true);
		validate();
	}

	public void init() {
		startTime = System.currentTimeMillis();
		getContentPane().setLayout(null);
		getContentPane().add(restart);// getContentPane()�õ�һ�����ݻ���
		restart.setBounds(40, 580, 100, 40); // ������ť����С�Ѿ��Ĺ�
		restart.addActionListener(new Boddy_jButton1_actionAdapter(this));

		getContentPane().add(level);// getContentPane()�õ�һ�����ݻ���
		level.setBounds(40, 370, 100, 40); // ������ť����С�Ѿ��Ĺ�
		level.addActionListener(new Boddy_jButton2_actionAdapter(this));

		getContentPane().add(picture);// getContentPane()�õ�һ�����ݻ���
		picture.setBounds(40, 300, 100, 40); // ������ť����С�Ѿ��Ĺ�
		picture.addActionListener(new Boddy_jButton3_actionAdapter(this));

		getContentPane().add(showCount);// getContentPane()�õ�һ�����ݻ���
		showCount.setBounds(40, 440, 100, 40); // ������ť����С�Ѿ��Ĺ�



		// ��ʼ����������ѡ�����ϷͼƬ
		picMap.put("0", "img/1.jpg");
		picMap.put("1", "img/2.jpg");
		picMap.put("2", "img/3.jpg");
		picMap.put("3", "img/4.jpg");
		picMap.put("4", "img/5.jpg");
		picMap.put("5", "img/stat5.jpg");
		/*
		 * ���ͼ��Ĳ�����ͼƬ���ʼ��
		 */

		BufferedImage buf = null;
		BufferedImage bufCut = null;
		BufferedImage bufnew = null;
		BufferedImage reDraw = null;
		ImageIcon icon = null;

		try {
			buf = ImageIO.read(new File(picMap.get(picNum)));// ��ȡ�ļ�ͼ��
			bufCut = buf.getSubimage(0, 0, 600, 600);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int num = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (num < puzzle.length) {
					bufnew = bufCut.getSubimage(100 * j, 100 * i, 200, 200);
					icon = new ImageIcon(bufnew);// ��ͼ��ת����ͼ��
					puzzle[num] = new PuzzlePiece(num, String.valueOf(num));// ���ͼ�굽ÿһ��BUTTON��ť����
					puzzle[num].setIcon(icon);
				} else// ʹ���һ��ͼ��Ϊ�հ�ͼ��
				{
					icon = new ImageIcon("img/stat2.jpg");// һ�ſհ�ͼ��
				}

				num++;
			}
		}
		// �������ͼ
		reDraw = reDraw(bufCut);
		icon = new ImageIcon(reDraw);
		label = new JLabel(icon);
		label.setBounds(40, 40, 230, 230);
		this.add(label);

		// ������ʾ��
		gameLevelField.setBounds(170, 370, 100, 40);
		picNameField.setBounds(170, 300, 100, 40);
		moveCountField.setBounds(170, 440, 100, 40);
		saveNameField.setBounds(40, 510, 100, 40);
		this.add(gameLevelField);
		this.add(picNameField);
		this.add(moveCountField);
		this.add(saveNameField);
		picNameField.setText(picMap.get(picNum));
		gameLevelField.setText(String.valueOf(gameLevel));
		moveCountField.setText(String.valueOf(moveCount));
		saveNameField.setText("����������");

		rankLabel.setBounds(170, 510, 150, 40);
		rankLabel.setFont(new Font("Serif", Font.BOLD, 15));
		getContentPane().add(rankLabel);

		rankShowLabel.setBounds(400, 10, 400, 80);
		rankShowLabel.setFont(new Font("����", Font.BOLD, 16));
		rankShowLabel.setForeground(Color.DARK_GRAY);
		getContentPane().add(rankShowLabel);

		rankShowLabel.setText(showRank().toString());

		congratulation.setBounds(450, 100, 400, 300);
		congratulation.setFont(new Font("����", Font.BOLD, 25));
		congratulation.setForeground(Color.RED);
		getContentPane().add(congratulation);
		
		hero.setBounds(350, 0, 40, 100);
		hero.setFont(new Font("����", Font.BOLD, 22));
		hero.setForeground(Color.black);
		getContentPane().add(hero);
		hero.setText("<html>Ӣ<br>��<br>��<html>");


		for (int i = 0; i < 24; i++) {
			this.add(puzzle[i]);// ��ÿһ����ť��ӵ���ǰ����������
			if (i < puzzle.length - 1)
				puzzle[i].addMouseListener(this);// �հ׸���Ӽ�������
		}

		int side = 100;
		int puzzleNum = 0;

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (puzzleNum < 24) {
					puzzle[puzzleNum].setBounds(350 + 100 * j, 100 + 100 * i, side, side);
					puzzleLocation[puzzleNum] = puzzle[puzzleNum].getBounds();
					// System.out.println("����puzzleLocation=
					// "+puzzleLocation[puzzleNum]);
				}
				puzzleNum++;
			}
		}
		puzzle[24].requestFocus();

		left = new JButton();
		right = new JButton();
		above = new JButton();
		below = new JButton();
		getContentPane().add(left);
		getContentPane().add(right);
		getContentPane().add(above);
		getContentPane().add(below);
		left.setBounds(345, 95, 5, 510); // ����ı߿��Ѿ��Ĺ�
		right.setBounds(850, 95, 5, 510);
		above.setBounds(350, 95, 500, 5);
		below.setBounds(350, 600, 500, 5);
		validate();
	}

	public void initPicture() {
		BufferedImage buf = null;
		BufferedImage bufCut = null;
		BufferedImage bufnew = null;
		BufferedImage reDraw = null;
		ImageIcon icon = null;
		try {
			// buf = ImageIO.read(new File("img/stat3.jpg"));// ��ȡ�ļ�ͼ��
			buf = ImageIO.read(new File(picMap.get(picNum)));// ��ȡ�ļ�ͼ��
			bufCut = buf.getSubimage(0, 0, 600, 600);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int num = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				num = i * 5 + j;// ��ʾ��ǰ���ͼ�������id���������е��±�
				if (num < 24) {
					bufnew = bufCut.getSubimage(100 * j, 100 * i, 200, 200);
					icon = new ImageIcon(bufnew);// ��ͼ��ת����ͼ��
					puzzle[num].setIcon(icon);
				} else// ʹ���һ��ͼ��Ϊ�հ�ͼ��
				{
					icon = new ImageIcon("img/stat2.jpg");// һ�ſհ�ͼ��
				}

			}
		}
		// ����ͼ
		remove(label);
		repaint();

		reDraw = reDraw(bufCut);
		icon = new ImageIcon(reDraw);
		label = new JLabel(icon);
		label.setBounds(40, 40, 230, 230);
		this.add(label);

	}

	public void keyTyped(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		// moveCountField.setText(String.valueOf(moveCount));
		PuzzlePiece piece = (PuzzlePiece) e.getSource();
		/*int x = -1, y = -1;
		x = e.getX();
		y = e.getY();
		int w = piece.getBounds().width;
		int h = piece.getBounds().height;*/
			go(piece);
		

		

	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void go(PuzzlePiece piece) {
		boolean move = true;
		Rectangle pieceRect = piece.getBounds();
		int x = piece.getBounds().x;
		int y = piece.getBounds().y;

		pieceRect.setLocation(x, y);// �ж��Ƿ�����ߵ�����
		Rectangle belowRect = below.getBounds();// ��ȡ�������ұ߽�ķ�Χ
		Rectangle aboveRect = above.getBounds();// ��ȡ�������ұ߽�ķ�Χ
		Rectangle leftRect = left.getBounds();// ��ȡ�������ұ߽�ķ�Χ
		Rectangle rightRect = right.getBounds();// ��ȡ�������ұ߽�ķ�Χ
		for (int k = 0; k < 24; k++) {
			Rectangle puzzleRect = puzzle[k].getBounds();
			if ((pieceRect.intersects(puzzleRect)) && (piece.number != k))
				move = false;
		}//�ཻ���ԣ����ǲ��ܺͱ���ཻ�Ŀ���ͬһ��λ��
		if (pieceRect.intersects(directionRect))//�����߽磬�����ƶ�
			move = false;
		if (move == true) {
			piece.setLocation(x, y);
			moveCount++;
			moveCountField.setText(String.valueOf(moveCount));
			if (isComplete() == 1&& alreadySave == false) {
				autoSaveScore();
			}
		}
	}


	// restart��ť���ܣ�����init()����
	public void jButton1_actionPerformed(ActionEvent eb) {
		// ��ť1����
		// init();
		dispose();
		new Puzzle();
		moveCount = 0;
		startTime = System.currentTimeMillis();
		moveCountField.setText(String.valueOf(moveCount));
		congratulation.setText("");
		// puzzle[0].setBorder(BorderFactory.createLineBorder(Color.red));
	}

	public void jButton2_actionPerformed(ActionEvent ea) {
		// ��ť2�Ķ��� �ı��Ѷ�
		gameLevel = ((levelCount++) % 3) + 1;// ����1��2��3��������0��1��2
		outOfOrder(gameLevel);
		gameLevelField.setText(String.valueOf(gameLevel));
		moveCount = 0;
		System.currentTimeMillis();
		moveCountField.setText(String.valueOf(moveCount));
		startTime = System.currentTimeMillis();
		congratulation.setText("");
		alreadySave = false;
	}

	// ѡ��ͼƬ�Ĺ���
	public void jButton3_actionPerformed(ActionEvent ea) {
		// ��ť3�Ķ���,�ı�ͼƬ
		picCount++;
		picCount %= 5;
		picNum = Integer.valueOf(picCount).toString();
		initPicture();
		// System.out.println(picNum);
		picNameField.setText(picMap.get(picNum));
		congratulation.setText("");
	}


	// ����ͼƬ����

	// �������ɲ�ͬ�̶ȵ����ҽ����
	public void outOfOrder(int level) {
		switch (level) {
		case 1:
			levelOne1();
			break;
		case 2:
			levelTwo();
			break;

		case 3:
			levelThree();
			break;
		default:
			levelOne();
			break;
		}

	}
	//�����õ��Ѷ�ѡ��
	private void levelOne1() {
		// TODO Auto-generated method stub
		int side = 100;
		puzzle[0].setBounds(350 + 100 * 0, 100 + 100 * 0, side, side);//
		puzzle[1].setBounds(350 + 100 * 1, 100 + 100 * 0, side, side);//
		puzzle[2].setBounds(350 + 100 * 2, 100 + 100 * 0, side, side);//
		puzzle[3].setBounds(350 + 100 * 3, 100 + 100 * 0, side, side);//
		puzzle[4].setBounds(350 + 100 * 4, 100 + 100 * 0, side, side);//
		puzzle[5].setBounds(350 + 100 * 0, 100 + 100 * 1, side, side);//
		puzzle[6].setBounds(350 + 100 * 1, 100 + 100 * 1, side, side);//
		puzzle[7].setBounds(350 + 100 * 2, 100 + 100 * 1, side, side);//
		puzzle[8].setBounds(350 + 100 * 3, 100 + 100 * 1, side, side);//
		puzzle[9].setBounds(350 + 100 * 4, 100 + 100 * 1, side, side);//
		puzzle[10].setBounds(350 + 100 * 0, 100 + 100 * 2, side, side);//
		puzzle[11].setBounds(350 + 100 * 1, 100 + 100 * 2, side, side);//
		puzzle[12].setBounds(350 + 100 * 2, 100 + 100 * 2, side, side);//
		puzzle[13].setBounds(350 + 100 * 3, 100 + 100 * 2, side, side);//
		puzzle[14].setBounds(350 + 100 * 4, 100 + 100 * 2, side, side);//
		puzzle[15].setBounds(350 + 100 * 0, 100 + 100 * 3, side, side);//
		puzzle[16].setBounds(350 + 100 * 1, 100 + 100 * 3, side, side);//
		puzzle[17].setBounds(350 + 100 * 2, 100 + 100 * 3, side, side);//
		puzzle[18].setBounds(350 + 100 * 3, 100 + 100 * 4, side, side);//
		puzzle[19].setBounds(350 + 100 * 3, 100 + 100 * 3, side, side);//
		puzzle[20].setBounds(350 + 100 * 0, 100 + 100 * 4, side, side);//
		puzzle[21].setBounds(350 + 100 * 1, 100 + 100 * 4, side, side);//
		puzzle[22].setBounds(350 + 100 * 2, 100 + 100 * 4, side, side);//
		puzzle[23].setBounds(350 + 100 * 4, 100 + 100 * 3, side, side);//

	}

	private void levelOne() {
		// TODO Auto-generated method stub
		int side = 100;
		puzzle[0].setBounds(350 + 100 * 0, 100 + 100 * 1, side, side);//
		puzzle[1].setBounds(350 + 100 * 0, 100 + 100 * 0, side, side);//
		puzzle[2].setBounds(350 + 100 * 1, 100 + 100 * 0, side, side);//
		puzzle[3].setBounds(350 + 100 * 3, 100 + 100 * 0, side, side);//
		puzzle[4].setBounds(350 + 100 * 4, 100 + 100 * 0, side, side);//
		puzzle[5].setBounds(350 + 100 * 1, 100 + 100 * 1, side, side);//
		puzzle[6].setBounds(350 + 100 * 1, 100 + 100 * 2, side, side);//
		puzzle[7].setBounds(350 + 100 * 2, 100 + 100 * 0, side, side);//
		puzzle[8].setBounds(350 + 100 * 2, 100 + 100 * 1, side, side);//
		puzzle[9].setBounds(350 + 100 * 4, 100 + 100 * 1, side, side);//
		puzzle[10].setBounds(350 + 100 * 0, 100 + 100 * 2, side, side);//
		puzzle[11].setBounds(350 + 100 * 3, 100 + 100 * 2, side, side);//
		puzzle[12].setBounds(350 + 100 * 2, 100 + 100 * 2, side, side);//
		puzzle[13].setBounds(350 + 100 * 3, 100 + 100 * 1, side, side);//
		puzzle[14].setBounds(350 + 100 * 4, 100 + 100 * 2, side, side);//
		puzzle[15].setBounds(350 + 100 * 1, 100 + 100 * 3, side, side);//
		puzzle[16].setBounds(350 + 100 * 0, 100 + 100 * 4, side, side);//
		puzzle[17].setBounds(350 + 100 * 3, 100 + 100 * 4, side, side);//
		puzzle[18].setBounds(350 + 100 * 2, 100 + 100 * 4, side, side);//
		puzzle[19].setBounds(350 + 100 * 3, 100 + 100 * 3, side, side);//
		puzzle[20].setBounds(350 + 100 * 0, 100 + 100 * 3, side, side);//
		puzzle[21].setBounds(350 + 100 * 1, 100 + 100 * 4, side, side);//
		puzzle[22].setBounds(350 + 100 * 2, 100 + 100 * 3, side, side);//
		puzzle[23].setBounds(350 + 100 * 4, 100 + 100 * 3, side, side);//

	}

	private void levelTwo() {
		int side = 100;
		puzzle[0].setBounds(350 + 100 * 1, 100 + 100 * 0, side, side);
		puzzle[1].setBounds(350 + 100 * 1, 100 + 100 * 2, side, side);
		puzzle[2].setBounds(350 + 100 * 4, 100 + 100 * 0, side, side);
		puzzle[3].setBounds(350 + 100 * 2, 100 + 100 * 3, side, side);
		puzzle[4].setBounds(350 + 100 * 4, 100 + 100 * 1, side, side);
		puzzle[5].setBounds(350 + 100 * 0, 100 + 100 * 1, side, side);
		puzzle[6].setBounds(350 + 100 * 0, 100 + 100 * 0, side, side);
		puzzle[7].setBounds(350 + 100 * 0, 100 + 100 * 2, side, side);
		puzzle[8].setBounds(350 + 100 * 2, 100 + 100 * 0, side, side);
		puzzle[9].setBounds(350 + 100 * 3, 100 + 100 * 0, side, side);
		puzzle[10].setBounds(350 + 100 * 0, 100 + 100 * 3, side, side);
		puzzle[11].setBounds(350 + 100 * 3, 100 + 100 * 1, side, side);
		puzzle[12].setBounds(350 + 100 * 2, 100 + 100 * 1, side, side);
		puzzle[13].setBounds(350 + 100 * 2, 100 + 100 * 2, side, side);
		puzzle[14].setBounds(350 + 100 * 4, 100 + 100 * 2, side, side);
		puzzle[15].setBounds(350 + 100 * 0, 100 + 100 * 4, side, side);
		puzzle[16].setBounds(350 + 100 * 2, 100 + 100 * 4, side, side);
		puzzle[17].setBounds(350 + 100 * 1, 100 + 100 * 1, side, side);
		puzzle[18].setBounds(350 + 100 * 3, 100 + 100 * 3, side, side);
		puzzle[19].setBounds(350 + 100 * 4, 100 + 100 * 3, side, side);
		puzzle[20].setBounds(350 + 100 * 1, 100 + 100 * 4, side, side);
		puzzle[21].setBounds(350 + 100 * 1, 100 + 100 * 3, side, side);
		puzzle[22].setBounds(350 + 100 * 3, 100 + 100 * 4, side, side);
		puzzle[23].setBounds(350 + 100 * 3, 100 + 100 * 2, side, side);

	}

	private void levelThree() {
		int side = 100;
		puzzle[0].setBounds(350 + 100 * 2, 100 + 100 * 0, side, side);
		puzzle[1].setBounds(350 + 100 * 1, 100 + 100 * 3, side, side);
		puzzle[2].setBounds(350 + 100 * 4, 100 + 100 * 0, side, side);
		puzzle[3].setBounds(350 + 100 * 1, 100 + 100 * 4, side, side);
		puzzle[4].setBounds(350 + 100 * 3, 100 + 100 * 1, side, side);
		puzzle[5].setBounds(350 + 100 * 0, 100 + 100 * 2, side, side);
		puzzle[6].setBounds(350 + 100 * 0, 100 + 100 * 1, side, side);
		puzzle[7].setBounds(350 + 100 * 2, 100 + 100 * 2, side, side);
		puzzle[8].setBounds(350 + 100 * 3, 100 + 100 * 0, side, side);
		puzzle[9].setBounds(350 + 100 * 2, 100 + 100 * 4, side, side);
		puzzle[10].setBounds(350 + 100 * 0, 100 + 100 * 4, side, side);
		puzzle[11].setBounds(350 + 100 * 1, 100 + 100 * 1, side, side);
		puzzle[12].setBounds(350 + 100 * 2, 100 + 100 * 1, side, side);
		puzzle[13].setBounds(350 + 100 * 3, 100 + 100 * 3, side, side);
		puzzle[14].setBounds(350 + 100 * 4, 100 + 100 * 2, side, side);
		puzzle[15].setBounds(350 + 100 * 0, 100 + 100 * 0, side, side);
		puzzle[16].setBounds(350 + 100 * 2, 100 + 100 * 3, side, side);
		puzzle[17].setBounds(350 + 100 * 1, 100 + 100 * 0, side, side);
		puzzle[18].setBounds(350 + 100 * 3, 100 + 100 * 4, side, side);
		puzzle[19].setBounds(350 + 100 * 4, 100 + 100 * 3, side, side);
		puzzle[20].setBounds(350 + 100 * 0, 100 + 100 * 3, side, side);
		puzzle[21].setBounds(350 + 100 * 4, 100 + 100 * 1, side, side);
		puzzle[22].setBounds(350 + 100 * 1, 100 + 100 * 2, side, side);
		puzzle[23].setBounds(350 + 100 * 3, 100 + 100 * 2, side, side);

	}

	public BufferedImage reDraw(BufferedImage img) {

		BufferedImage reDraw = new BufferedImage(230, 230, BufferedImage.TYPE_INT_RGB);
		reDraw.getGraphics().drawImage(img, 0, 0, 230, 230, null);
		return reDraw;
	}

	// �ж�ƴͼ�Ƿ���ɵĹ���
	public int isComplete() {
		int flag = 1;
		if (gameLevel != 0) {// ֤������������������
			for (int i = 0; i < puzzle.length - 1; i++) {

				if (!puzzle[i].getBounds().equals(puzzleLocation[i])) {

					flag = -1;

				}
			}
		} else {// ��ʵ�����Ƿ�����ж���ɡ�
			flag = -2;
		}
		return flag;
	}

	public StringBuilder showRank() {
		int count = 0;
		StringBuilder rankString = new StringBuilder("<html>");
		String nameTemp = null;
		// �����������Ӣ�۰�
		for (User u : set) {
			if(u.getName().equals("����������")){
				nameTemp= "����";
			}else{
				nameTemp = u.getName();
			}
			if (count < 3) {
				count++;
				rankString.append("��"+count + "��:"+nameTemp+"&nbsp;������" + u.getMoveCount() + "&nbsp;����" + u.getGameLevel() + "&nbsp;��ʱ��"
						+ (int) u.getGameTime() / 1000 + "s<br>");
			}
		}
		return rankString;
	}

	public void autoSaveScore() {
		// Ҫ��������Ϣ��������
		alreadySave = true;
		String name = saveNameField.getText();
		stopTime = System.currentTimeMillis();
		long gameTime = stopTime - startTime;
		User user = new User(name, moveCount, new Date(), gameLevel, gameTime);// ��Ҫ������Ϸ��ʱ�Ĳ���
		set.add(user);
		// System.out.println(user);
		int rank = 0;

		// д���ļ�
		ObjectOutputStream oos = null;

		try {
			oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(set);
			for (User u : set) {
				// System.out.println("����set��¼��"+u);
				if (u.equals(user)) {
					rankLabel.setText("����ǰ����Ϊ��" + (rank + 1));
					// System.out.println("����ǰ�ɼ�Ϊ:"+user);
					break;
					// System.out.println("���������ǵڼ�����"+rank);
				} else {
					rank++;
				}
			}

			rankShowLabel.setText(showRank().toString());
			StringBuilder conString = new StringBuilder("<html>��ϲ����ɣ�    ");
			conString.append("�ɼ�Ϊ<br>������" + user.getMoveCount() + "\t����" + user.getGameLevel() + "\t��ʱ��"
					+ (int) user.getGameTime() / 1000 + "s");
			conString.append("<br>��ǰ����Ϊ:" + (rank + 1));
			congratulation.setText(conString.toString());
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		finally {
			try {
				oos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}

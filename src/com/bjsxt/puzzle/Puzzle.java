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
	Rectangle[] puzzleLocation = new Rectangle[25];// 记录完成时候每个button的getBounds
	JButton left, right, above, below;
	JButton restart = new JButton("重新开始");
	JButton level = new JButton("选择级别");
	JButton picture = new JButton("选择图片");
	JButton showCount = new JButton("步数统计");
	JButton saveCount = new JButton("保存记录");
	Map<String, String> picMap = new HashMap<>();
	String picNum = "0";// 需要选择图片，点击按钮事件，计数循环之类的。
	int picCount = 0;// 记录图片选择按钮点击次数
	int levelCount = 0;// 记录级别选择按钮点击次数
	JLabel label = null;
	static File file = new File("SaveScore.txt");
	static TreeSet<User> set = new TreeSet<>(new UserCom());// 为了排序，还是用treeset,比较规则放在比较器中
	JLabel rankLabel = new JLabel();
	JLabel rankShowLabel = new JLabel();
	long startTime = 0;// 游戏开始时间
	long stopTime = 0;// 游戏结束时间
	JButton congraButton = new JButton();
	JLabel picNameField = new JLabel();
	JLabel gameLevelField = new JLabel();
	JLabel moveCountField = new JLabel();
	JTextField saveNameField = new JTextField(10);
	int moveCount = 0;
	JLabel congratulation = new JLabel();
	JLabel hero = new JLabel();
	boolean alreadySave = false;

	private int gameLevel = 0;// 定义闯关级别，用于控制难度。
	int ImageWidth;
	int ImageHeight;

	static {
		// 初始化list内容，从文件中读取。
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
		super("拼图游戏");
		init();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(200, 10, 900, 700);
		setVisible(true);
		validate();
	}

	public void init() {
		startTime = System.currentTimeMillis();
		getContentPane().setLayout(null);
		getContentPane().add(restart);// getContentPane()得到一个内容画板
		restart.setBounds(40, 580, 100, 40); // 重启按钮，大小已经改过
		restart.addActionListener(new Boddy_jButton1_actionAdapter(this));

		getContentPane().add(level);// getContentPane()得到一个内容画板
		level.setBounds(40, 370, 100, 40); // 重启按钮，大小已经改过
		level.addActionListener(new Boddy_jButton2_actionAdapter(this));

		getContentPane().add(picture);// getContentPane()得到一个内容画板
		picture.setBounds(40, 300, 100, 40); // 重启按钮，大小已经改过
		picture.addActionListener(new Boddy_jButton3_actionAdapter(this));

		getContentPane().add(showCount);// getContentPane()得到一个内容画板
		showCount.setBounds(40, 440, 100, 40); // 重启按钮，大小已经改过



		// 初始化几个可以选择的游戏图片
		picMap.put("0", "img/1.jpg");
		picMap.put("1", "img/2.jpg");
		picMap.put("2", "img/3.jpg");
		picMap.put("3", "img/4.jpg");
		picMap.put("4", "img/5.jpg");
		picMap.put("5", "img/stat5.jpg");
		/*
		 * 完成图像的操作级图片块初始化
		 */

		BufferedImage buf = null;
		BufferedImage bufCut = null;
		BufferedImage bufnew = null;
		BufferedImage reDraw = null;
		ImageIcon icon = null;

		try {
			buf = ImageIO.read(new File(picMap.get(picNum)));// 读取文件图像
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
					icon = new ImageIcon(bufnew);// 将图像转化成图标
					puzzle[num] = new PuzzlePiece(num, String.valueOf(num));// 添加图标到每一个BUTTON按钮上面
					puzzle[num].setIcon(icon);
				} else// 使最后一张图像为空白图像
				{
					icon = new ImageIcon("img/stat2.jpg");// 一张空白图像
				}

				num++;
			}
		}
		// 添加缩略图
		reDraw = reDraw(bufCut);
		icon = new ImageIcon(reDraw);
		label = new JLabel(icon);
		label.setBounds(40, 40, 230, 230);
		this.add(label);

		// 增加显示框
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
		saveNameField.setText("输入姓名：");

		rankLabel.setBounds(170, 510, 150, 40);
		rankLabel.setFont(new Font("Serif", Font.BOLD, 15));
		getContentPane().add(rankLabel);

		rankShowLabel.setBounds(400, 10, 400, 80);
		rankShowLabel.setFont(new Font("黑体", Font.BOLD, 16));
		rankShowLabel.setForeground(Color.DARK_GRAY);
		getContentPane().add(rankShowLabel);

		rankShowLabel.setText(showRank().toString());

		congratulation.setBounds(450, 100, 400, 300);
		congratulation.setFont(new Font("黑体", Font.BOLD, 25));
		congratulation.setForeground(Color.RED);
		getContentPane().add(congratulation);
		
		hero.setBounds(350, 0, 40, 100);
		hero.setFont(new Font("黑体", Font.BOLD, 22));
		hero.setForeground(Color.black);
		getContentPane().add(hero);
		hero.setText("<html>英<br>雄<br>榜<html>");


		for (int i = 0; i < 24; i++) {
			this.add(puzzle[i]);// 将每一个按钮添加到当前这个面板上面
			if (i < puzzle.length - 1)
				puzzle[i].addMouseListener(this);// 空白格不添加监听机制
		}

		int side = 100;
		int puzzleNum = 0;

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (puzzleNum < 24) {
					puzzle[puzzleNum].setBounds(350 + 100 * j, 100 + 100 * i, side, side);
					puzzleLocation[puzzleNum] = puzzle[puzzleNum].getBounds();
					// System.out.println("测试puzzleLocation=
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
		left.setBounds(345, 95, 5, 510); // 滑块的边框，已经改过
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
			// buf = ImageIO.read(new File("img/stat3.jpg"));// 读取文件图像
			buf = ImageIO.read(new File(picMap.get(picNum)));// 读取文件图像
			bufCut = buf.getSubimage(0, 0, 600, 600);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int num = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				num = i * 5 + j;// 表示当前这个图像的坐标id，在数组中的下标
				if (num < 24) {
					bufnew = bufCut.getSubimage(100 * j, 100 * i, 200, 200);
					icon = new ImageIcon(bufnew);// 将图像转化成图标
					puzzle[num].setIcon(icon);
				} else// 使最后一张图像为空白图像
				{
					icon = new ImageIcon("img/stat2.jpg");// 一张空白图像
				}

			}
		}
		// 缩略图
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

		pieceRect.setLocation(x, y);// 判断是否可以走的条件
		Rectangle belowRect = below.getBounds();// 获取上下左右边界的范围
		Rectangle aboveRect = above.getBounds();// 获取上下左右边界的范围
		Rectangle leftRect = left.getBounds();// 获取上下左右边界的范围
		Rectangle rightRect = right.getBounds();// 获取上下左右边界的范围
		for (int k = 0; k < 24; k++) {
			Rectangle puzzleRect = puzzle[k].getBounds();
			if ((pieceRect.intersects(puzzleRect)) && (piece.number != k))
				move = false;
		}//相交可以，但是不能和别的相交的块是同一个位置
		if (pieceRect.intersects(directionRect))//超过边界，不能移动
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


	// restart按钮功能，调用init()方法
	public void jButton1_actionPerformed(ActionEvent eb) {
		// 按钮1动作
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
		// 按钮2的动作 改变难度
		gameLevel = ((levelCount++) % 3) + 1;// 就是1，2，3，而不是0，1，2
		outOfOrder(gameLevel);
		gameLevelField.setText(String.valueOf(gameLevel));
		moveCount = 0;
		System.currentTimeMillis();
		moveCountField.setText(String.valueOf(moveCount));
		startTime = System.currentTimeMillis();
		congratulation.setText("");
		alreadySave = false;
	}

	// 选择图片的功能
	public void jButton3_actionPerformed(ActionEvent ea) {
		// 按钮3的动作,改变图片
		picCount++;
		picCount %= 5;
		picNum = Integer.valueOf(picCount).toString();
		initPicture();
		// System.out.println(picNum);
		picNameField.setText(picMap.get(picNum));
		congratulation.setText("");
	}


	// 保存图片功能

	// 用于生成不同程度的扰乱结果。
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
	//测试用低难度选项
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

	// 判断拼图是否完成的功能
	public int isComplete() {
		int flag = 1;
		if (gameLevel != 0) {// 证明乱序过，否则不算完成
			for (int i = 0; i < puzzle.length - 1; i++) {

				if (!puzzle[i].getBounds().equals(puzzleLocation[i])) {

					flag = -1;

				}
			}
		} else {// 先实验下是否可以判断完成。
			flag = -2;
		}
		return flag;
	}

	public StringBuilder showRank() {
		int count = 0;
		StringBuilder rankString = new StringBuilder("<html>");
		String nameTemp = null;
		// 单独用来输出英雄榜
		for (User u : set) {
			if(u.getName().equals("输入姓名：")){
				nameTemp= "匿名";
			}else{
				nameTemp = u.getName();
			}
			if (count < 3) {
				count++;
				rankString.append("第"+count + "名:"+nameTemp+"&nbsp;步数：" + u.getMoveCount() + "&nbsp;级别：" + u.getGameLevel() + "&nbsp;耗时："
						+ (int) u.getGameTime() / 1000 + "s<br>");
			}
		}
		return rankString;
	}

	public void autoSaveScore() {
		// 要求输入信息，并保存
		alreadySave = true;
		String name = saveNameField.getText();
		stopTime = System.currentTimeMillis();
		long gameTime = stopTime - startTime;
		User user = new User(name, moveCount, new Date(), gameLevel, gameTime);// 还要增加游戏耗时的参数
		set.add(user);
		// System.out.println(user);
		int rank = 0;

		// 写入文件
		ObjectOutputStream oos = null;

		try {
			oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(set);
			for (User u : set) {
				// System.out.println("遍历set记录："+u);
				if (u.equals(user)) {
					rankLabel.setText("您当前排名为：" + (rank + 1));
					// System.out.println("您当前成绩为:"+user);
					break;
					// System.out.println("测试现在是第几名："+rank);
				} else {
					rank++;
				}
			}

			rankShowLabel.setText(showRank().toString());
			StringBuilder conString = new StringBuilder("<html>恭喜您完成！    ");
			conString.append("成绩为<br>步数：" + user.getMoveCount() + "\t级别：" + user.getGameLevel() + "\t耗时："
					+ (int) user.getGameTime() / 1000 + "s");
			conString.append("<br>当前排名为:" + (rank + 1));
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

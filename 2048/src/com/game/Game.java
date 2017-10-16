package com.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JPanel{
	
	private static final long serialVersionUID = 1L;

	public static int WIDTH = 800, HEIGHT = 600, imgdim = 105, keycheck = 0,alt = 0, changer = 0;
	
	public BufferedImage blank,two,four,eight,teen,thirty,sixty,onetwenty, twofifty,fiveh,onek,twok;
	
	public int[] num, testnum;
	
	public int score = 0, highscore = 0, autooffset =1, avgscore, avgnum;
	
	public String text = "   2048",caption1 = "",caption2 = "",autotoggle = "p to toggle AI",autotype = "Top Right";
	
	public Font title, scoref;
	
	public boolean lose = false,auto = false;
	
	Random r;
	
	public Game(){
		addKeyListener(new KeyStuff());
		setFocusable(true);
		
		try {
			blank = ImageIO.read(Game.class.getResource("/blank.png"));
			two = ImageIO.read(Game.class.getResource("/2.png"));
			four = ImageIO.read(Game.class.getResource("/4.png"));
			eight = ImageIO.read(Game.class.getResource("/8.png"));
			teen = ImageIO.read(Game.class.getResource("/16.png"));
			thirty = ImageIO.read(Game.class.getResource("/32.png"));
			sixty = ImageIO.read(Game.class.getResource("/64.png"));
			onetwenty = ImageIO.read(Game.class.getResource("/128.png"));
			twofifty = ImageIO.read(Game.class.getResource("/256.png"));
			fiveh = ImageIO.read(Game.class.getResource("/512.png"));
			onek = ImageIO.read(Game.class.getResource("/1024.png"));
			twok = ImageIO.read(Game.class.getResource("/2048.png"));
		} catch (IOException e) {
			System.err.println("crash");
			//System.exit(0);
			e.printStackTrace();
		}
		
		num = new int[16];
		testnum= new int[16];
		
		title = new Font("TimesNewRoman",0,60);
		scoref = new Font("TimesNewRoman", 0 , 36);
		
		r = new Random();
		
		add();
		add();
		score = 0;
		
		//file = new File("Res/highscore");
		highscore = gethighscore();
		
	}
	
	public void run(){
		long current = 0, lastl = 0,lasts = 0;
		while(true){
			current = System.currentTimeMillis();
			
			if(current-lastl>=1){
				
				if(!moveright(false,num)&&!moveleft(false,num)&&!moveup(false,num)&&!movedown(false,num)){
					//temp
					//repaint();
					lose = true;
					//text = "You Lose!";
					caption1 = "ctrl + r to restart";
					caption2 = "";
					//auto = false;
					autotoggle = "";
					avgscore = avgscore*avgnum + score;
					avgnum++;
					avgscore = avgscore / avgnum;
					System.out.println(avgscore + " || " + score);
					reset();
					repaint();
				}
				
				lastl = current;
			}
			if(current-lasts>=autooffset&&auto&&autotype == "Top Right"){
				lasts = current;
				
				AITopRight();
				
			}
			if(current-lasts>=autooffset&&auto&&autotype == "Best Score"){
				lasts = current;
				
				AIBestScore();
				
			}
			if(current-lasts>=autooffset&&auto&&autotype == "Empty Spaces"){
				lasts = current;
				
				AITopRight();
				
			}
			if(current-lasts>=autooffset&&auto&&autotype == "Space n' Score"){
				lasts = current;
				
				AIScoreSpace();
				
			}
			
			if(current-lasts>=autooffset&&auto&&autotype == "All"){
				lasts = current;
				
				AIAll();
				
			}
			
		}
	}
	
	public void paint(Graphics g){
		super.paint(g);
		
		g.setFont(title);
		g.drawString(text, 520, 100);
		g.setFont(scoref);
		g.drawString("Your Score: ", 530, 200);
		g.drawString(""+score, 530, 250);
		g.drawString("Highscore: ", 530, 300);
		g.drawString(""+highscore, 530, 350);
		g.drawString(caption1, 520, 400);
		g.drawString(caption2, 520, 440);
		g.drawString(autotoggle, 530, 490);
		if(auto){
			g.drawString(autotype, 530, 540);
		}
		
		for(int i = 0; i < 16;i++){
			
			if (num[i] == 0) {
				g.drawImage(blank, i % 4 * imgdim + i % 4 * 20 + 20, 50 + ((int) i / 4) * imgdim + ((int) i / 4) * 20, imgdim, imgdim, this);
			}
			if (num[i] == 2) {
				g.drawImage(two, i % 4 * imgdim + i % 4 * 20 + 20, 50 + ((int) i / 4) * imgdim + ((int) i / 4) * 20, imgdim, imgdim, this);
			}
			if (num[i] == 4) {
				g.drawImage(four, i % 4 * imgdim + i % 4 * 20 + 20, 50 + ((int) i / 4) * imgdim + ((int) i / 4) * 20, imgdim, imgdim, this);
			}
			if (num[i] == 8) {
				g.drawImage(eight, i % 4 * imgdim + i % 4 * 20 + 20, 50 + ((int) i / 4) * imgdim + ((int) i / 4) * 20, imgdim, imgdim, this);
			}
			if (num[i] == 16) {
				g.drawImage(teen, i % 4 * imgdim + i % 4 * 20 + 20, 50 + ((int) i / 4) * imgdim + ((int) i / 4) * 20, imgdim, imgdim, this);
			}
			if (num[i] == 32) {
				g.drawImage(thirty, i % 4 * imgdim + i % 4 * 20 + 20, 50 + ((int) i / 4) * imgdim + ((int) i / 4) * 20, imgdim, imgdim, this);
			}
			if (num[i] == 64) {
				g.drawImage(sixty, i % 4 * imgdim + i % 4 * 20 + 20, 50 + ((int) i / 4) * imgdim + ((int) i / 4) * 20, imgdim, imgdim, this);
			}
			if (num[i] == 128) {
				g.drawImage(onetwenty, i % 4 * imgdim + i % 4 * 20 + 20, 50 + ((int) i / 4) * imgdim + ((int) i / 4) * 20, imgdim, imgdim, this);
			}
			if (num[i] == 256) {
				g.drawImage(twofifty, i % 4 * imgdim + i % 4 * 20 + 20, 50 + ((int) i / 4) * imgdim + ((int) i / 4) * 20, imgdim, imgdim, this);
				if(text != "512" && text != "1024" && text != "you won")text = "256";
			}
			if (num[i] == 512) {
				g.drawImage(fiveh, i % 4 * imgdim + i % 4 * 20 + 20, 50 + ((int) i / 4) * imgdim + ((int) i / 4) * 20, imgdim, imgdim, this);
				if(text != "1024" && text != "you won")text = "512";
			}
			if (num[i] == 1024) {
				g.drawImage(onek, i % 4 * imgdim + i % 4 * 20 + 20, 50 + ((int) i / 4) * imgdim + ((int) i / 4) * 20, imgdim, imgdim, this);
				if(text != "you won")text = "1024";
			}
			if (num[i] == 2048) {
				g.drawImage(twok, i % 4 * imgdim + i % 4 * 20 + 20, 50 + ((int) i / 4) * imgdim + ((int) i / 4) * 20, imgdim, imgdim, this);
				text = "you won";
			}
		}
		
	}
	
	public boolean moveup(boolean move, int[] num) {
		boolean hasmoved = false;
		for(int i = 0; i < 4; i++){
			//adding
			if (num[i] != 0) {
				if (num[i] == num[i + 4]) {
					if (move) {
						num[i] += num[i + 4];
						num[i + 4] = 0;
						score += num[i];
					}
					hasmoved = true;
				} else if (num[i] == num[i + 8] && num[i + 4] == 0) {
					if (move) {
						num[i] += num[i + 8];
						num[i + 8] = 0;
						score += num[i];
					}
					hasmoved = true;
				} else if (num[i] == num[i + 12] && num[i + 4] == 0 && num[i + 8] == 0) {
					if (move) {
						num[i] += num[i + 12];
						num[i + 12] = 0;
						score += num[i];
					}
					hasmoved = true;
				}
			}
			
			if (num[i + 4] != 0) {

				if (num[i + 4] == num[i + 8]) {
					if (move) {
						num[i + 4] += num[i + 8];
						num[i + 8] = 0;
						score += num[i + 4];
					}
					hasmoved = true;
				} else if (num[i + 4] == num[i + 12] && num[i + 8] == 0) {
					if (move) {
						num[i + 4] += num[i + 12];
						num[i + 12] = 0;
						score += num[i + 4];
					}
					hasmoved = true;
				}

			}
			
			if (num[i + 8] == num[i + 12] && num[i + 8] != 0) {
				if (move) {
					num[i + 8] += num[i + 12];
					num[i + 12] = 0;
					score += num[i + 8];
				}
				hasmoved = true;
			}
			
			//filling gaps
			
			if (num[i + 8] == 0 && num[i + 12] != 0) {
				if (move) {
					num[i + 8] = num[i + 12];
					num[i + 12] = 0;
				}
				hasmoved = true;
			}
			if (num[i + 4] == 0 && (num[i + 12] != 0 || num[i + 8] != 0)) {
				if (move) {
					num[i + 4] = num[i + 8];
					num[i + 8] = num[i + 12];
					num[i + 12] = 0;
				}
				hasmoved = true;
			}
			if (num[i] == 0 && (num[i + 12] != 0 || num[i + 8] != 0 || num[i + 4] != 0)) {
				if (move) {
					num[i] = num[i + 4];
					num[i + 4] = num[i + 8];
					num[i + 8] = num[i + 12];
					num[i + 12] = 0;
				}
				hasmoved = true;
			}
		}
		if (hasmoved) {
			return true;
		}
		return false;
	}

	public boolean movedown(boolean move, int[] num) {
		boolean hasmoved = false;
		for(int i = 0; i < 4; i++){
			
			//adding
			
			if (num[i + 12] != 0) {

				if (num[i + 12] == num[i + 8]) {
					if (move) {
						num[i + 12] += num[i + 8];
						num[i + 8] = 0;
						score += num[i + 12];
					}
					hasmoved = true;
				} else if (num[i + 12] == num[i + 4] && num[i + 8] == 0) {
					if (move) {
						num[i + 12] += num[i + 4];
						num[i + 4] = 0;
						score += num[i + 12];
					}
					hasmoved = true;
				} else if (num[i + 12] == num[i] && num[i + 8] == 0 && num[i + 4] == 0) {
					if (move) {
						num[i + 12] += num[i];
						num[i] = 0;
						score += num[i + 12];
					}
					hasmoved = true;
				}

			}

			if (num[i + 8] != 0) {

				if (num[i + 8] == num[i + 4]) {
					if (move) {
						num[i + 8] += num[i + 4];
						num[i + 4] = 0;
						score += num[i + 8];
					}
					hasmoved = true;
				} else if (num[i + 8] == num[i] && num[i + 4] == 0) {
					if (move) {
						num[i + 8] += num[i];
						num[i] = 0;
						score += num[i + 8];
					}
					hasmoved = true;
				}

			}

			if (num[i + 4] != 0 && num[i + 4] == num[i]) {
				if (move) {
					num[i + 4] += num[i];
					num[i] = 0;
					score += num[i + 4];
				}
				hasmoved = true;
			}
			
			//moving down
			
			if (num[i + 4] == 0 && num[i] != 0) {
				if (move) {
					num[i + 4] = num[i];
					num[i] = 0;
				}
				hasmoved = true;
			}
			if (num[i + 8] == 0 && (num[i] != 0 || num[i + 4] != 0)) {
				if (move) {
					num[i + 8] = num[i + 4];
					num[i + 4] = num[i];
					num[i] = 0;
				}
				hasmoved = true;
			}
			if (num[i + 12] == 0 && (num[i] != 0 || num[i + 4] != 0 || num[i + 8] != 0)) {
				if (move) {
					num[i + 12] = num[i + 8];
					num[i + 8] = num[i + 4];
					num[i + 4] = num[i];
					num[i] = 0;
				}
				hasmoved = true;
			}
		}
		
		if (hasmoved) {
			return true;
		}
		return false;
	}

	public boolean moveleft(boolean move, int[] num) {
		boolean hasmoved = false;
		
		for(int i = 0; i<4;i++){
			
			//adding
			
			if(num[i*4] != 0){
				
				if (num[i * 4] == num[i * 4 + 1]) {
					if (move) {
						num[i * 4] += num[i * 4 + 1];
						num[i * 4 + 1] = 0;
						score += num[i * 4];
					}
					hasmoved = true;

				} else if (num[i * 4] == num[i * 4 + 2] && num[i * 4 + 1] == 0) {
					if (move) {
						num[i * 4] += num[i * 4 + 2];
						num[i * 4 + 2] = 0;
						score += num[i * 4];
					}
					hasmoved = true;

				} else if (num[i * 4] == num[i * 4 + 3] && num[i * 4 + 1] == 0 && num[i * 4 + 2] == 0) {
					if (move) {
						num[i * 4] += num[i * 4 + 3];
						num[i * 4 + 3] = 0;
						score += num[i * 4];
					}
					hasmoved = true;

				}
				
			}
			
			if(num[i*4+1] != 0){
				
				if (num[i * 4 + 1] == num[i * 4 + 2]) {
					if (move) {
						num[i * 4 + 1] += num[i * 4 + 2];
						num[i * 4 + 2] = 0;
						score += num[i * 4 + 1];
					}
					hasmoved = true;
				} else if (num[i * 4 + 1] == num[i * 4 + 3] && num[i * 4 + 2] == 0) {
					if (move) {
						num[i * 4 + 1] += num[i * 4 + 3];
						num[i * 4 + 3] = 0;
						score += num[i * 4 + 1];
					}
					hasmoved = true;
				}
			}
			
			if (num[i * 4 + 2] != 0 && num[i * 4 + 2] == num[i * 4 + 3]) {
				if (move) {
					num[i * 4 + 2] += num[i * 4 + 3];
					num[i * 4 + 3] = 0;
					score += num[i * 4 + 2];
				}
				hasmoved = true;

			}
			
			//filling gaps
			
			if (num[i * 4 + 2] == 0 && num[i * 4 + 3] != 0) {
				if (move) {
					num[i * 4 + 2] = num[i * 4 + 3];
					num[i * 4 + 3] = 0;
				}
				hasmoved = true;
			}

			if (num[i * 4 + 1] == 0 && (num[i * 4 + 3] != 0 || num[i * 4 + 2] != 0)) {
				if (move) {
					num[i * 4 + 1] = num[i * 4 + 2];
					num[i * 4 + 2] = num[i * 4 + 3];
					num[i * 4 + 3] = 0;
				}
				hasmoved = true;
			}
			if (num[i * 4] == 0 && (num[i * 4 + 3] != 0 || num[i * 4 + 2] != 0 || num[i * 4 + 1] != 0)) {
				if (move) {
					num[i * 4] = num[i * 4 + 1];
					num[i * 4 + 1] = num[i * 4 + 2];
					num[i * 4 + 2] = num[i * 4 + 3];
					num[i * 4 + 3] = 0;
				}
				hasmoved = true;
			}
			
		}
		
		if (hasmoved) {
			return true;
		}
		return false;
	}

	public boolean moveright(boolean move, int[] num) {
		boolean hasmoved = false;
		
		for(int i = 0; i<4;i++){
			
			//adding
			
			if(num[i*4+3] != 0){
				
				if(num[i*4+3] == num[i*4+2]){
					if (move) {
					num[i*4+3] += num[i*4+2];
					num[i*4+2] = 0;
					score+= num[i*4+3];
					}
					hasmoved = true;
				}else if(num[i*4+3] == num[i*4+1]&&num[i*4+2]==0){
					if (move) {
					num[i*4+3] += num[i*4+1];
					num[i*4+1] = 0;
					score+= num[i*4+3];
					}
					hasmoved = true;
				}else if(num[i*4+3] == num[i*4]&&num[i*4+2]==0&&num[i*4+1] == 0){
					if (move) {
					num[i*4+3] += num[i*4];
					num[i*4] = 0;
					score+= num[i*4+3];
					}
					hasmoved = true;
				}
			}
			
			if(num[i*4+2] != 0){
				
				if (num[i * 4 + 2] == num[i * 4 + 1]) {
					if (move) {
						num[i * 4 + 2] += num[i * 4 + 1];
						num[i * 4 + 1] = 0;
						score += num[i * 4 + 2];
					}
					hasmoved = true;
				} else if (num[i * 4 + 2] == num[i * 4] && num[i * 4 + 1] == 0) {
					if (move) {
						num[i * 4 + 2] += num[i * 4];
						num[i * 4] = 0;
						score += num[i * 4 + 2];
					}
					hasmoved = true;
				}
				
			}
			
			if (num[i * 4 + 1] != 0 && num[i * 4] == num[i * 4 + 1]) {
				if (move) {
					num[i * 4] += num[i * 4 + 1];
					num[i * 4 + 1] = 0;
					score += num[i * 4 + 1];
				}
				hasmoved = true;

			}
			
			//filling gaps
			
			if (num[(i * 4) + 1] == 0 && num[i * 4] != 0) {
				if (move) {
					num[(i * 4) + 1] = num[(i * 4)];
					num[(i * 4)] = 0;
				}
				hasmoved = true;
			}
			if (num[(i * 4) + 2] == 0 && (num[i * 4] != 0 || num[i * 4 + 1] != 0)) {
				if (move) {
					num[(i * 4) + 2] = num[(i * 4) + 1];
					num[(i * 4) + 1] = num[(i * 4)];
					num[(i * 4)] = 0;
				}
				hasmoved = true;
			}
			if (num[(i * 4) + 3] == 0 && (num[i * 4] != 0 || num[i * 4 + 1] != 0 || num[i * 4 + 2] != 0)) {
				if (move) {
					num[(i * 4) + 3] = num[(i * 4) + 2];
					num[(i * 4) + 2] = num[(i * 4) + 1];
					num[(i * 4) + 1] = num[(i * 4)];
					num[(i * 4)] = 0;
				}
				hasmoved = true;
			}
			
		}
		
		if (hasmoved) {
			return true;
		}
		return false;
	}
	
	public void AITopRight(){
		alt++;
		if(alt <= 2){
			alt = 0;
		}
		
		if(alt == 1&&moveright(true,num)){
			add();
		}else if(alt == 2&&moveup(true,num)){
			add();
		}else if(moveright(true,num)){
			add();
		}else if(moveup(true,num)){
			add();
		}else if(moveleft(false,num)&&numofz(0)<numofz(1)){
			moveleft(true,num);
			alt = 1;
			add();
		}else if(movedown(false,num)&&numofz(0)>numofz(1)){
			movedown(true,num);
			alt = 0;
			add();
		}else if(moveleft(true,num)){
			add();
			alt = 1;
		}else if(movedown(true,num)){
			add();
			alt = 0;
		}
	}
	
	public void AIBestScore(){
		
		int st,sr,sd,sl,ts = score;
		
		for(int i = 0; i < num.length; i++){
			testnum[i] = num[i];
		}
		
		moveup(true, testnum);
		st = score;
		score = ts;
		
		for(int i = 0; i < num.length; i++){
			testnum[i] = num[i];
		}
		
		moveright(true, testnum);
		sr = score;
		score = ts;
		for(int i = 0; i < num.length; i++){
			testnum[i] = num[i];
		}
		
		movedown(true, testnum);
		sd = score;
		score = ts;
		
		for(int i = 0; i < num.length; i++){
			testnum[i] = num[i];
		}
		
		moveleft(true, testnum);
		sl = score;
		score = ts;
		
		if(st >= sr && st >= sd && st >= sl && moveup(true, num)){
			add();
		}else if(sr >= sd && sr >= sl && moveright(true, num)){
			add();
		}else if(sd >= sl && movedown(true, num)){
			add();
		}else if(moveleft(true, num)){
			add();
		}
		
	}
	
	public void AIEmptySpaces(){
		int st,sr,sd,sl;
		
		for(int i = 0; i < num.length; i++){
			testnum[i] = num[i];
		}
		
		moveup(true, testnum);
		st = numofz();
		
		for(int i = 0; i < num.length; i++){
			testnum[i] = num[i];
		}
		
		moveright(true, testnum);
		sr = numofz();
		
		for(int i = 0; i < num.length; i++){
			testnum[i] = num[i];
		}
		
		movedown(true, testnum);
		sd = numofz();
		
		for(int i = 0; i < num.length; i++){
			testnum[i] = num[i];
		}
		
		moveleft(true, testnum);
		sl = numofz();
		
		if(st <= sr && st <= sd && st <= sl && moveup(true, num)){
			add();
		}else if(sr <= sd && sr <= sl && moveright(true, num)){
			add();
		}else if(sd <= sl && movedown(true, num)){
			add();
		}else if(moveleft(true, num)){
			add();
		}
	}
	
	public void AIScoreSpace(){
		alt++;
		if(alt == 2){
			alt = 0;
		}
		
		if(alt == 1){
			AIBestScore();
		}else{
			AIEmptySpaces();
		}
		
	}
	
	public void AIAll(){
		
		int st,sr,ts = score;
		
		for(int i = 0; i < num.length; i++){
			testnum[i] = num[i];
		}
		
		moveup(true, testnum);
		st = score-ts;
		score = ts;
		
		for(int i = 0; i < num.length; i++){
			testnum[i] = num[i];
		}
		
		moveright(true, testnum);
		sr = score-ts;
		score = ts;
		
		int spt,spr,spd,spl;
		
		for(int i = 0; i < num.length; i++){
			testnum[i] = num[i];
		}
		
		moveup(true, testnum);
		spt = numofz();
		
		for(int i = 0; i < num.length; i++){
			testnum[i] = num[i];
		}
		
		moveright(true, testnum);
		spr = numofz();
		
		int at = st+(st/2)*(spt),ar = sr+(sr/2)*(spr);
		
		if(num[2] == num[7]&&num[2]>0&&horizontal_L(1,1,3)&&horizontal_L(0)&&moveleft(true, num)){
			add();
			alt = 4;
		}else if(num[1] == num[6]&&num[1]>0&&horizontal_L(1,1,2)&&horizontal_L(0)&&moveleft(true, num)){
			add();
			alt = 4;
		}else if(alt == 3 && moveright(true, num)){
			alt = 0;
			add();
		}else if(alt == 4 && moveup(true, num)){
			alt = 0;
			add();
		}else if(at >= ar && moveup(true, num)){
			add();
		}else if(ar>at&&moveright(true, num)){
			add();
		}else if(moveup(true, num)){
			add();
		}else if(moveright(true, num)){
			add();
		}else if(moveleft(true, num)){
			add();
			alt = 3;
		}else if(movedown(true, num)){
			add();
			alt = 4;
		}
		
	}
	
	public boolean horizontal_L(int i){
		//returns true is it cant move
		
		i = i*4;
		
		if(num[i] == 0||num[i+1] == 0||num[i+2] == 0||num[i+3] == 0){
			return false;
		}
		
		if(num[i] == num[i+1]||num[i+1] == num[i+2]||num[i+2] == num[i+3]){
			return false;
		}
		
		return true;
		
	}
	
	public boolean horizontal_L(int i, int check, int col){
		
		i = i*4;
		
		int ischeck = 0;
		
		for(int y = i; y < i+col;y++){
			if(num[y] == 0){
				ischeck++;
				continue;
			}
			if(num[y] == num[y+1])ischeck++;
		}
		
		if(ischeck == check)
			return true;
		return false;
	}
	
	public int numofz(){
		int z = 0;
		for(int i = 0; i < num.length; i++){
			if(num[i] == 0)z++;
		}
		return z;
	}
	
	public void add(){
		boolean complete = false;
		int temphighscore = highscore;
		while (!complete) {
			int chance = r.nextInt(16);
			if (num[chance] == 0) {
				if (r.nextInt(10) < 9) {
					num[chance] = 2;
					
				}else{
					num[chance] = 4;
				}
				complete = true;
			}
			
		}
		
		if(score > highscore){
			highscore = score;
		}
		//repaint();
		if(score > temphighscore){
			sethighscore(highscore);
		}
	}
	
	public int numofz(int i){
		int x = 0;
		int y = 0;
		
		if(i == 0){
			if(num[0] == 0){
				x++;
			}
			if(num[1] == 0){
				x++;
			}
			if(num[2] == 0){
				x++;
			}
			if(num[3] == 0){
				x++;
			}
			return x;
		}
		if(i == 1){
			if(num[3] == 0){
				y++;
			}
			if(num[7] == 0){
				y++;
			}
			if(num[11] == 0){
				y++;
			}
			if(num[15] == 0){
				y++;
			}
			return y;
		}
		return 0;
	}
	
	public int gethighscore(){
		//Game.class.getResource("/highscore.xml").openStream()
		Scanner scanner;
		int temp;
		try {
			scanner = new Scanner(new InputStreamReader(Game.class.getResource("/highscore.xml").openStream()));
			temp =  scanner.nextInt();
			scanner.close();
			return temp;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		return 0;
	}
	
	public void sethighscore(int hscore) {
		/*try {
			// Writer writer = new BufferedWriter(new FileWriter(new
			// File(Game.class.getResource("/highscore.xml").toURI())));
			// writer.write(Integer.toString(hscore));
			// writer.close();
			String filedir = Game.class.getResource("/highscore.xml").toString();
			filedir = filedir.replace("%20", " ");
			filedir = filedir.replace("file:/", "");
			System.out.println(filedir);
			Writer out = new FileWriter(filedir);
			out.write(Integer.toString(hscore));
			out.close();
		} catch (IOException e) {
			
			e.printStackTrace();
			System.exit(0);
		}// catch (URISyntaxException e) {
//			System.exit(0);
//			e.printStackTrace();
//		}*/
	}
	
	public void reset(){
		
		for(int i = 0; i < 16; i++){
			num[i] = 0;
		}
		
		//text = "   2048";
		caption1 = "";
		score = 0;
		lose = false;
		autotoggle = "p to toggle AI";
		add();
		add();
		repaint();
	}
	
	public static void main(String[] args) {
		
		Game game = new Game();
		
		JFrame frame = new JFrame("2048");
		
		frame.add(game);
		frame.setSize(WIDTH, HEIGHT);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		game.run();
		
	}
	
	public class KeyStuff implements KeyListener{
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			
			
			if (key == KeyEvent.VK_W&&keycheck != 1) {
				
				if(moveup(true,num)){
					add();
				}
				
				keycheck = 1;
			}
			if (key == KeyEvent.VK_S&&keycheck != 2) {
				
				if(movedown(true,num)){
					add();
				}
				
				keycheck = 2;
			}
			if (key == KeyEvent.VK_D&&keycheck != 3) {
				
				if(moveright(true,num)){
					add();
				}
				
				keycheck = 3;
			}
			if (key == KeyEvent.VK_A&&keycheck != 4) {
				
				if(moveleft(true,num)){
					add();
				}
				
				keycheck = 4;
			}
			
			if (key == KeyEvent.VK_R&&keycheck != 5) {
				if(keycheck == 6){
					reset();
				}
				keycheck = 5;
			}
			
			if (key == KeyEvent.VK_CONTROL&&keycheck != 6) {
				if(keycheck == 5){
					reset();
				}
				keycheck = 6;
			}
			
			if (key == KeyEvent.VK_H&&keycheck != 7) {
				if(keycheck == 6){
					highscore = 0;
					sethighscore(highscore);
					repaint();
				}
				keycheck = 7;
			}
			
			if(key == KeyEvent.VK_P&&!lose){
				if(auto){
					auto = false;
					caption1 = "";
					caption2 = "";
					autotoggle = "p to toggle AI";
				}else{
					auto = true;
					caption1 = "arrows to change";
					caption2 = "AI speed/type";
					autotoggle = "AI type:";
				}
				repaint();
			}
			
			if(key == KeyEvent.VK_UP&&autooffset>1&&auto == true&&!lose){
				if(autooffset>=100)
					autooffset-= 10;
				if(autooffset<100){
					autooffset--;
				}
			}
			if(key == KeyEvent.VK_DOWN&&autooffset<1000&&auto == true&&!lose){
				if(autooffset>=100)
					autooffset+= 10;
				if(autooffset<100){
					autooffset++;
				}
			}
			if(key == KeyEvent.VK_RIGHT && auto == true  && !lose){
				if(autotype == "Best Score"){
					autotype = "Empty Spaces";
				}else if (autotype == "Empty Spaces"){
					autotype = "Space n' Score";
				}else if(autotype == "Top Right"){
					autotype = "Best Score";
				}else if(autotype == "Space n' Score"){
					autotype = "All";
				}else if(autotype == "All"){
					autotype = "Top Right";
				}
			}
			
			//temp
			repaint();
			
		}

		@Override
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();
			
			if (key == KeyEvent.VK_W) {
				keycheck = 0;
			}
			if (key == KeyEvent.VK_S) {
				keycheck = 0;
			}
			if (key == KeyEvent.VK_D) {
				keycheck = 0;
			}
			if (key == KeyEvent.VK_A) {
				keycheck = 0;
			}
			if (key == KeyEvent.VK_R) {
				keycheck = 0;
			}
			if (key == KeyEvent.VK_CONTROL) {
				keycheck = 0;
			}
			if (key == KeyEvent.VK_H) {
				keycheck = 0;
			}
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}

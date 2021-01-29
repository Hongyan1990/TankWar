package com.imooc.tank;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class TankClient extends Frame {
	public static final int WIDTH = 400;
	public static final int HEIGHT = 300;
	Image imageScreenOff = null;
	
	TankThread tankThread = new TankThread();
	Tank myTank = new Tank(50, 50, true,this);
	Tank enemyTank = new Tank(300, 200, false, this);
	Wall w1 = new Wall(100, 100, 20, 60, this);
	Wall w2 = new Wall(200, 150, 60, 20, this);
//	Missile m = null;
	List<Missile> ms = new ArrayList<Missile>();
	List<Explode> explodes = new ArrayList<Explode>();
	List<Tank> enemyTanks = new ArrayList<Tank>();
	List<Blood> bloods = new ArrayList<Blood>();
	
	public void launchFrame() {
		this.setLocation(300, 200);
		this.setSize(WIDTH, HEIGHT);
		this.setBackground(Color.GRAY);
		this.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		this.addKeyListener(new MonitorKey());
		this.setResizable(false);
		this.setVisible(true);
		for(int i=1; i<=2; i++) {
			this.enemyTanks.add(new Tank(50 + 40*i, 50 + 30*i, false, this));
		}
		new Thread(tankThread).start();
	}
	
	public void createBlood() {
		for(int i=0; i<5; i++) {
			this.bloods.add(new Blood(i));
		}
	}

	@Override
	public void update(Graphics g) {
		if(imageScreenOff == null) {
			imageScreenOff = this.createImage(WIDTH, HEIGHT);
		}
		Graphics gOff = imageScreenOff.getGraphics();
		Color c = g.getColor();
		gOff.setColor(Color.GRAY);
		gOff.fillRect(0, 0, WIDTH, HEIGHT);
		gOff.setColor(c);
		paint(gOff);
		g.drawImage(imageScreenOff, 0, 0, null);
	}



	@Override
	public void paint(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.YELLOW);
		g.drawString("missile count: " + ms.size(), 10, 50);
		g.drawString("explode count: " + explodes.size(), 10, 70);
		g.drawString("tank count: " + enemyTanks.size(), 10, 90);
		g.setColor(c);
		w1.draw(g);
		w2.draw(g);
		for(int i=0; i<ms.size(); i++) {
			Missile m = ms.get(i);
			m.hitTanks(enemyTanks);
			m.hitTank(myTank);
			m.hitWall(w1);
			m.hitWall(w2);
			m.drawMissile(g);
		}
		
		for(int i=0; i<explodes.size(); i++) {
			Explode e = explodes.get(i);
			e.draw(g);
		}
		for(int i=0; i<enemyTanks.size(); i++) {
			Tank t = enemyTanks.get(i);
			t.collidesWithWall(w1);
			t.collidesWithWall(w2);
			t.collidesWithTank(enemyTanks);
			t.drawTank(g);
		}
		for(int i=0; i<bloods.size(); i++) {
			Blood b = bloods.get(i);
			b.draw(g);
		}
		myTank.drawTank(g);
		myTank.eat(bloods);
//		b.draw(g);
//		enemyTank.drawTank(g);
	}
	
	class TankThread implements Runnable {

		@Override
		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	class MonitorKey extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			myTank.keyPressed(e);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			myTank.keyReleased(e);
		}

	}

	public static void main(String[] args) {
		TankClient tc = new TankClient();
		tc.launchFrame();

	}

}

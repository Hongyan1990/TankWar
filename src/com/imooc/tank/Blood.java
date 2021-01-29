package com.imooc.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Blood {
	private int x, y, w, h;
	private int[][] pos = {{200, 100}, {250, 80}, {230, 200}, {300, 150}, {320, 100}, {280, 200}, {80, 280}, {120, 260}, {60, 80}};
	private int step = 0;
	private boolean live = true;
	
	public Blood(int step) {
		this.x = pos[0][0];
		this.y = pos[0][1];
		this.w = this.h = 12;
		this.step = step;
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.PINK);
		g.fillRect(x, y, w, h);
		g.setColor(c);
		if(step == pos.length) {
			step = 0;
		}
		x = pos[step][0];
		y = pos[step][1];
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, w, h);
	} 
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	
	
}

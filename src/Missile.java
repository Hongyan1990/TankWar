import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.List;

public class Missile {
	public static final int SPEED_X = 10;
	public static final int SPEED_Y = 10;
	
	public static final int WIDTH = 8;
	public static final int HEIGHT = 8;
	
	private int x = 0;
	private int y = 0;
	
	private boolean live = true;
	
	private boolean good;
	
	Tank.Dir dir;
	
	TankClient tc = null;
	
	public Missile(int x, int y, Tank.Dir dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public Missile(int x, int y, Tank.Dir dir, boolean good, TankClient tc) {
		this(x, y, dir);
		this.tc = tc;
		this.good = good;
	}
	
	public void drawMissile(Graphics g) {
		if(!live) {
			tc.ms.remove(this);
			return;
		}
		Color c = g.getColor();
		if(good) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.BLACK);
		}
		
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		move();
		checkDead();
	}
	
	public void move() {
		switch(dir) {
		case L:
			x -= SPEED_X;
			break;
		case LU:
			x -= SPEED_X;
			y -= SPEED_Y;
			break;
		case U:
			y -= SPEED_Y;
			break;
		case RU:
			y -= SPEED_Y;
			x += SPEED_X;
			break;
		case R:
			x += SPEED_X;
			break;
		case RD:
			x += SPEED_X;
			y += SPEED_Y;
			break;
		case D:
			y += SPEED_Y;
			break;
		case LD:
			y += SPEED_Y;
			x -= SPEED_X;
			break;
		default:
			x += 0;
			y += 0;
		}
	}
	
	public void checkDead() {
		if(x<0 || y<0 || x>TankClient.WIDTH || y>TankClient.HEIGHT) {
			this.setLive(false);
		}
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	} 
	
	public boolean hitTank(Tank t) {
		if(this.getRect().intersects(t.getRect()) && t.isLive() && good != t.good) {
			t.setLive(false);
			this.live = false;
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			return true;
		}
		return false;
	}
	
	public boolean hitTanks(List<Tank> tanks) {
		for(int i=0; i<tanks.size(); i++) {
			Tank t = tanks.get(i);
			if(hitTank(t)) {
				return true;
			}
		}
		return false;
	}
	
	public void setLive(boolean live) {
		this.live = live;
	}
	
	public boolean getLive() {
		return live;
	}
	
}

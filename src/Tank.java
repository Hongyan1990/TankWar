import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Tank {
	public static final int SPEED_X = 5;
	public static final int SPEED_Y = 5;
	public static final int WIDTH = 20;
	public static final int HEIGHT = 20;
	
	public static Random r = new Random();
	
	private int x = 0;
	private int y = 0;
	private boolean bR = false, bU = false, bL = false, bD = false;
	enum Dir {L, LU, U, RU, R, RD, D, LD, STOP};
	boolean good;
	private boolean live = true;
	private Dir dir = Dir.STOP;
	private Dir ptDir = Dir.D;
	private int step = r.nextInt(12) + 3;
	TankClient tc = null;

	public Tank(int x, int y, boolean good) {
		this.x = x;
		this.y = y;
		this.good = good;
	}
	
	public Tank(int x, int y, boolean good, TankClient tc) {
		this(x, y, good);
		this.tc = tc;
	}
	
	public void drawTank(Graphics g) {
		if(!live) {
			if(!good) {
				tc.enemyTanks.remove(this);
				return;
			}
		};
		Color c = g.getColor();
		if(good) {
			g.setColor(Color.BLUE);
		} else {
			g.setColor(Color.BLACK);
		}
		
		g.fillOval(x, y, WIDTH, HEIGHT);
		g.setColor(c);
		drawTankPt(g);
		move();
	}
	
	public void drawTankPt(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.WHITE);
		switch(ptDir) {
		case L:
			g.drawLine(x + WIDTH/2, y + HEIGHT/2, x, y + HEIGHT/2);
			break;
		case LU:
			g.drawLine(x + WIDTH/2, y + HEIGHT/2, x, y);
			break;
		case U:
			g.drawLine(x + WIDTH/2, y + HEIGHT/2, x + WIDTH/2, y);
			break;
		case RU:
			g.drawLine(x + WIDTH/2, y + HEIGHT/2, x + WIDTH, y);
			break;
		case R:
			g.drawLine(x + WIDTH/2, y + HEIGHT/2, x + WIDTH, y + HEIGHT/2);
			break;
		case RD:
			g.drawLine(x + WIDTH/2, y + HEIGHT/2, x + WIDTH, y + HEIGHT);
			break;
		case D:
			g.drawLine(x + WIDTH/2, y + HEIGHT/2, x + WIDTH/2, y + HEIGHT);
			break;
		case LD:
			g.drawLine(x + WIDTH/2, y + HEIGHT/2, x, y + HEIGHT);
			break;
		default:
			break;
		}
		g.setColor(c);
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
		case STOP:
			y += 0;
			x += 0;
			break;
		default:
			x += 0;
			y += 0;
		}
	
		if(dir != Dir.STOP) {
			ptDir = dir;
		}
		
		
		if(x < 0) x =0;
		if(y < 30) y = 30;
		if(x > TankClient.WIDTH-Tank.WIDTH) x = TankClient.WIDTH - Tank.WIDTH;
		if(y > TankClient.HEIGHT-Tank.HEIGHT) y = TankClient.HEIGHT - Tank.HEIGHT;
		
		if(!good) {
			if(step == 0) {
				step = r.nextInt(12) + 3;
				
				Dir[] dirs = Dir.values();
				int i = r.nextInt(dirs.length);
				dir = dirs[i];
			}
			step--;
			if(r.nextInt(40) > 38) fire();
		}
	}
	
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch(keyCode) {
		case KeyEvent.VK_CONTROL:
			fire();
			break;
		case KeyEvent.VK_UP:
			this.bU = true;
			break;
		case KeyEvent.VK_DOWN:
			this.bD = true;
			break;
		case KeyEvent.VK_LEFT:
			this.bL = true;
			break;
		case KeyEvent.VK_RIGHT:
			this.bR = true;
			break;
		}
		locateDirection();

	}
	
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch(keyCode) {
		case KeyEvent.VK_UP:
			this.bU = false;
			break;
		case KeyEvent.VK_DOWN:
			this.bD = false;
			break;
		case KeyEvent.VK_LEFT:
			this.bL = false;
			break;
		case KeyEvent.VK_RIGHT:
			this.bR = false;
			break;
		}
		locateDirection();
	}
	
	public void locateDirection() {
		if(bU && !bD && !bL && !bR) dir = Dir.U;
		else if(bU && !bD && !bL && bR) dir = Dir.RU;
		else if(!bU && !bD && !bL && bR) dir = Dir.R;
		else if(!bU && bD && !bL && bR) dir = Dir.RD;
		else if(!bU && bD && !bL && !bR) dir = Dir.D;
		else if(!bU && bD && bL && !bR) dir = Dir.LD;
		else if(!bU && !bD && bL && !bR) dir = Dir.L;
		else if(bU && !bD && bL && !bR) dir = Dir.LU;
		else if(!bU && !bD && !bL && !bR) dir = Dir.STOP;
	}
	
	public Missile fire() {
		int x = this.x + WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + HEIGHT/2 - Missile.HEIGHT/2;
		Missile m = new Missile(x, y, ptDir, good, tc);
		tc.ms.add(m);
		return m;
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}
	
	
	
}

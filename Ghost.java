import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Ghost {
    public int x;
    public int y;
    private double realX;
    private double realY;
    public int width = 60;
    public int height = 60;
    private final int collisionWidth = 26;
    private final int collisionHeight = 26;
    private final double speed = 2.4;
    private static final int GRID_SIZE = 10;
    private static final int GRID_WIDTH = (GamePanel.GAME_WIDTH / GRID_SIZE) + 1;
    private static final int GRID_HEIGHT = (GamePanel.GAME_HEIGHT / GRID_SIZE) + 1;
    private ArrayList<Point> path = new ArrayList<>();
    private int pathIndex = 0;
    private int pathTimer = 0;
    private final int PATH_REFRESH = 18;
    private BufferedImage ghostImage;
    public Ghost(int x,int y) {
        this.x = x;
        this.y = y;
        this.realX = x;
        this.realY = y;
        loadImage();
    }
    private void loadImage() {
        try {
            ghostImage =ImageIO.read(getClass().getResource("/images/ghost.png"));
        } catch (Exception e) {
            System.out.println("error");
            ghostImage = null;
        }
    }
    public void update(Player player,ArrayList<Wall> walls,ArrayList<Desk> desks) {
        if (player == null) {
            return;
        }
        pathTimer++;
        if (path.isEmpty() || pathTimer >= PATH_REFRESH || pathIndex >= path.size()) {
            pathTimer = 0;
            findNewPath(player,walls,desks);
        }
        followPath(player,walls,desks);
        x = (int)Math.round(realX);
        y = (int)Math.round(realY);
    }
    private void findNewPath(Player player, ArrayList<Wall> walls, ArrayList<Desk> desks) {
        int startX = worldToGrid(realX + width / 2.0);
        int startY = worldToGrid(realY+ height / 2.0);
        int targetX = worldToGrid(player.x+ player.width / 2.0);
        int targetY = worldToGrid(player.y+ player.height / 2.0);
        startX = clamp(startX,0,GRID_WIDTH - 1);
        startY = clamp(startY,0,GRID_HEIGHT - 1);
        targetX = clamp(targetX,0,GRID_WIDTH - 1);
        targetY = clamp(targetY,0,GRID_HEIGHT - 1);
        boolean[][] blocked =createCollisionGrid(walls,desks);
        blocked[startY][startX] = false;
        blocked[targetY][targetX] = false;
        ArrayList<Point> newPath =aStar(startX,startY,targetX,targetY,blocked);
        if (!newPath.isEmpty()) {
            path = newPath;
            pathIndex = 0;
        }
    }
    private boolean[][] createCollisionGrid(ArrayList<Wall> walls,ArrayList<Desk> desks) {
        boolean[][] blocked = new boolean[GRID_HEIGHT][GRID_WIDTH];
        for (int gy = 0; gy < GRID_HEIGHT; gy++) {
            for (int gx = 0; gx < GRID_WIDTH; gx++) {
                double centerX = gx * GRID_SIZE + GRID_SIZE/2.0;
                double centerY = gy * GRID_SIZE + GRID_SIZE/2.0;
                Rectangle ghostRect = new Rectangle((int)(centerX- collisionWidth/2.0),
                        (int)(centerY- collisionHeight/2.0 ),collisionWidth,collisionHeight);
                if (collision(ghostRect,walls,desks)) {
                    blocked[gy][gx] = true;
                }
            }
        }
        return blocked;
    }
    private boolean collision(Rectangle rect,ArrayList<Wall> walls,ArrayList<Desk> desks) {
        for (Wall wall : walls) {
            if (rect.intersects(wall.getBounds())) {
                return true;
            }
        }
        for (Desk desk : desks) {
            if (rect.intersects(desk.getBounds())) {
                return true;
            }
        }
        return false;
    }
    private static class Node
            implements Comparable<Node> {
        int x;
        int y;
        double g;
        double f;
        Node parent;
        Node(int x,int y,double g,double f,Node parent) {
            this.x = x;
            this.y = y;
            this.g = g;
            this.f = f;
            this.parent = parent;
        }
        @Override
        public int compareTo(Node other) {
            return Double.compare(this.f,other.f);
        }
    }
    private ArrayList<Point> aStar(int startX, int startY, int targetX, int targetY, boolean[][] blocked) {
        ArrayList<Point> result =
                new ArrayList<>();
        PriorityQueue<Node> open =
                new PriorityQueue<>();
        boolean[][] closed =new boolean[GRID_HEIGHT][GRID_WIDTH];
        double[][] bestG =new double[GRID_HEIGHT][GRID_WIDTH];

        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                bestG[y][x] = Double.MAX_VALUE;
            }
        }
        Node start = new Node(startX, startY, 0, heuristic( startX, startY, targetX, targetY ), null);
        open.add(start);
        bestG[startY][startX] = 0;
        int[][] directions = {{1,0},{-1,0},{0,1},{0,-1},{1,1},{-1,1},{1,-1},{-1,-1}};
        while (!open.isEmpty()) {
            Node current = open.poll();
            if (closed[current.y][current.x]) {
                continue;
            }
            closed[current.y][current.x] = true;
            if (current.x == targetX && current.y == targetY) {
                Node temp = current;
                while (temp != null) {
                    result.add(0,new Point( temp.x,temp.y ));
                    temp = temp.parent;}
                return result;
            }
            for (int[] dir : directions) {
                int nx = current.x + dir[0];
                int ny = current.y + dir[1];
                if (nx<0 || nx >= GRID_WIDTH || ny<0 || ny >= GRID_HEIGHT) {
                    continue;
                }
                if (blocked[ny][nx]) {
                    continue;
                }
                if (dir[0] != 0 && dir[1] != 0) {
                    if (blocked[ current.y ][nx] || blocked[ ny ][ current.x ]) {
                        continue;
                    }
                }
                if (closed[ny][nx]) {
                    continue;
                }
                double cost;
                if (dir[0] != 0 && dir[1] != 0) {
                    cost = 1.414;
                } else {
                    cost = 1.0;
                }
                double newG = current.g + cost;
                if (newG < bestG[ny][nx]) {
                    bestG[ny][nx] = newG;
                    double h = heuristic(nx, ny, targetX, targetY);
                    Node next = new Node(nx, ny, newG, newG + h, current);
                    open.add(next);
                }
            }
        }
        return result;
    }
    private double heuristic(int x1,int y1,int x2,int y2) {
        int dx = Math.abs(x1-x2);
        int dy = Math.abs(y1-y2);
        return Math.max(dx,dy);
    }
    private void followPath(Player player,ArrayList<Wall> walls,ArrayList<Desk> desks) {
        if (path == null || path.isEmpty()) {
            return;
        }
        while (pathIndex < path.size()) {
            Point point = path.get(pathIndex);
            double targetX = point.x*GRID_SIZE + GRID_SIZE/2.0 - width/2.0;
            double targetY =  point.y*GRID_SIZE + GRID_SIZE/2.0 - height/2.0;
            double dx = targetX - realX;
            double dy = targetY - realY;
            double distance = Math.sqrt(dx*dx + dy*dy);
            if (distance<5) {
                pathIndex++;
            } else {
                break;
            }
        }
        if (pathIndex >= path.size()) {
            return;
        }
        Point point =path.get(pathIndex);
        double targetX = point.x*GRID_SIZE + GRID_SIZE/2.0 - width/2.0;
        double targetY = point.y*GRID_SIZE + GRID_SIZE/2.0 - height/2.0;
        double dx = targetX-realX;
        double dy = targetY-realY;
        double distance = Math.sqrt(dx*dx+dy*dy);
        if (distance <= 0) {
            return;
        }
        dx /= distance;
        dy /= distance;
        double moveX = dx*speed;
        double moveY = dy*speed;
        boolean movedX = tryMoveX(moveX,walls,desks);
        boolean movedY = tryMoveY(moveY,walls,desks);
        if (!movedX && Math.abs(dx)>0.1) {
            tryMoveY(Math.signum(dy)*speed,walls,desks);
        }
        if (!movedY && Math.abs(dy)>0.1) {
            tryMoveX(Math.signum(dx)*speed,walls,desks);
        }
    }
    private boolean tryMoveX(double amount,ArrayList<Wall> walls,ArrayList<Desk> desks) {
        if (Math.abs(amount)<0.001) {
            return false;
        }
        double newX = realX+amount;
        Rectangle next = new Rectangle((int)newX+getCollisionOffsetX(),(int)realY+getCollisionOffsetY(),collisionWidth,collisionHeight);
        if (!collision( next,walls,desks )) {
            realX = newX;
            return true;
        }
        return false;
    }
    private boolean tryMoveY(double amount,ArrayList<Wall> walls,ArrayList<Desk> desks) {
        if (Math.abs(amount)<0.001) {
            return false;
        }
        double newY = realY + amount;
        Rectangle next = new Rectangle((int)realX+getCollisionOffsetX(),(int)newY+getCollisionOffsetY(),collisionWidth,collisionHeight);
        if (!collision( next,walls,desks )) {
            realY = newY;
            return true;
        }
        return false;
    }
    private int worldToGrid(double value) {
        return (int)(value/GRID_SIZE);
    }
    private int clamp(int value,int min,int max) {
        return Math.max(min, Math.min( max, value ));
    }
    private int getCollisionOffsetX() {
        return (width - collisionWidth)/2;
    }
    private int getCollisionOffsetY() {
        return (height - collisionHeight)/2;
    }
    public Rectangle getBounds() {
        return new Rectangle(x + getCollisionOffsetX(),y+getCollisionOffsetY(),collisionWidth,collisionHeight);
    }
    public boolean hitPlayer(Player player) {
        if (player == null) {
            return false;
        }
        return getBounds().intersects(player.getBounds());
    }
    public void reset(int startX,int startY) {
    x = startX;
    y = startY;
    realX = startX;
    realY = startY;
    path.clear();
    pathIndex = 0;
    pathTimer = PATH_REFRESH;
}
    public void draw(Graphics2D g) {
        if (ghostImage != null) {
            g.drawImage(ghostImage,x,y,width,height,null);
        } else {
            g.setColor(Color.WHITE);
            g.fillOval(x,y,width,height);
            g.setColor(Color.BLACK);
            g.fillOval(x+14,y+18,8,8);
            g.fillOval(x+38,y+18,8,8);
        }
    }
}

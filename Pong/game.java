import processing.core.PApplet;
// Define movement speed
float speed = 3;

// Define scores
float p1Score = 0;
float p2Score = 0;

// Define player variables
Player p1;
Player p2;

// Define ball variables
Ball ball;

// Define the last x position of the ball
float lastBallPositionX = 0;

void setup() {  
  size(500, 500); // Set screen size
  
  // Create the player instances at each side of the screen
  p1 = new Player(10, height/2);
  p2 = new Player(width-20, height/2);
  
  // Create the ball instance
  ball = new Ball();
}

void draw() {
  background(0); // Set screen background to black
  
  // Update players
  p1.update();
  p2.update();
  
  // Update ball
  ball.update();
  
  // Get ball position
  PVector ballPosition = ball.getPosition(); 
  
  // Check if ball is outside the screen
  if (ballPosition.x < 0) {
    p2Score += 1;
    ball.resetMovement(); 
  } else if (ballPosition.x > width) {
    p1Score += 1;
    ball.resetMovement(); 
  }
  
  // Handle ball collisions with players
  if (ball.overlapsWith(p1)) {
    ball.setDirection(1); 
  }
  if (ball.overlapsWith(p2)) {
    ball.setDirection(-1); 
  }
  
  // Move P2 automatically (simple AI)
  if (lastBallPositionX < ballPosition.x) {
    PVector p2Position = p2.getPosition();
    float directionToBallY = ballPosition.y - p2Position.y;
    directionToBallY = constrain(directionToBallY, -1, 1);
    directionToBallY *= speed;
    p2.setDirection(directionToBallY); 
  }
  
  // Update last ball position
  lastBallPositionX = ballPosition.x;
  
  // Display Scores
  fill(255);
  textSize(20);
  text("P1: " + int(p1Score), 50, 30);
  text("P2: " + int(p2Score), width - 100, 30);
}

void keyPressed() {
  // Move up
  if (key == 'w') p1.setDirection(-speed);
  // Move down
  else if (key == 's') p1.setDirection(speed);
}

// ------------------ Classes --------------------

class Player {
  PVector position;
  float yDir;
  float w = 10;
  float h = 25;
  float b = 15;
  
  Player(float x, float y) {
    position = new PVector(x, y);
    this.yDir = 0;
  }
  
  public void setDirection(float yDir) {
    this.yDir = yDir;
  }
  
  public PVector getPosition() {
    return position;
  }
  
  public float getHeight() {
    return h;
  }
  
  public float getWidth() {
    return w;
  }
  
  public void update() {
    // Update position based on y direction
    position.y += yDir;
    
    // Keep player within boundaries
    if (position.y < b) {
      position.y = b;
    } else if (position.y > height - b - h) {
      position.y = height - b - h;
    }
    
    // Draw player
    fill(255);
    rect(position.x, position.y, w, h);
  }
}

class Ball {
  PVector position;
  PVector direction;
  float d = 15;   
  float s = 5; // Speed
  
  Ball() {
    resetMovement();
  }
  
  public PVector getPosition() {
    return position;
  }
  
  public void resetMovement() {
    position = new PVector(width/2, height/2);
    float speedX = random(-s, s);
    direction = new PVector(speedX, speedX/2);        
  }
  
  public void setDirection(float x) {
    direction.x = x * s;
  }
  
  public void update() {
    position.add(direction);
    
    if (position.y < 0 || position.y > height - d) {
      direction.y = -direction.y; // Reverse the y direction
    }
    
    // Draw ball
    fill(255);
    ellipse(position.x, position.y, d, d);
  }
  
  public boolean overlapsWith(Player player) {
    PVector p = player.getPosition();
    float w = player.getWidth();
    float h = player.getHeight();
    float r = d/2;
    
    for (int i = 0; i < 8; i++) {
      float degree = radians(i * 45);
      float x = r * cos(degree) + position.x;
      float y = r * sin(degree) + position.y;
      
      if (p.x < x && x < p.x + w &&
          p.y < y && y < p.y + h) {
        return true;
      }
    }
    return false;
  }
}

public class game extends PApplet {

    public static void main(String[] args) {
        PApplet.main("game");
    }
}

// ------------------ End of Classes --------------------
// This code is a simple 2D Pong game where two players can control paddles to hit a ball.
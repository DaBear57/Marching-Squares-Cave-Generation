import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class cave_generator extends PApplet {

boolean[][] cells = new boolean[128][80];
int cs;

public void gen(float t) {
  for (int i = 1; i < cells.length - 1; i++) {
    for (int j = 1; j < cells[0].length - 1; j++) {
      if (random(1) < t) {
        cells[i][j] = true;
      }
    }
  }
}

public void setup() {
  
  frameRate(60);
  cs = floor(width / cells.length);
  gen(0.58f);
}

public boolean grow(int x, int y) {
  int nwalls = 0;
  if (! cells[x][y]) {
    nwalls--;
  }
  for (int i = x - 1; i <= x + 1; i++) {
    for (int j = y - 1; j <= y + 1; j++) {
      if (! cells[i][j]) {
        nwalls++;
      }
    }
  }
  if (! cells[x][y] && nwalls < 3) {
    return true;
  }
  if (cells[x][y] && nwalls > 4) {
    return false;
  }
  return cells[x][y];
}

public boolean xor(boolean a, boolean b) {
  if (!(a && b) && (a || b)) {
    return true;
  }
  return false;
}

public void march(int x, int y) {
  ArrayList<PVector> connect = new ArrayList<PVector>();
  if (xor(cells[x][y],cells[x + 1][y])) {
    connect.add(new PVector(x + 0.5f, y));
  }
  if (xor(cells[x + 1][y + 1],cells[x + 1][y])) {
    connect.add(new PVector(x + 1, y + 0.5f));
  }
  if (xor(cells[x][y + 1],cells[x + 1][y + 1])) {
    connect.add(new PVector(x + 0.5f, y + 1));
  }
  if (xor(cells[x][y],cells[x][y + 1])) {
    connect.add(new PVector(x, y + 0.5f));
  }
  for (PVector i : connect) {
    i = i.mult(cs);
  }
  stroke(255);
  strokeWeight(3);
  for (int i = 0; i < connect.size(); i += 2) {
    PVector a = connect.get(i);
    PVector b = connect.get(i + 1);
    line(a.x + cs / 2,a.y + cs / 2,b.x + cs / 2,b.y + cs / 2);
  }
}

public void draw() {
  background(0);
  noStroke();
  for (int i = 1; i < cells.length - 1; i++) {
    for (int j = 1; j < cells[0].length - 1; j++) {
      cells[i][j] = grow(i,j);
    }
  }
  //for (int i = 1; i < cells.length - 1; i++) {
  //  for (int j = 1; j < cells[0].length - 1; j++) {
  //    if (cells[i][j]) {
  //      rect(i * cs, j * cs, cs, cs);
  //    }
  //  }
  //}
  for (int i = 0; i < cells.length - 1; i++) {
    for (int j = 0; j < cells[0].length - 1; j++) {
      march(i,j);
    }
  }
  println(xor(true,false));
  if (mousePressed && mouseButton == LEFT) {
    cells[floor(mouseX/cs)][floor(mouseY/cs)] = false;
  } else if (mousePressed && mouseButton == RIGHT) {
    cells[floor(mouseX/cs)][floor(mouseY/cs)] = true;
  }
}
  public void settings() {  fullScreen(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#cccccc", "cave_generator" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

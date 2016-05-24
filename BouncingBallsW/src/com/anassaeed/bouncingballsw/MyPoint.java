package com.anassaeed.bouncingballsw;


public class MyPoint {
	  String text;
	  public int x;
	  public int y;
	  public int xVelocity;
	  public int yVelocity;
	  public MyPoint(String text, int x, int y) {
	    this.text = text;
	    this.x = x;
	    this.y = y;
	    double tempx =  Math.random();
	    if ( tempx > 0.5){
	    	this.xVelocity = 2+(int)(4*Math.random());
	    }else {
	    	this.xVelocity = -2 -(int)(4*Math.random());
	    }
	    double tempy = Math.random();
	    if ( tempy > 0.5){
	    	this.yVelocity = 2+ (int)(4*Math.random());
	    } else {
	    	this.yVelocity = -2 -(int)(4*Math.random());
	    }
	    
	  }
	}
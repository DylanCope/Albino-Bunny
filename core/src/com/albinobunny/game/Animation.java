package com.albinobunny.game;

import java.util.ArrayList;

public class Animation<X> 
{
	private ArrayList<X> frames;
	private int currentFrame;
	private float frameTime, timeTillChange;
	private boolean loop, animate;
	
	public Animation(float frameTime, X...frames)
	{
		this(frameTime, true, frames);
	}
	
	public Animation(float frameTime, boolean loop, X...frames)
	{
		this.frameTime = frameTime;
		timeTillChange = frameTime;
		this.frames = new ArrayList<X>();
		for (int i = 0; i < frames.length; i++)
			this.frames.add(frames[i]);
		animate = true;
		this.loop = loop;
	}
	
	public void update(float delta)
	{
		if (animate)
		{
			timeTillChange -= delta;
			if (timeTillChange <= 0)
			{
				if (!loop && currentFrame + 1 == frames.size())
				{
					animate = false;
				}
				else
				{
					timeTillChange = frameTime;
					currentFrame = (currentFrame + 1) % frames.size();
				}
				
			}
		}
	}
	
	public X getCurrentFrame()
	{
		return frames.get(currentFrame);
	}
	
	public void animate()
	{
		animate = true;
		timeTillChange = frameTime;
		currentFrame = 0;
	}
	
}

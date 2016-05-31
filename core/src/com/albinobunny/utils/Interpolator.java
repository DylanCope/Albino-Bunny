package com.albinobunny.utils;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Interpolator extends Actor
{
	private interface InterpolationCalculator
	{
		public void interpolate(float delta);
	}
	
	public interface OnCompleteEvent {
		public void event();
	}

	public enum Type { Position, Size, Rotation, Color, X, Y, Width, Height, Alpha };
	public enum Method { Linear, Sinusodial };
	
	ArrayList<InterpolationCalculator> calculators;
	Actor actor;
	Type type;
	Method method;
	OnCompleteEvent onComplete;
	Object start, end;
	float delay;
	
	public Interpolator(Actor actor)
	{
		this.actor = actor;
		calculators = new ArrayList<InterpolationCalculator>();
		delay = 0;
	}
	
	public Interpolator type(Type type)
	{
		this.type = type;
		return this;
	}
	
	public Interpolator start(Object start)
	{
		this.start = start;
		return this;
	}
	
	public Interpolator target(Object target)
	{
		this.end = target;
		return this;
	}
	
	public Interpolator delay(float delay)
	{
		this.delay = delay;
		return this;
	}
	
	public Interpolator onComplete(OnCompleteEvent onComplete)
	{
		this.onComplete = onComplete;
		return this;
	}
	
	public Interpolator method(Method method)
	{
		this.method = method;
		return this;
	}
	
	/**
	 * Given previously set variables such as target, start, type, ect. an interface is created to
	 * manage the interpolation of the provided actor's attributes. As the Interpolator class extends
	 * Actor, the interpolation is updated when the act() method is called. This can either be called
	 * manually or the Interpolator object can be added to a stage.
	 * To remove the need to requirement to set every variable before calling this function, various
	 * assumptions are made for variables that haven't been set; If the start hasn't been set, then the
	 * current values for the attribute defined by the type will be used. If the method isn't set, 
	 * Method.Sinusodial will be used. If the type hasn't been set it will be guessed by examining the
	 * end and seeing what class its an instance of.
	 * @throws Exception
	 */
	public void interpolate(float time)
	{
		if (method == null)
			method = Method.Sinusodial;
		if (type == null)
		{
			if (end instanceof Color)
				type = Type.Color;
			else if (end instanceof Float)
				type = Type.Alpha;
			else if (end instanceof Vector2)
				type = Type.Position;
		}
		if (start == null)
		{
			if (type.equals(Type.Color))
				start = actor.getColor();
			else if (type.equals(Type.Position))
				start = new Vector2(actor.getX(), actor.getY());
			else if (type.equals(Type.Rotation))
				start = actor.getRotation();
			else if (type.equals(Type.Size))
				start = new Vector2(actor.getWidth(), actor.getHeight());
			else if (type.equals(Type.X))
				start = actor.getX();
			else if (type.equals(Type.Y))
				start = actor.getY();
			else if (type.equals(Type.Width))
				start = actor.getWidth();
			else if (type.equals(Type.Height))
				start = actor.getHeight();
			else if (type.equals(Type.Alpha))
				start = actor.getColor().a;
		}
		interpolate(type, start, end, time, method, onComplete);
		onComplete = null;
		start = null;
		end = null;
		delay = 0;
	}
	
	public void interpolate(Vector2 end, float time) 
	{
		try {
			interpolate(Type.Position, end, time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void interpolate(Type type, Object end, float time)
	{
		interpolate(type, end, time, Method.Sinusodial);
	}
	
	public void interpolate(Type type, Object end, float time, float delay)
	{
		this.delay = delay;
		interpolate(type, end, time, Method.Sinusodial);
	}
	
	public void interpolate(Type type, Object end, float time, Method method)
	{	
		if (type.equals(Type.Color)) 
			interpolate(type, actor.getColor(), end, time, method);
		else if (type.equals(Type.Position)) 
			interpolate(type, new Vector2(actor.getX(), actor.getY()), end, time, method);
		else if (type.equals(Type.Rotation)) 
			interpolate(type, actor.getRotation(), end, time, method);
		else if (type.equals(Type.Size)) 
			interpolate(type, new Vector2(actor.getWidth(), actor.getHeight()), end, time, method);
		else if (type.equals(Type.X)) 
			interpolate(type, actor.getX(), end, time, method);
		else if (type.equals(Type.Y)) 
			interpolate(type, actor.getY(), end, time, method);
		else if (type.equals(Type.Width)) 
			interpolate(type, actor.getWidth(), end, time, method);
		else if (type.equals(Type.Height)) 
			interpolate(type, actor.getHeight(), end, time, method);
		else if (type.equals(Type.Alpha)) 
			interpolate(type, actor.getColor().a, end, time, method);
	}
	
	public void interpolate(Type type, Object start, Object end, float time, Method method)
	{
		if (end instanceof Integer)
			end = (float) ((Integer) end).intValue();
		if (end instanceof Double)
			end = (float) ((Double) end).doubleValue();
		
		if (start instanceof Integer)
			start = (float) ((Integer) end).intValue();
		if (start instanceof Double)
			start = (float) ((Double) start).doubleValue();
		
		interpolate(type, start, end, time, method, onComplete);
	}
	
	public void interpolate(final Type type, final Object start, final Object end, 
								final float time, final Method method, final OnCompleteEvent onComplete)
	{	
		Timer.schedule(new Task() {
			public void run() 
			{
				InterpolationCalculator calculator = null;
				
				if (method.equals(Method.Linear))
				{
					calculator = new InterpolationCalculator()
					{	
						float elapsed = 0;
						
						public void interpolate(float delta)
						{
							elapsed += delta;
							if (elapsed >= time) {
								calculators.remove(this);
								if (onComplete != null)
									onComplete.event();
								if (type.equals(Type.Color))
									actor.setColor((Color) end);
								else if (type.equals(Type.Position))
									actor.setPosition(((Vector2) end).x, ((Vector2) end).y);
								else if (type.equals(Type.Rotation))
									actor.setRotation((Float) end);
								else if (type.equals(Type.Size))
									actor.setSize(((Vector2) end).x, ((Vector2) end).y);
								else if (type.equals(Type.X))
									actor.setX((Float) end);
								else if (type.equals(Type.Y))
									actor.setY((Float) end);
								else if (type.equals(Type.Width))
									actor.setWidth((Float) end);
								else if (type.equals(Type.Height))
									actor.setHeight((Float) end);
								else if (type.equals(Type.Alpha))
									actor.setColor(actor.getColor().r, actor.getColor().g, actor.getColor().b, ((Float) end));
							}
							
							if (type.equals(Type.Color))
							{
								Color s = (Color) start; Color e = (Color) end;
								actor.setColor(new Color(
										s.r + elapsed * (e.r - s.r) / time, 
										s.g + elapsed * (e.g - s.g) / time, 
										s.b + elapsed * (e.b - s.b) / time, 
										s.a + elapsed * (e.a - s.a) / time));
							}
							else if (type.equals(Type.Position))
							{
								Vector2 s = (Vector2) start; Vector2 e = (Vector2) end;
								actor.setPosition(
										s.x + elapsed * (e.x - s.x) / time, 
										s.y + elapsed * (e.y - s.y) / time);
							}
							else if (type.equals(Type.Rotation))
							{
								Float s = (Float) start; Float e = (Float) end;
								actor.setRotation(s + elapsed * (e - s) / time);
							}
							else if (type.equals(Type.Size))
							{
								Vector2 s = (Vector2) start; Vector2 e = (Vector2) end;
								actor.setSize(
										s.x + elapsed * (e.x - s.x) / time, 
										s.y + elapsed * (e.y - s.y) / time);
							}
							else if (type.equals(Type.X)) 
							{
								Float s = (Float) start; Float e = (Float) end;
								actor.setX(s + elapsed * (e - s) / time);
							}
							else if (type.equals(Type.Y)) 
							{
								Float s = (Float) start; Float e = (Float) end;
								actor.setY(s + elapsed * (e - s) / time);
							}
							else if (type.equals(Type.Width)) 
							{
								Float s = (Float) start; Float e = (Float) end;
								actor.setWidth(s + elapsed * (e - s) / time);
							}
							else if (type.equals(Type.Height)) 
							{
								Float s = (Float) start; Float e = (Float) end;
								actor.setHeight(s + elapsed * (e - s) / time);
							}
							else if (type.equals(Type.Alpha)) 
							{
								Float s = (Float) start; Float e = (Float) end;
								actor.setColor(actor.getColor().r, actor.getColor().g, 
										actor.getColor().b, s + elapsed * (e - s) / time);
							}
						}
					};
				}
				else if (method.equals(Method.Sinusodial))
				{
					calculator = new InterpolationCalculator()
					{
						public float elapsed = 0;
						
						public void interpolate(float delta)
						{
							elapsed += delta;
							if (elapsed >= time) {
								calculators.remove(this);
							if (onComplete != null)
								onComplete.event();
							if (onComplete != null)
								onComplete.event();
							if (type.equals(Type.Color))
								actor.setColor((Color) end);
							else if (type.equals(Type.Position))
								actor.setPosition(((Vector2) end).x, ((Vector2) end).y);
							else if (type.equals(Type.Rotation))
								actor.setRotation((Float) end);
							else if (type.equals(Type.Size))
								actor.setSize(((Vector2) end).x, ((Vector2) end).y);
							else if (type.equals(Type.X))
								actor.setX((Float) end);
							else if (type.equals(Type.Y))
								actor.setY((Float) end);
							else if (type.equals(Type.Width))
								actor.setWidth((Float) end);
							else if (type.equals(Type.Height))
								actor.setHeight((Float) end);
							else if (type.equals(Type.Alpha))
								actor.setColor(actor.getColor().r, actor.getColor().g, actor.getColor().b, ((Float) end));
							}
							
							float cos = (float) Math.cos(Math.PI * elapsed / time);
							float k = (1 - cos) * time / (float) Math.PI;
							
							if (type.equals(Type.Color) && start instanceof Color && end instanceof Color)
							{
								Color s = (Color) start; Color e = (Color) end; 
								float vr = 0.5f * (float) Math.PI * (e.r - s.r) / time;
								float vg = 0.5f * (float) Math.PI * (e.g - s.g) / time;
								float vb = 0.5f * (float) Math.PI * (e.b - s.b) / time;
								float va = 0.5f * (float) Math.PI * (e.a - s.a) / time;
								actor.setColor(new Color(s.r + vr * k, s.g + vg * k, s.b + vb * k, s.a + va * k));
							}
							else if (type.equals(Type.Position) && start instanceof Vector2 && end instanceof Vector2)
							{
								Vector2 s = (Vector2) start; Vector2 e = (Vector2) end;
								Vector2 vMax = new Vector2(
										0.5f * (float) Math.PI * (e.x - s.x) / time,
										0.5f * (float) Math.PI * (e.y - s.y) / time);
								actor.setPosition(s.x + vMax.x * k, s.y + vMax.y * k);
							}
							else if (type.equals(Type.Rotation) && start instanceof Float && end instanceof Float)
							{
								Float s = (Float) start; Float e = (Float) end;
								float vMax = 0.5f * (float) Math.PI * (e - s) / time;
								actor.setRotation(s + vMax * k);
							}
							else if (type.equals(Type.Size) && start instanceof Vector2 && end instanceof Vector2)
							{
								Vector2 s = (Vector2) start; Vector2 e = (Vector2) end;
								Vector2 vMax = new Vector2(
										0.5f * (float) Math.PI * (e.x - s.x) / time,
										0.5f * (float) Math.PI * (e.y - s.y) / time);
								actor.setSize(s.x + vMax.x * k, s.y + vMax.y * k);
							}
							else if (type.equals(Type.X) && start instanceof Float && end instanceof Float) 
							{
								Float s = (Float) start; Float e = (Float) end;
								float vMax = 0.5f * (float) Math.PI * (e - s) / time;
								actor.setX(s + vMax * k);
							}
							else if (type.equals(Type.Y) && start instanceof Float && end instanceof Float) 
							{ 
								Float s = (Float) start; Float e = (Float) end;
								float vMax = 0.5f * (float) Math.PI * (e - s) / time;
								actor.setY(s + vMax * k);
							}
							else if (type.equals(Type.Width) && start instanceof Float && end instanceof Float) 
							{
								Float s = (Float) start; Float e = (Float) end;
								float vMax = 0.5f * (float) Math.PI * (e - s) / time;
								actor.setWidth(s + vMax * k);
							}
							else if (type.equals(Type.Height) && start instanceof Float && end instanceof Float) 
							{
								Float s = (Float) start; Float e = (Float) end;
								float vMax = 0.5f * (float) Math.PI * (e - s) / time;
								actor.setHeight(s + vMax * k);
							}
							else if (type.equals(Type.Alpha) && start instanceof Float && end instanceof Float) 
							{
								Float s = (Float) start; Float e = (Float) end;
								float vMax = 0.5f * (float) Math.PI * (e - s) / time;
								actor.getColor().a = (s + vMax * k);
							}
						}
					};
				}
				
				if (calculator != null)
					calculators.add(calculator);
				
				delay = 0;
			}
		}, delay);
	}
	
	@Override
	public void act(float delta)
	{
//		Gdx.app.log("DEBUG", "asdf");
		for (int i = 0; i < calculators.size(); i++)
			calculators.get(i).interpolate(delta);
	}
}
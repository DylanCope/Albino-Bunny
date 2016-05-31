package com.albinobunny.game;

import java.util.Random;

import com.albinobunny.utils.Assets;
import com.albinobunny.utils.MusicPlayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Player 
{
	private Sprite sprite, floor;
	private boolean alive, onGround, moveLeft, moveRight;
	private Vector2 position, velocity;
	private Controls controls;
	private float walkSpeed;
	
	enum AnimationType { STILL, WALKING, JUMPING }
	private Array<Animation<Texture>> animations;
	private Animation<Texture> currentAnimation;
	
	public enum Direction { LEFT, RIGHT }
	private Direction dir;
	
	public interface Controls
	{
		public boolean moveLeft();
		public boolean moveRight();
		public boolean jump();
	}
	
	public static final Controls RandomControls = new Controls()
	{
		Player.Direction dir;
		float changeTime = 0.5f;
		float timeTillChange = 0;
		
		@Override
		public boolean moveLeft() {
			
			Random rand = new Random();
			
			if (timeTillChange <= 0)
			{
				if (rand.nextInt(20) == 0) {
					dir = Direction.LEFT;
					timeTillChange = changeTime;
				}
				if (rand.nextInt(30) == 0) {
					dir = null;
					timeTillChange = changeTime;
				}
			}
			else
			{
				timeTillChange -= Gdx.graphics.getDeltaTime();
			}
			return dir == Direction.LEFT;
		}

		@Override
		public boolean moveRight() {
			Random rand = new Random();
			
			if (timeTillChange <= 0)
			{
				if (rand.nextInt(20) == 0) {
					dir = Direction.RIGHT;
					timeTillChange = changeTime;
				}
				if (rand.nextInt(30) == 0) {
					dir = null;
					timeTillChange = changeTime;
				}
			}
			else
			{
				timeTillChange -= Gdx.graphics.getDeltaTime();
			}
			
			return dir == Direction.RIGHT;
		}

		@Override
		public boolean jump() {
			Random rand = new Random();
			return rand.nextInt(1000) == 0;
		}
	};
	

	public static final Controls TiltControls = new Controls() 
	{
		@Override
		public boolean moveLeft() {
			return Gdx.input.getAccelerometerX() > 1 || Gdx.input.isKeyPressed(Keys.LEFT);
		}

		@Override
		public boolean moveRight() {
			return Gdx.input.getAccelerometerX() < -1 || Gdx.input.isKeyPressed(Keys.RIGHT);
		}

		@Override
		public boolean jump() {
			return Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE);
		}
	};
	
	public Player(Sprite floor)
	{
		this.floor = floor;
		dir = Direction.RIGHT;

		Texture still = Assets.manager.get("graphics/player/bunny_still.png", Texture.class);
		Texture walk = Assets.manager.get("graphics/player/bunny_walk.png", Texture.class);
		Texture air1 = Assets.manager.get("graphics/player/bunny_air1.png", Texture.class);
		Texture air2 = Assets.manager.get("graphics/player/bunny_air2.png", Texture.class);
		Texture air3 = Assets.manager.get("graphics/player/bunny_air3.png", Texture.class);

		sprite = new Sprite(still);
		sprite.setColor(Color.WHITE);
		
		sprite.setSize(Gdx.graphics.getWidth() / 6, Gdx.graphics.getWidth() / 6);
		
		Texture[] stillFrames = { still };
		Animation<Texture> stillAnimation = new Animation<Texture>(10f, stillFrames);
		Texture[] walkingFrames = { walk, still };
		Animation<Texture> walkingAnimation = new Animation<Texture>(0.1f, walkingFrames);
		Texture[] jumpingFrames = { walk, air1, air1, air2, air3 };
		Animation<Texture> jumpingAnimation = new Animation<Texture>(0.12f, false, jumpingFrames);
		
		animations = new Array<Animation<Texture>>();
		animations.add(stillAnimation);
		animations.add(walkingAnimation);
		animations.add(jumpingAnimation);
		currentAnimation = walkingAnimation;
		
		position = new Vector2(Gdx.graphics.getWidth() / 3, floor.getY() + floor.getHeight());
		velocity = new Vector2(0, 0);
		
		alive = true;
		onGround = true;
		
		controls = TiltControls;
		
		walkSpeed = Gdx.graphics.getWidth() * 3 / 4f;
		
		moveLeft = false;
		moveRight = false;
	}

	public void update(float delta)
	{
		float dx = delta * walkSpeed;
		
		moveLeft = controls.moveLeft() && position.x - dx > 0;
		moveRight = controls.moveRight() && position.x + dx < Gdx.graphics.getWidth() - sprite.getWidth();
		
		if (moveRight) {
			position.x += dx;
			if (!dir.equals(Direction.RIGHT))
			{
				dir = Direction.RIGHT;
				sprite.flip(true, false);
			}
		}
		else if (moveLeft) {
			position.x -= dx;
			if (!dir.equals(Direction.LEFT))
			{
				dir = Direction.LEFT;
				sprite.flip(true, false);
			}
		}

		if (onGround)
		{
			if (moveRight || moveLeft)
			{
				currentAnimation = animations.get(AnimationType.WALKING.ordinal());
			}
			else
			{
				currentAnimation = animations.get(AnimationType.STILL.ordinal());
			}
		}
		
		if (controls.jump() && onGround)
		{
			onGround = false;
			velocity.set(0, AlbinoBunnyGame.grav * 0.4f);
			currentAnimation = animations.get(AnimationType.JUMPING.ordinal());
			currentAnimation.animate();
			
			MusicPlayer.jump(0.1f);
		}
		
		if (!onGround)
		{
			velocity.y -= delta * AlbinoBunnyGame.grav;
			position.y += delta * velocity.y;
		}
		
		if (!onGround && position.y <= floor.getY() + floor.getHeight())
		{
			onGround = true;
			position.y = floor.getY() + floor.getHeight();
		}
		
		currentAnimation.update(delta);
		sprite.setTexture(currentAnimation.getCurrentFrame());
	}
	
	public void render(SpriteBatch batch)
	{
		sprite.setPosition(position.x, position.y);
		sprite.draw(batch);
	}
	
	public void kill()
	{
		alive = false;
	}
	
	public void ressurect()
	{
		alive = true;
	}
	
	public boolean isAlive()
	{
		return alive;
	}
	
	public Sprite getSprite()
	{
		return sprite;
	}
	
	public void setControls(Controls controls)
	{
		this.controls = controls;
	}
	
	public void setWalkSpeed(float walkSpeed)
	{
		this.walkSpeed = walkSpeed;
	}
	
	public float getWalkSpeed()
	{
		return walkSpeed;
	}
	
	public boolean isInAir()
	{
		return !onGround;
	}
	
	public boolean moveLeft()
	{
		return moveLeft;
	}
	
	public boolean moveRight()
	{
		return moveRight;
	}
}

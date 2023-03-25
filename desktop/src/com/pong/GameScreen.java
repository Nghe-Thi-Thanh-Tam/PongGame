package com.pong;

import Helper.Const;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import org.lwjgl.opengl.GL20;
import Object.*;


public class GameScreen extends ScreenAdapter {

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    private Player player;
    private PlayerAI playerAI;
    private Ball ball;
    private Wall wallTop, wallBot;
    private GameContactListener gameContactListener;
    private TextureRegion[] numbers;
    public GameScreen(OrthographicCamera camera){
        this.camera = camera;
        this.camera.position.set(new Vector3(Boot.INSTANCE.getScreenWidth()/2, Boot.INSTANCE.getScreenHeight()/2, 0));
        this.batch = new SpriteBatch();
        this.world = new World(new Vector2(0, 0), false);
        this.box2DDebugRenderer = new Box2DDebugRenderer();
        this.gameContactListener = new GameContactListener(this);
        this.world.setContactListener(this.gameContactListener);

        this.player = new Player(16, Boot.INSTANCE.getScreenHeight()/2, this);
        this.playerAI = new PlayerAI(Boot.INSTANCE.getScreenWidth()-16, Boot.INSTANCE.getScreenHeight()/2, this);


        this.ball = new Ball(this);
        this.wallTop = new Wall(Wall.height/2, this);
        this.wallBot = new Wall(Boot.INSTANCE.getScreenHeight()-Wall.height/2, this);
        this.numbers = loadTextureSprite("numbers.png", 10);
    }

    public void update(){
        world.step(1/60f, 6, 2);
        batch.setProjectionMatrix(camera.combined);

        this.camera.update();
        this.player.update();
        this.playerAI.update();
        this.ball.update();

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE))  Gdx.app.exit();

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)){
            ball.reset();
            this.player.setScore(0);
            this.playerAI.setScore(0);
        }
    }

    @Override
    public void render(float delta){
        update();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        this.player.render(this.batch);
        this.playerAI.render(this.batch);
        this.ball.render(this.batch);
        this.wallBot.render(this.batch);
        this.wallTop.render(this.batch);
        drawNumber(batch, player.getScore(), 64, Boot.INSTANCE.getScreenHeight()-55, 30, 42);
        drawNumber(batch, playerAI.getScore(), Boot.INSTANCE.getScreenWidth()-96, Boot.INSTANCE.getScreenHeight()-55, 30, 42);

        batch.end();

//        this.box2DDebugRenderer.render(world, camera.combined.scl(Const.PPM));
    }
    private void drawNumber(SpriteBatch batch, int number, float x, float y, float width, float height){
        if (number<10){
            batch.draw(numbers[number], x, y, width, height);
        }
        else{
            int firstDigit = number/10;
            int secondDigit = number%10;

            batch.draw(numbers[firstDigit], x, y, width, height);
            batch.draw(numbers[secondDigit], x+30, y, width, height);

        }
    }
    private TextureRegion[] loadTextureSprite(String filename, int columns){
        Texture texture = new Texture(filename);
        return TextureRegion.split(texture, texture.getWidth()/columns, texture.getHeight())[0];
    }
    public World getWorld() {
        return world;
    }

    public Ball getBall() {
        return ball;
    }

    public Player getPlayer() {
        return player;
    }

    public PlayerAI getPlayerAI() {
        return playerAI;
    }
}

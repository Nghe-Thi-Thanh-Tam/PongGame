package Object;

import Helper.BodyHelper;
import Helper.ContactType;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.pong.Boot;
import com.pong.GameScreen;

public class Wall {
    private Body body;
    private float x, y;
    private int width;
    public static int height = 50;
    private Texture texture;

    public Wall(float y, GameScreen gameScreen){
        this.x = Boot.INSTANCE.getScreenWidth()/2;
        this.y = y;
        this.width = Boot.INSTANCE.getScreenWidth();

        texture = new Texture("white.png");
        this.body = BodyHelper.createBody(x, y, width, height, true, 0, gameScreen.getWorld(), ContactType.WALL);

    }

    public void render(SpriteBatch batch){
        batch.draw(texture, x - (width/2), y - (height/2), width, height);
    }
}

package com.yojplex.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by kenthall on 8/14/15.
 */
public class MainMenu {
    private boolean start;
    private int width;
    private int height;
    private double circleWidth;
    private double circleHeight;
    private int phoneWidth;
    private int phoneHeight;
    private Texture bg;
    private Texture circle;
    private Texture circleOutline;
    private Texture phone;
    private Texture upArrow;
    private Texture downArrow;
    private boolean arrowVisible;
    private Texture check;
    private BitmapFont titleFont;
    private BitmapFont tiltFont;
    private GlyphLayout titleLayout;
    private GlyphLayout titleLayout2;
    private GlyphLayout tiltLayout;
    private GlyphLayout highScoreLayout;
    private GlyphLayout gamesPlayedLayout;
    private int count;
    private Vector2 phoneLoc;
    private boolean playerSet;
    private Sound startSound;

    public MainMenu(){
        start=false;
        width=Gdx.graphics.getWidth();
        height=Gdx.graphics.getHeight();
        circleWidth=0;
        circleHeight=0;
        phoneWidth=78;
        phoneHeight=137;
        bg=new Texture("bg.png");
        circle=new Texture("circle.png");
        circleOutline=new Texture("circleOutline.png");
        phone=new Texture("phone.png");
        upArrow=new Texture("upArrow.png");
        downArrow=new Texture("downArrow.png");
        check=new Texture("check.png");
        arrowVisible=true;
        count=0;
        phoneLoc=new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2-phoneHeight*MyGdxGame.masterScale/2);
        highScoreLayout=new GlyphLayout();
        gamesPlayedLayout=new GlyphLayout();
        playerSet=true;
        startSound=Gdx.audio.newSound(Gdx.files.internal("sounds/ding.mp3"));

        titleLayout=new GlyphLayout();
        titleLayout2=new GlyphLayout();
        titleFont=new BitmapFont(Gdx.files.internal("fonts/titleFont.fnt"),Gdx.files.internal("fonts/titleFont.png"),false);

        tiltLayout=new GlyphLayout();
        tiltFont=new BitmapFont(Gdx.files.internal("fonts/tiltFont.fnt"),Gdx.files.internal("fonts/tiltFont.png"),false);
    }

    public void draw(SpriteBatch batch){
        if (playerSet) {
            MyGdxGame.setPlayer(new Player(Gdx.graphics.getWidth() / 2 + 150, Gdx.graphics.getHeight() / 2 + 150));
            playerSet=false;
        }
        batch.draw(bg, 0, 0, width, height);
        batch.draw(circleOutline, Gdx.graphics.getWidth() / 2-290*MyGdxGame.masterScale/2, Gdx.graphics.getHeight() / 2-290*MyGdxGame.masterScale/2, 290*MyGdxGame.masterScale, 290*MyGdxGame.masterScale);
        batch.draw(circle, MyGdxGame.getPlayer().getLocPlayer().x-(float)circleWidth/2, MyGdxGame.getPlayer().getLocPlayer().y-(float)circleHeight/2, (float) circleWidth, (float) circleHeight);
        batch.draw(phone, phoneLoc.x - phoneWidth*MyGdxGame.masterScale / 2, phoneLoc.y-phoneHeight*MyGdxGame.masterScale/2, phoneWidth*MyGdxGame.masterScale, phoneHeight*MyGdxGame.masterScale);

        count++;
        if (count>=25){
            arrowVisible=!arrowVisible;
            count=0;
        }

        MyGdxGame.getScoreFont().getData().setScale(MyGdxGame.masterScale);
        highScoreLayout.setText(MyGdxGame.getScoreFont(), "High Score:"+MyGdxGame.getPlayer().getPrefs().getInteger("highScore"));
        gamesPlayedLayout.setText(MyGdxGame.getScoreFont(), "Games Played:" + MyGdxGame.getPlayer().getPrefs().getInteger("gamesPlayed"));

        titleFont.getData().setScale(MyGdxGame.masterScale);
        titleLayout.setText(titleFont, "Bouncy");
        titleLayout2.setText(titleFont, "Stan");
        titleFont.draw(batch, "Bouncy", Gdx.graphics.getWidth() / 2 - titleLayout.width / 2, Gdx.graphics.getHeight() - 100 - titleLayout.height / 2);
        titleFont.draw(batch, "Stan", Gdx.graphics.getWidth() / 2 - titleLayout2.width / 2, Gdx.graphics.getHeight() - (float) (titleLayout.height * (2.3/MyGdxGame.masterScale)) - titleLayout2.height / 2);

        tiltFont.getData().setScale(MyGdxGame.masterScale, MyGdxGame.masterScale + MyGdxGame.masterScale / 3.333f);
        tiltLayout.setText(tiltFont, "Tilt Flat to Play!");
        tiltFont.draw(batch, "Tilt Flat to Play!", phoneLoc.x - tiltLayout.width / 2, phoneLoc.y - 200*MyGdxGame.masterScale - tiltLayout.height / 2);

        MyGdxGame.getScoreFont().draw(batch, "High Score:" + MyGdxGame.getPlayer().getPrefs().getInteger("highScore"), phoneLoc.x - highScoreLayout.width / 2, 200);
        MyGdxGame.getScoreFont().draw(batch, "Games Played:" + MyGdxGame.getPlayer().getPrefs().getInteger("gamesPlayed"), phoneLoc.x - gamesPlayedLayout.width / 2, 100);

        if (Gdx.input.getAccelerometerY()<1 && Gdx.input.getAccelerometerY()>-1){
            batch.draw(check, phoneLoc.x-phoneWidth*MyGdxGame.masterScale/2, phoneLoc.y-phoneWidth*MyGdxGame.masterScale/2, phoneWidth*MyGdxGame.masterScale, phoneWidth*MyGdxGame.masterScale);
            if (circleWidth<300*MyGdxGame.masterScale && circleHeight<300*MyGdxGame.masterScale){
                circleWidth+=5;
                circleHeight+=5;
            }
        }
        else{
            if ((int)Gdx.input.getAccelerometerY()>1 && arrowVisible){
                batch.draw(upArrow, phoneLoc.x - (float) (phoneWidth * 0.8)*MyGdxGame.masterScale / 2, phoneLoc.y-(float) (phoneWidth * 0.8)*MyGdxGame.masterScale/2.2f, (float) (phoneWidth * 0.8)*MyGdxGame.masterScale, (float) (phoneWidth * 0.8)*MyGdxGame.masterScale);
            }
            else if ((int)Gdx.input.getAccelerometerY()<-1 && arrowVisible){
                batch.draw(upArrow, phoneLoc.x - (float) (phoneWidth * 0.8)*MyGdxGame.masterScale / 2, phoneLoc.y-(float) (phoneWidth * 0.8)*MyGdxGame.masterScale / 4, (float) (phoneWidth * 0.8)*MyGdxGame.masterScale, (float) (phoneWidth * 0.8)*MyGdxGame.masterScale);
            }

            if (circleWidth>0 && circleHeight>0){
                circleWidth-=5;
                circleHeight-=5;
            }
        }

        //executes when circle is big enough, initiating play
        if (circleWidth>=300*MyGdxGame.masterScale && circleHeight>= 300*MyGdxGame.masterScale){
            MyGdxGame.reset();
            startSound.play(1f);
            start=true;
            MyGdxGame.getRequestHandler().showAds(IActivityRequestHandler.adState.LOAD);
        }
    }

    public void dispose(){
        bg.dispose();
        circle.dispose();
        circleOutline.dispose();
        phone.dispose();
        upArrow.dispose();
        downArrow.dispose();
        check.dispose();
        titleFont.dispose();
        tiltFont.dispose();
        startSound.dispose();
    }

    public boolean getStart(){
        return start;
    }

    public void setStart(boolean start){
        this.start=start;
    }

    public void setCircleWidth(double circleWidth){
        this.circleWidth=circleWidth;
    }

    public void setCircleHeight(double circleHeight){
        this.circleHeight=circleHeight;
    }

    public void setPlayerSet(boolean playerSet){
        this.playerSet=playerSet;
    }
}

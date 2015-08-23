package com.dugga.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


/**
 * Created by kenthall on 8/8/15.
 */
public class DeathMenu{
    private Texture menuImg;
    private int width;
    private int height;
    private Vector2 loc;
    private Button restartButton;
    private double growWidth;
    private double growHeight;
    private Sprite menuSprite;
    private int count;
    private boolean scoreWhite;
    private boolean showAd;

    public DeathMenu(){
        menuImg=new Texture("menu.png");
        width = 810;
        height=810;
        loc=new Vector2(Gdx.graphics.getWidth()/2-width/2, Gdx.graphics.getHeight()/2-height/2);
        restartButton=new Button(Button.Type.RESTART, (int)loc.x+width/2, (int)loc.y+height/2-200);
        growWidth=0.1;
        growHeight=0.1;
        menuSprite=new Sprite(menuImg, width, height);
        menuSprite.setOriginCenter();
        menuSprite.setPosition(loc.x, loc.y);
        showAd=true;
    }

    public void draw(SpriteBatch batch){
        if (showAd) {
            MyGdxGame.getRequestHandler().showAds(IActivityRequestHandler.adState.SHOW);
            showAd=false;
        }
        MyGdxGame.getScoreFont3().getData().setScale((float)growWidth, (float)growHeight);
        menuSprite.setScale((float)growWidth, (float)growHeight);
        menuSprite.draw(batch);
        if (growWidth<1){
            growWidth+=0.05;
        }
        if (growHeight<1){
            growHeight+=0.05;
        }

        if (count<40){
            count++;
        }
        else if (count>=40){
            scoreWhite=!scoreWhite;
            count=0;
        }
        restartButton.draw(batch);
        if (MyGdxGame.getPlayer().getScore()>MyGdxGame.getPlayer().getStartingHighScore() && MyGdxGame.getPlayer().getStartingHighScore()>0) {
            if (scoreWhite) {
                MyGdxGame.drawScore(restartButton.getLocX(), (float) (restartButton.getLocY() + 550), MyGdxGame.ScoreType.END, Color.WHITE);
            }
            else if (!scoreWhite) {
                MyGdxGame.drawScore(restartButton.getLocX(), (float) (restartButton.getLocY() + 550), MyGdxGame.ScoreType.END, null);
            }
        }
        else{
            MyGdxGame.drawScore(restartButton.getLocX(), (float) (restartButton.getLocY() + 550), MyGdxGame.ScoreType.END, Color.WHITE);
        }
        MyGdxGame.getScoreFont3().draw(batch, "Best:" + MyGdxGame.getPlayer().getPrefs().getInteger("highScore"), restartButton.getLocX() - MyGdxGame.getHighScoreLayout().width / 2, (float) (restartButton.getLocY() + 267));
    }

    public void dispose(){
        restartButton.dispose();
        menuImg.dispose();
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }
}

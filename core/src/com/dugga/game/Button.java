package com.dugga.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by kenthall on 8/8/15.
 */
public class Button {
    public enum Type{
        RESTART
    }
    private Texture buttonImg;
    private int width;
    private int height;
    private int locX;
    private int locY;
    private Vector2 loc;
    private Type buttonType;
    private Rectangle hitBox;
    private double growWidth;
    private double growHeight;
    private boolean doneGrowing;
    private Sprite buttonSprite;
    private InputProcessor inputProcessor;
    private boolean buttonDown;
    private boolean goButton;
    private Sound buttonSound;
    public Button(Type buttonType, int locX, int locY){
        width = 540;
        height=240;
        this.locX=locX;
        this.locY=locY;
        loc=new Vector2(locX-width/2, locY-height/2);
        buttonImg=new Texture("bg.png");
        switch (buttonType){
            case RESTART:
                buttonImg=new Texture("retryButton.png");
                break;
        }
        hitBox=new Rectangle(loc.x, loc.y+height*2, width, height);
        this.buttonType=buttonType;
        growWidth=0;
        growHeight=0;
        buttonSprite=new Sprite(buttonImg, width, height);
        buttonSprite.setOriginCenter();
        buttonSprite.setPosition(loc.x, loc.y);
        inputProcessor=new InputProcessor();
        Gdx.input.setInputProcessor(inputProcessor);
        buttonDown=false;
        doneGrowing=false;
        goButton=false;
        buttonSound=Gdx.audio.newSound(Gdx.files.internal("sounds/boop.wav"));
    }

    public void draw(SpriteBatch batch){
        buttonSprite.setScale((float) growWidth, (float) growHeight);
        buttonSprite.draw(batch);
        if (growWidth<1 && !doneGrowing) {
            growWidth += 0.05;
        }
        else if(growWidth>=1){
            doneGrowing=true;
        }

        if (growHeight<1 && !doneGrowing){
            growHeight+=0.05;
        }
        else if (growHeight>=1){
            doneGrowing=true;
        }
        switch (buttonType){
            case RESTART:
                if (hitBox.contains(Gdx.input.getX(), Gdx.input.getY()) && inputProcessor.touchDown(Gdx.input.getX(), Gdx.input.getY(), 0, 0) && Gdx.input.isTouched()){
                    goButton=true;
                    if (!buttonDown) {
                        growWidth -= 0.1;
                        growHeight -= 0.1;
                        buttonDown = true;
                        buttonSound.play(0.5f);
                    }
                }
                if (inputProcessor.touchUp(Gdx.input.getX(), Gdx.input.getY(), 0, 0)){
                    if (buttonDown) {
                        growWidth += 0.1;
                        growHeight += 0.1;
                        buttonDown=false;
                    }
                    if(hitBox.contains(Gdx.input.getX(), Gdx.input.getY())) {
                        if (!buttonDown && goButton && growWidth>=1 && growHeight>=1) {
                            MyGdxGame.getMainMenu().setStart(false);
                            MyGdxGame.getMainMenu().setCircleWidth(0);
                            MyGdxGame.getMainMenu().setCircleHeight(0);
                            MyGdxGame.getMainMenu().setPlayerSet(true);
                            MyGdxGame.getDeathMenu().dispose();
                            MyGdxGame.getRequestHandler().showAds(IActivityRequestHandler.adState.HIDE);
                        }
                    }
                }
                break;
        }
    }

    public void dispose(){
        buttonImg.dispose();
        buttonSound.dispose();
    }

    public int getLocX(){
        return locX;
    }

    public int getLocY(){
        return locY;
    }
}

package com.dugga.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by student on 7/30/2015.
 */
public class Grid {
    private int width;
    private int height;
    private int boxCount;
    private Rectangle[] hitBox;
    private int[] rand;
    //private int randTwinkle;
    private Random generator;
    private int hitX;
    private int hitY;
    private boolean[] bounceBlock;
    private int blockRarity;
    private ArrayList<Integer> gjPattern;
    private ArrayList<Integer> smilePattern;
    private ArrayList<Integer> tenPattern;
    private ArrayList<Integer> devPattern;
    private ArrayList<Integer> exPattern;
    private ArrayList<Integer> xPattern;
    private Texture squareBck;
    private Texture squareBue;
    private Texture squareG;
    private Texture squareI;
    private Texture squareO;
    private Texture squareP;
    private Texture squareR;
    private Texture squareY;
    private double growWidth;
    private double growHeight;
    private boolean growing;
    private TextureAtlas.AtlasRegion region;
    private Sprite bounceSquare;
    private int emptyBox;
    private boolean changeColor;
    private int randColor;
    private boolean drawInstruct;
    private GlyphLayout moveLayout;
    private BitmapFont moveFont;
    private double fontAlpha;
//    private Texture twinkle1;
//    private Texture twinkle2;
//    private int twinkleCount;
//    private boolean twinkleOn;
//    private int twinkleGo;

    public Grid(int locX, int locY, int width){
        this.width=width;
        height=width;
        generator = new Random();
        rand=new int[45];
        bounceBlock=new boolean [45];
        blockRarity=2;
        gjPattern=new ArrayList<Integer>(Arrays.asList(25, 26, 27, 30, 0, 5, 6, 7, 2, 17, 18, 19, 21, 23, 36, 38, 41, 42, 43));
        smilePattern=new ArrayList<Integer>(Arrays.asList(31, 1, 33, 3, 20, 36, 37, 38, 24));
        tenPattern=new ArrayList<Integer>(Arrays.asList(25, 30, 0, 27, 28, 29, 32, 34, 2, 3, 4, 10, 11, 12, 13, 14, 20, 35, 40, 22, 23, 24, 37, 39, 42, 43, 44));
        devPattern=new ArrayList<Integer>(Arrays.asList(25, 28, 30, 32, 0, 1, 5, 7, 10, 13, 22, 24, 37, 38, 39, 42, 44));
        exPattern=new ArrayList<Integer>(Arrays.asList(1, 3, 6, 8, 11, 13, 21, 23));
        xPattern=new ArrayList<Integer>(Arrays.asList(0, 4, 6, 8, 12, 16, 18, 20, 24));
        squareBck=new Texture("square/squareBlack.png");
        squareBue=new Texture("square/squareBlue.png");
        squareG=new Texture("square/squareGreen.png");
        squareI=new Texture("square/squareIndigo.png");
        squareO=new Texture("square/squareOrange.png");
        squareP=new Texture("square/squarePurple.png");
        squareR=new Texture("square/squareRed.png");
        squareY=new Texture("square/squareYellow.png");
        growWidth=0;
        growHeight=0;
        fontAlpha=0;
        changeColor=false;
        drawInstruct=true;
        moveLayout=new GlyphLayout();
        moveFont=new BitmapFont(Gdx.files.internal("fonts/scoreFont3.fnt"),Gdx.files.internal("fonts/scoreFont3.png"),false);
        moveFont.getData().setScale(0.75f, 0.9f);
        moveLayout.setText(moveFont, "Tilt to Move Stan");
//        twinkleCount=0;
//        twinkleOn=true;
//        twinkle1=new Texture("twinkle/twinkle1.png");
//        twinkle2=new Texture("twinkle/twinkle2.png");
        //initialize rand, randTwinkle, and bounceBlock
        for(int i=0; i<rand.length;i++){
            rand[i]=2;
            rand[25]=0;
            rand[26]=0;
            rand[27]=0;
            rand[30]=0;
            rand[0]=0;
            rand[5]=0;
            rand[6]=0;
            rand[7]=0;
            rand[2]=0;
            rand[15]=0;
            rand[16]=0;
            rand[17]=0;
            rand[20]=0;
            rand[22]=0;
            rand[35]=0;
            rand[37]=0;
            rand[4]=0;
            rand[9]=0;
            rand[14]=0;
            rand[19]=0;
            rand[39]=0;
            rand[40]=0;
            rand[41]=0;
            rand[42]=0;
            bounceBlock[i]=false;
        }
    }

    public void draw(SpriteBatch batch) {
        createHitBoxes(width / 5, height / 5, batch);
        updateGround();
        blockTransition();

        if (drawInstruct) {
            moveFont.setColor(1, 1, 1, (float)fontAlpha);
            moveFont.draw(batch, "Tilt to Move Stan", Gdx.graphics.getWidth() / 2 - moveLayout.width / 2, Gdx.graphics.getHeight() - 200 - moveLayout.height / 2);

            if (fontAlpha<0.7 && MyGdxGame.getPlayer().getScore()<=1 && !MyGdxGame.getPlayer().getDead()){
                fontAlpha+=0.05;
            }
        }

        if (MyGdxGame.getPlayer().getScore()>1 || MyGdxGame.getPlayer().getDead()){
            if (fontAlpha>0){
                fontAlpha-=0.1;
            }
            else if (fontAlpha<=0) {
                drawInstruct = false;
            }
        }

//        if (twinkleCount<10){
//            twinkleCount++;
//        }
//        else if (twinkleCount>=10){
//            twinkleOn=!twinkleOn;
//            twinkleCount=0;
//        }
    }

    public void createHitBoxes(int width, int height, SpriteBatch batch) {
        int centerX = Gdx.graphics.getWidth() / 2;
        int centerY = Gdx.graphics.getHeight() / 2;
        int row1a = centerY + 4 * height;
        int row1b = centerY + 3 * height;
        int row1 = centerY + 2 * height;
        int row2 = centerY + height;
        int row3 = centerY;
        int row4 = centerY - height;
        int row5 = centerY - 2 * height;
        int row5a = centerY - 3 * height;
        int row5b = centerY - 4 * height;
        int column1 = centerX - 2 * width;
        int column2 = centerX - width;
        int column3 = centerX;
        int column4 = centerX + width;
        int column5 = centerX + 2 * width;

        hitBox = new Rectangle[45];
        for (boxCount = 0; boxCount < hitBox.length; boxCount++) {
            switch (boxCount) {
                case 0:
                    hitY = row1 - width / 2;
                    hitX = column1 - height / 2;
                    /*if (rand[0]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[0]=true;
                    }*/
                    break;
                case 1:
                    hitY = row1 - width / 2;
                    hitX = column2 - height / 2;
                    /*if (rand[1]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[1]=true;
                    }*/
                    break;
                case 2:
                    hitY = row1 - width / 2;
                    hitX = column3 - height / 2;
                    /*if (rand[2]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[2]=true;
                    }*/
                    break;
                case 3:
                    hitY = row1 - width / 2;
                    hitX = column4 - height / 2;
                    /*if (rand[3]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[3]=true;
                    }*/
                    break;
                case 4:
                    hitY = row1 - width / 2;
                    hitX = column5 - height / 2;
                    /*if (rand[4]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[4]=true;
                    }*/
                    break;
                case 5:
                    hitY = row2 - width / 2;
                    hitX = column1 - height / 2;
                    /*if (rand[5]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[5]=true;
                    }*/
                    break;
                case 6:
                    hitY = row2 - width / 2;
                    hitX = column2 - height / 2;
                    /*if (rand[6]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[6]=true;
                    }*/
                    break;
                case 7:
                    hitY = row2 - width / 2;
                    hitX = column3 - height / 2;
                    /*if (rand[7]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[7]=true;
                    }*/
                    break;
                case 8:
                    hitY = row2 - width / 2;
                    hitX = column4 - height / 2;
                    /*if (rand[8]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[8]=true;
                    }*/
                    break;
                case 9:
                    hitY = row2 - width / 2;
                    hitX = column5 - height / 2;
                    /*if (rand[9]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[9]=true;
                    }*/
                    break;
                case 10:
                    hitY = row3 - width / 2;
                    hitX = column1 - height / 2;
                    /*if (rand[10]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[10]=true;
                    }*/
                    break;
                case 11:
                    hitY = row3 - width / 2;
                    hitX = column2 - height / 2;
                    /*if (rand[11]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[11]=true;
                    }*/
                    break;
                case 12:
                    hitY = row3 - width / 2;
                    hitX = column3 - height / 2;
                    /*if (rand[12]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[12]=true;
                    }*/
                    break;
                case 13:
                    hitY = row3 - width / 2;
                    hitX = column4 - height / 2;
                    /*if (rand[13]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[13]=true;
                    }*/
                    break;
                case 14:
                    hitY = row3 - width / 2;
                    hitX = column5 - height / 2;
                    /*if (rand[14]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[14]=true;
                    }*/
                    break;
                case 15:
                    hitY = row4 - width / 2;
                    hitX = column1 - height / 2;
                    /*if (rand[15]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[15]=true;
                    }*/
                    break;
                case 16:
                    hitY = row4 - width / 2;
                    hitX = column2 - height / 2;
                    /*if (rand[16]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[16]=true;
                    }*/
                    break;
                case 17:
                    hitY = row4 - width / 2;
                    hitX = column3 - height / 2;
                    /*if (rand[17]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[17]=true;
                    }*/
                    break;
                case 18:
                    hitY = row4 - width / 2;
                    hitX = column4 - height / 2;
                    /*if (rand[18]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[18]=true;
                    }*/
                    break;
                case 19:
                    hitY = row4 - width / 2;
                    hitX = column5 - height / 2;
                    /*if (rand[19]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[19]=true;
                    }*/
                    break;
                case 20:
                    hitY = row5 - width / 2;
                    hitX = column1 - height / 2;
                    /*if (rand[20]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[20]=true;
                    }*/
                    break;
                case 21:
                    hitY = row5 - width / 2;
                    hitX = column2 - height / 2;
                    /*if (rand[21]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[21]=true;
                    }*/
                    break;
                case 22:
                    hitY = row5 - width / 2;
                    hitX = column3 - height / 2;
                    /*if (rand[22]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[22]=true;
                    }*/
                    break;
                case 23:
                    hitY = row5 - width / 2;
                    hitX = column4 - height / 2;
                    /*if (rand[23]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[23]=true;
                    }*/
                    break;
                case 24:
                    hitY = row5 - width / 2;
                    hitX = column5 - height / 2;
                    /*if (rand[24]==0) {
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[24]=true;
                    }*/
                    break;
                case 25:
                    hitY = row1a - width / 2;
                    hitX = column1 - height / 2;
                    /*if (rand[25]==0){
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[25]=true;
                    }*/
                    break;
                case 26:
                    hitY = row1a - width / 2;
                    hitX = column2 - height / 2;
                    /*if (rand[26]==0){
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[26]=true;
                    }*/
                    break;
                case 27:
                    hitY = row1a - width / 2;
                    hitX = column3 - height / 2;
                    /*if (rand[27]==0){
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[27]=true;
                    }*/
                    break;
                case 28:
                    hitY = row1a - width / 2;
                    hitX = column4 - height / 2;
                    /*if (rand[28]==0){
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[28]=true;
                    }*/
                    break;
                case 29:
                    hitY = row1a - width / 2;
                    hitX = column5 - height / 2;

                    emptyBox = boxCount;
                    if (MyGdxGame.getPlayer().getScore() > MyGdxGame.getPlayer().getStartingHighScore() && MyGdxGame.getPlayer().getStartingHighScore() > 0) {
                        MyGdxGame.drawScore(hitX + width / 2, hitY + height / 2, MyGdxGame.ScoreType.SCORE, null);
                    } else {
                        MyGdxGame.drawScore(hitX + width / 2, hitY + height / 2, MyGdxGame.ScoreType.SCORE, Color.BLACK);
                    }
                    break;
                case 30:
                    hitY = row1b - width / 2;
                    hitX = column1 - height / 2;
                    /*if (rand[30]==0){
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[30]=true;
                    }*/
                    break;
                case 31:
                    hitY = row1b - width / 2;
                    hitX = column2 - height / 2;
                    /*if (rand[31]==0){
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[31]=true;
                    }*/
                    break;
                case 32:
                    hitY = row1b - width / 2;
                    hitX = column3 - height / 2;
                    /*if (rand[32]==0){
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[32]=true;
                    }*/
                    break;
                case 33:
                    hitY = row1b - width / 2;
                    hitX = column4 - height / 2;
                    /*if (rand[33]==0){
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[33]=true;
                    }*/
                    break;
                case 34:
                    hitY = row1b - width / 2;
                    hitX = column5 - height / 2;
                    /*if (rand[34]==0){
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[34]=true;
                    }*/
                    break;
                case 35:
                    hitY = row5a - width / 2;
                    hitX = column1 - height / 2;
                    /*if (rand[35]==0){
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[35]=true;
                    }*/
                    break;
                case 36:
                    hitY = row5a - width / 2;
                    hitX = column2 - height / 2;
                    /*if (rand[36]==0){
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[36]=true;
                    }*/
                    break;
                case 37:
                    hitY = row5a - width / 2;
                    hitX = column3 - height / 2;
                    /*if (rand[37]==0){
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[37]=true;
                    }*/
                    break;
                case 38:
                    hitY = row5a - width / 2;
                    hitX = column4 - height / 2;
                    /*if (rand[38]==0){
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[38]=true;
                    }*/
                    break;
                case 39:
                    hitY = row5a - width / 2;
                    hitX = column5 - height / 2;
                    /*if (rand[39]==0){
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[39]=true;
                    }*/
                    break;
                case 40:
                    hitY = row5b - width / 2;
                    hitX = column1 - height / 2;
                    /*if (rand[40]==0){
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[40]=true;
                    }*/
                    break;
                case 41:
                    hitY = row5b - width / 2;
                    hitX = column2 - height / 2;
                    /*if (rand[41]==0){
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[41]=true;
                    }*/
                    break;
                case 42:
                    hitY = row5b - width / 2;
                    hitX = column3 - height / 2;
                    /*if (rand[42]==0){
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[42]=true;
                    }*/
                    break;
                case 43:
                    hitY = row5b - width / 2;
                    hitX = column4 - height / 2;
                    /*if (rand[43]==0){
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[43]=true;
                    }*/
                    break;
                case 44:
                    hitY = row5b - width / 2;
                    hitX = column5 - height / 2;
                    /*if (rand[44]==0){
                        batch.draw(square, hitX, hitY, width, height);
                        bounceBlock[44]=true;
                    }*/
                    break;
                default:
                    hitY = row1 - width / 2;
                    hitX = column1 - height / 2;
                    break;
            }

            if (rand[boxCount] == 0 && emptyBox != boxCount) {
                if (!changeColor) {
                    region = new TextureAtlas.AtlasRegion(squareBck, hitX, hitY, width, height);
                    bounceSquare = new Sprite(region);
                    bounceSquare.setOriginCenter();
                    bounceSquare.setPosition(hitX, hitY);
                    bounceSquare.setScale((float) growWidth, (float) growHeight);
                }
                if (growing) {
                    if (MyGdxGame.getPlayer().getScore() > 10) {
                        changeColor=true;
                        region = new TextureAtlas.AtlasRegion(squareR, hitX, hitY, width, height);
                        bounceSquare = new Sprite(region);
                    }
                    if (MyGdxGame.getPlayer().getScore() > 20) {
                        region = new TextureAtlas.AtlasRegion(squareO, hitX, hitY, width, height);
                        bounceSquare = new Sprite(region);
                    }
                    if (MyGdxGame.getPlayer().getScore() > 30) {
                        region = new TextureAtlas.AtlasRegion(squareY, hitX, hitY, width, height);
                        bounceSquare = new Sprite(region);
                    }
                    if (MyGdxGame.getPlayer().getScore() > 40) {
                        region = new TextureAtlas.AtlasRegion(squareG, hitX, hitY, width, height);
                        bounceSquare = new Sprite(region);
                    }
                    if (MyGdxGame.getPlayer().getScore() > 50) {
                        region = new TextureAtlas.AtlasRegion(squareBue, hitX, hitY, width, height);
                        bounceSquare = new Sprite(region);
                    }
                    if (MyGdxGame.getPlayer().getScore() > 60) {
                        region = new TextureAtlas.AtlasRegion(squareI, hitX, hitY, width, height);
                        bounceSquare = new Sprite(region);
                    }
                    if (MyGdxGame.getPlayer().getScore() > 70) {
                        region = new TextureAtlas.AtlasRegion(squareP, hitX, hitY, width, height);
                        bounceSquare = new Sprite(region);
                    }
                    if (MyGdxGame.getPlayer().getScore() > 80) {
                        if (randColor==0){
                            region = new TextureAtlas.AtlasRegion(squareR, hitX, hitY, width, height);
                            bounceSquare = new Sprite(region);
                        }
                        else if (randColor==1){
                            region = new TextureAtlas.AtlasRegion(squareO, hitX, hitY, width, height);
                            bounceSquare = new Sprite(region);
                        }
                        else if (randColor==2){
                            region = new TextureAtlas.AtlasRegion(squareY, hitX, hitY, width, height);
                            bounceSquare = new Sprite(region);
                        }
                        else if (randColor==3){
                            region = new TextureAtlas.AtlasRegion(squareG, hitX, hitY, width, height);
                            bounceSquare = new Sprite(region);
                        }
                        else if (randColor==4){
                            region = new TextureAtlas.AtlasRegion(squareBue, hitX, hitY, width, height);
                            bounceSquare = new Sprite(region);
                        }
                        else if (randColor==5){
                            region = new TextureAtlas.AtlasRegion(squareI, hitX, hitY, width, height);
                            bounceSquare = new Sprite(region);
                        }
                        else if (randColor==6){
                            region = new TextureAtlas.AtlasRegion(squareP, hitX, hitY, width, height);
                            bounceSquare = new Sprite(region);
                        }
                    }
                }
                bounceSquare.setOriginCenter();
                bounceSquare.setPosition(hitX, hitY);
                bounceSquare.setScale((float) growWidth, (float) growHeight);
                bounceSquare.draw(batch);
                bounceBlock[boxCount] = true;
            }
//            else if (rand[boxCount] != 0 && emptyBox != boxCount){
//                if (randTwinkle == boxCount && twinkleGo==0) {
//                    if (twinkleOn) {
//                        batch.draw(twinkle1, hitX + width / 2 - 45 / 2, hitY + height / 2 - 55 / 2, 45, 55);
//                    } else {
//                        batch.draw(twinkle2, hitX + width / 2 - 45 / 2, hitY + height / 2 - 55 / 2, 45, 55);
//                    }
//                }
//            }

            hitBox[boxCount] = new Rectangle(hitX, hitY, width, height);
            if (MyGdxGame.getPlayer().getHitGround()) {
                if (hitBox[boxCount].overlaps(MyGdxGame.getPlayer().getHitBox())) {
                    if (bounceBlock[boxCount] && MyGdxGame.getPlayer().getDeathChange()) {
                        MyGdxGame.getPlayer().setDead(false);
                        MyGdxGame.getPlayer().setDeathChange(false);
                        if (MyGdxGame.getPlayer().getScore() % 3 == 0 && MyGdxGame.getPlayer().getScore() != 0) {
                            blockRarity += 1;
                        }
                        if (MyGdxGame.getPlayer().getScore() % 1 == 0 && MyGdxGame.getPlayer().getScore() != 0) {
                            if (MyGdxGame.getPlayer().getBounceSpeed() <= 10) {
                                MyGdxGame.getPlayer().setBounceSpeed(MyGdxGame.getPlayer().getBounceSpeed() + 0.08);
                                MyGdxGame.getPlayer().setDustInterval(MyGdxGame.getPlayer().getDustInterval() - 0.0002);
                                MyGdxGame.getPlayer().setSpeedRatio(MyGdxGame.getPlayer().getSpeedRatio()+0.002);
                            }
                        }
                    }
                    else if (MyGdxGame.getPlayer().getDeathChange()) {
                        MyGdxGame.getPlayer().setDead(true);
                    }
                }
            }
            if (bounceBlock[boxCount]) {
                bounceBlock[boxCount] = false;
            }
        }
    }

    //allows me to make a pattern of blocks simply by inputting their numbers into this function through an arraylist
    public void makePattern(ArrayList<Integer> arrayList){
        for (int x=0;x<rand.length;x++){
            rand[x]=2;
        }
        for (int i=0;i<arrayList.size();i++){
            rand[arrayList.get(i)]=0;
        }
    }

    public void updateGround(){
        final int patternChange=generator.nextInt(6);
        if (MyGdxGame.getPlayer().getHitGround()) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                        //makes ball shrink at max width and height
                        if (growWidth >= 0.9 && growHeight >= 0.9) {
                            growing = false;
                        }
                }
                //delays block refresh at something
            }, 0);
        }
        //updates blocks randomly, as long as pattern is not meant to appear
        if (growWidth<=0 && growHeight<=0 && MyGdxGame.getPlayer().getScore()%10 != 0){
            for (int i = 0; i < rand.length; i++) {
                rand[i] = generator.nextInt(blockRarity);
            }
            //add random block, just in case none appear
            rand[generator.nextInt(45)] = 0;

            //randTwinkle=generator.nextInt(45);
            //twinkleGo=generator.nextInt(2);
            randColor=generator.nextInt(7);
        }

        //makes pattern appear every ten points
        if (growWidth<=0 && growHeight<=0 && MyGdxGame.getPlayer().getScore() % 10 == 0 && MyGdxGame.getPlayer().getScore()!=0) {
            if (growWidth >= 0.9 && growHeight >= 0.9) {
                growing = false;
            }
            switch(patternChange){
                //randomizes pattern
                case 0:
                    makePattern(gjPattern);
                    break;
                case 1:
                    makePattern(smilePattern);
                    break;
                case 2:
                    makePattern(tenPattern);
                    break;
                case 3:
                    makePattern(devPattern);
                    break;
                case 4:
                    makePattern(exPattern);
                    break;
                case 5:
                    makePattern(xPattern);
                    break;
            }
//            randTwinkle=generator.nextInt(45);
//            twinkleGo=generator.nextInt(2);
            randColor=generator.nextInt(7);
        }
    }

    public void blockTransition(){
            if (growWidth < 0 && growHeight < 0) {
                growing = true;
            }
            if (growing && growWidth <= 0.9 && growHeight <= 0.9) {
                growWidth += 0.1;
                growHeight += 0.1;
            } else if (!growing) {
                growWidth -= 0.1;
                growHeight -= 0.1;
            }
    }

    public void dispose(){
        squareBck.dispose();
        squareBue.dispose();
        squareG.dispose();
        squareI.dispose();
        squareO.dispose();
        squareP.dispose();
        squareR.dispose();
        squareY.dispose();
        moveFont.dispose();
    }

    public void setGrowWidth(double growWidth){
        this.growWidth=growWidth;
    }

    public void setGrowHeight(double growHeight){
        this.growHeight=growHeight;
    }

    public double getGrowWidth(){
        return growWidth;
    }

    public double getGrowHeight(){
        return growHeight;
    }

    public void setGrowing(boolean growing){
        this.growing=growing;
    }

    public boolean getGrowing(){
        return growing;
    }

    public int getBoxCount(){
        return boxCount;
    }
}

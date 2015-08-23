package com.dugga.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;

public class MyGdxGame extends ApplicationAdapter {
	private static SpriteBatch batch;
    private SpriteBatch splashBatch;
    private static MainMenu mainMenu;
    private static Player player;
    private static Grid grid;
    private static DeathMenu deathMenu;
    private static boolean running=true;
    private static BitmapFont scoreFont;
    private static BitmapFont scoreFont2;
    private static BitmapFont scoreFont3;
    private static BitmapFont endFont;
    private static BitmapFont endFont2;
    private static double scoreFontScale;
    private double endFontScale;
    private static GlyphLayout scoreFontLayout;
    private static GlyphLayout scoreFontLayout2;
    private static GlyphLayout endFontLayout;
    private static GlyphLayout endFontLayout2;
    private static GlyphLayout highScoreLayout;
    private static int count;
    private static boolean scoreWhite;
    private boolean splash;
    private boolean splashFade;
    private Texture splashImg;
    private double splashAlpha;
    private double batchAlpha;
    private static IActivityRequestHandler requestHandler;
    public enum ScoreType{
        SCORE, END
    }

    public MyGdxGame(IActivityRequestHandler handler) {
        requestHandler = handler;
    }

	@Override
	public void create () {
        batch = new SpriteBatch();
        splashBatch=new SpriteBatch();
        mainMenu=new MainMenu();
        scoreFontLayout=new GlyphLayout();
        scoreFontLayout2=new GlyphLayout();
        endFontLayout=new GlyphLayout();
        endFontLayout2=new GlyphLayout();
        highScoreLayout=new GlyphLayout();
        scoreFontScale=1;
        endFontScale=0.1;
        count=0;
        scoreWhite=false;
        splashImg=new Texture("splash.png");
        splashAlpha=0;
        batchAlpha=0;

        scoreFont = new BitmapFont(Gdx.files.internal("fonts/scoreFont.fnt"),Gdx.files.internal("fonts/scoreFont.png"),false);
        scoreFont2=new BitmapFont(Gdx.files.internal("fonts/scoreFont2.fnt"),Gdx.files.internal("fonts/scoreFont2.png"),false);
        scoreFont3=new BitmapFont(Gdx.files.internal("fonts/scoreFont3.fnt"),Gdx.files.internal("fonts/scoreFont3.png"),false);
        scoreFont.getData().setScale((float) scoreFontScale, (float) scoreFontScale);
        scoreFont2.getData().setScale((float) scoreFontScale, (float) scoreFontScale);
        scoreFont3.getData().setScale((float) scoreFontScale, (float) scoreFontScale);

        endFont = new BitmapFont(Gdx.files.internal("fonts/endFont.fnt"),Gdx.files.internal("fonts/endFont.png"),false);
        endFont2=new BitmapFont(Gdx.files.internal("fonts/endFont2.fnt"),Gdx.files.internal("fonts/endFont2.png"),false);
        endFont.getData().setScale((float) endFontScale, (float) endFontScale);
        endFont2.getData().setScale((float) endFontScale, (float) endFontScale);

        splash=true;
        splashFade=false;
    }

	@Override
	public void render () {
        Gdx.gl.glClearColor(1, 1, 1, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        splashBatch.begin();
        if (splash){
            if (!splashFade) {
                Gdx.gl.glClearColor(0, 0, 0, 0);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            }
            else{
                Gdx.gl.glClearColor(1, 1, 1, 0);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            }
            splashBatch.setColor(1, 1, 1, (float) splashAlpha);

            splashBatch.draw(splashImg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            if (splashAlpha<1 && !splashFade){
                splashAlpha+=0.05;
            }
            else if (splashAlpha>0 && splashFade){
                splashAlpha-=0.05;
            }

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    splashFade = true;
                    if (splashAlpha <= 0) {
                        splash = false;
                    }
                }
            }, 2);
        }
        splashBatch.end();
        batch.begin();

        if (!mainMenu.getStart() && !splash){
            batch.setColor(1, 1, 1, (float)batchAlpha);
            scoreFont.getData().setScale(1, 1);
            mainMenu.draw(batch);
            if (batchAlpha<1){
                batchAlpha+=0.05;
            }
        }
        if (mainMenu.getStart() && !splash) {
            if (count<40){
                count++;
            }
            else if (count>=40 && !player.getDead()){
                scoreWhite=!scoreWhite;
                count=0;
            }
            if (!player.getDead()) {
                grid.draw(batch);
                player.draw(batch);

                scoreFontLayout.setText(scoreFont, "" + player.getScore());
                scoreFontLayout2.setText(scoreFont2, "" + player.getScore());
                highScoreLayout.setText(scoreFont3, "Best:" + player.getPrefs().getInteger("highScore"));
                scoreFont.getData().setScale((float) scoreFontScale, (float) scoreFontScale);
                scoreFont2.getData().setScale((float) scoreFontScale, (float) scoreFontScale);
                scoreFont3.getData().setScale((float) scoreFontScale, (float) scoreFontScale);

                if (scoreFontScale < 1) {
                    scoreFontScale += 0.1;
                }
            } else {
                player.draw(batch);
                grid.draw(batch);

                scoreFontLayout.setText(scoreFont, "" + player.getScore());
                scoreFontLayout2.setText(scoreFont2, "" + player.getScore());
                highScoreLayout.setText(scoreFont3, "Best:"+player.getPrefs().getInteger("highScore"));
                scoreFont.getData().setScale((float) scoreFontScale, (float) scoreFontScale);
                scoreFont2.getData().setScale((float) scoreFontScale, (float) scoreFontScale);
                scoreFont3.getData().setScale((float) scoreFontScale, (float) scoreFontScale);

                if (scoreFontScale > 0.1) {
                    scoreFontScale -= 0.1;
                }
            }
            if (player.getWidth() <= 0 && player.getHeight() <= 0 && !player.getDrawCircle()) {
                deathMenu.draw(batch);
                endFontLayout.setText(endFont, "" + player.getScore());
                endFontLayout2.setText(endFont2, "" + player.getScore());
                endFont.getData().setScale((float) endFontScale, (float) endFontScale);
                endFont2.getData().setScale((float) endFontScale, (float) endFontScale);
                if (endFontScale < 1) {
                    endFontScale += 0.05;
                }
            } else {
                endFontLayout.setText(endFont, "" + player.getScore());
                endFontLayout2.setText(endFont2, "" + player.getScore());
                endFont.getData().setScale((float) endFontScale, (float) endFontScale);
                endFont2.getData().setScale((float) endFontScale, (float) endFontScale);
                if (endFontScale > 0.1) {
                    endFontScale -= 0.05;
                }
            }
        }
        batch.end();
	}

    public static void reset(){
        grid=new Grid(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, Gdx.graphics.getWidth());
        deathMenu=new DeathMenu();
    }

    public static void drawScore(float x, float y, ScoreType scoreType, Color color){
        switch (scoreType){
            case SCORE:
                if (color==Color.BLACK) {
                    scoreFont.draw(batch, "" + player.getScore(), x - scoreFontLayout.width / 2, y - scoreFontLayout.height / 2);
                }
                else{
                    if (scoreWhite && scoreFontScale>0.1){
                        scoreFont2.draw(batch, "" + player.getScore(), x - scoreFontLayout2.width / 2, y - scoreFontLayout2.height / 4);
                    }
                    else if (!scoreWhite && scoreFontScale>0.1){
                        scoreFont3.draw(batch, "" + player.getScore(), x - scoreFontLayout2.width / 2, y - scoreFontLayout2.height / 4);
                    }
                }
                break;
            case END:
                if (color==Color.WHITE) {
                    endFont.draw(batch, "" + player.getScore(), x - endFontLayout.width / 2, y - endFontLayout.height/2);
                }
                else{
                    endFont2.draw(batch, "" + player.getScore(), x - endFontLayout2.width / 2, y - endFontLayout2.height / 2);
                }
                break;
        }
    }

    @Override
    public void dispose(){
        batch.dispose();
        player.dispose();
        grid.dispose();
        deathMenu.dispose();
        mainMenu.dispose();

        splashImg.dispose();
        scoreFont.dispose();
        scoreFont2.dispose();
        scoreFont3.dispose();
        endFont.dispose();
        endFont2.dispose();
    }

    public static Player getPlayer(){
        return player;
    }

    public static void setPlayer(Player player){
        MyGdxGame.player=player;
    }

    public static Grid getGrid(){
        return grid;
    }

    public static void setGrid(Grid grid){
        MyGdxGame.grid=grid;
    }

    public static BitmapFont getScoreFont(){
        return scoreFont;
    }

    public static BitmapFont getEndFont(){
        return endFont;
    }

    public static DeathMenu getDeathMenu(){
        return deathMenu;
    }

    public static void setDeathMenu (DeathMenu deathMenu){
        MyGdxGame.deathMenu=deathMenu;
    }

    public static MainMenu getMainMenu(){
        return mainMenu;
    }

    public static void setMainMenu(MainMenu mainMenu){
        MyGdxGame.mainMenu=mainMenu;
    }

    public static BitmapFont getScoreFont3(){
        return scoreFont3;
    }

    public static GlyphLayout getHighScoreLayout(){
        return highScoreLayout;
    }

    public static GlyphLayout getEndFontLayout(){
        return endFontLayout;
    }

    public static IActivityRequestHandler getRequestHandler(){
        return requestHandler;
    }

}

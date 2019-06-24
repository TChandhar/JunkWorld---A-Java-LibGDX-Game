package com.chandhar.junkworld.UI;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import com.chandhar.junkworld.Util;

public class StatusUI extends Window
{
    private Image hpBar;
    private Image mpBar;
    private Image xpBar;
    private ImageButton inventoryButton;

    //Attributes
    private int levelVal = 1;
    private int goldVal = 0;
    private int hpVal = 50;
    private int mpVal = 50;
    private int xpVal = 0;

    public StatusUI() {
        super("stats", Util.UI_SKIN);


        //groups
        WidgetGroup group1 = new WidgetGroup(); //health
        WidgetGroup group2 = new WidgetGroup(); //magic
        WidgetGroup group3 = new WidgetGroup(); // exp

        //Images
        hpBar = new Image(Util.UI_TEXTUREATLAS.findRegion("HP_Bar"));
        mpBar = new Image(Util.UI_TEXTUREATLAS.findRegion("MP_Bar"));
        xpBar = new Image(Util.UI_TEXTUREATLAS.findRegion("XP_Bar"));
        Image bar1 = new Image(Util.UI_TEXTUREATLAS.findRegion("bar"));
        Image bar2 = new Image(Util.UI_TEXTUREATLAS.findRegion("bar"));
        Image bar3 = new Image(Util.UI_TEXTUREATLAS.findRegion("bar"));

        //labels
        Label hpLabel = new Label("hp:", Util.UI_SKIN);
        Label hp = new Label(String.valueOf(hpVal), Util.UI_SKIN);
        Label mpLabel = new Label("mp:", Util.UI_SKIN);
        Label mp = new Label(String.valueOf(mpVal), Util.UI_SKIN);
        Label xpLabel = new Label("xp:", Util.UI_SKIN);
        Label xp = new Label(String.valueOf(xpVal), Util.UI_SKIN);
        Label levelLabel = new Label("Lv:", Util.UI_SKIN);
        Label level = new Label(String.valueOf(levelVal), Util.UI_SKIN);
        Label goldLabel = new Label("gold:", Util.UI_SKIN);
        Label gold = new Label(String.valueOf(goldVal), Util.UI_SKIN);

        //buttons
        inventoryButton = new ImageButton(Util.UI_SKIN, "InventoryButton");
        inventoryButton.getImageCell().size(32, 32);

        //Align Images
        hpBar.setPosition(3, 6);
        mpBar.setPosition(3, 6);
        xpBar.setPosition(3, 6);

        //add to widget groups
        group1.addActor(bar1);
        group1.addActor(hpBar);
        group2.addActor(bar2);
        group2.addActor(mpBar);
        group3.addActor(bar3);
        group3.addActor(xpBar);

        //ADD TO LAYOUT
        //defaults() returns cell comprised default properties for all cells in table
        //expand() allows the cells to contain extra space in both x and y directions
        //fill() allows a WIDGET to be sized to a CELL in both directions
        defaults().expand().fill();
        //page 153

        //account for title padding
        this.pad(this.getPadTop() + 10, 10, 10, 10);

        this.add(); //adds an empty column
        this.add();
        this.add(inventoryButton).align(Align.right);
        this.row(); // skips to next row

        this.add(group1).size(bar1.getWidth(), bar1.getHeight());   //HP BAR
        this.add(hpLabel);                                          //HP LABEL
        this.add(hp).align(Align.left);                             //HP AMOUNT
        this.row();

        this.add(group2).size(bar2.getWidth(), bar2.getHeight());   //MP BAR
        this.add(mpLabel);                                          //MP LABEL
        this.add(mp).align(Align.left);
        this.row();

        this.add(group3).size(bar3.getWidth(), bar3.getHeight());
        this.add(xpLabel);
        this.add(xp).align(Align.left);

        this.add(levelLabel).align(Align.left);
        this.add(level).align(Align.left);
        this.row();

        this.add(goldLabel);
        this.add(gold).align(Align.left);

        this.pack();    //to make sure table sizes itself to width and height
    }

    public ImageButton getInventoryButton()
    {
            return inventoryButton;
    }




}

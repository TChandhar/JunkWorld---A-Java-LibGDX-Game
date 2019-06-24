package com.chandhar.junkworld.UI;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.SnapshotArray;

import com.chandhar.junkworld.Util;
//import com.chandhar.junkworld.InventoryItem;

//used to keep track of how many inventory items are stacked
//contains methods for adding and removing inventory items

public class InventorySlot extends Stack
{
    //defaults for all slots
    private Stack defaultBackground;
    private Image customBackgroundDecal;
    private Label numItemsLabel;
    private int numItemsVal;
    private int filterItemType;

    public InventorySlot()
    {
        filterItemType = 0; //filter nothing
        defaultBackground = new Stack();
        customBackgroundDecal = new Image();
        Image image = new Image(new NinePatch(Util.UI_TEXTUREATLAS.createPatch("dialog")));

        numItemsLabel = new Label(String.valueOf(numItemsVal), Util.UI_SKIN, "inventory-item-count"); //comes from statusui.json
        numItemsLabel.setAlignment(Align.bottomRight);
        numItemsLabel.setVisible(false);

        this.add(defaultBackground);
        this.add(numItemsLabel);
    }

    public boolean gitTest() {
        return false;
    }

    public void gitTest2() {
        return;
    }

    public InventorySlot(int _filterItemType, Image _customBackgroundDecal)
    {
        this();
        filterItemType = _filterItemType;
        customBackgroundDecal = _customBackgroundDecal;
    }


    public void decrementItemCount()
    {
        numItemsVal--;
        numItemsLabel.setText(String.valueOf(numItemsVal));
        if(defaultBackground.getChildren().size == 1)
        {
            defaultBackground.add(customBackgroundDecal);
        }

        //checkVisibilityOfItemCount();
    }

    public void incrementItemCount()
    {
        numItemsVal++;
        numItemsLabel.setText(String.valueOf(numItemsVal));
        if(defaultBackground.getChildren().size > 1)
        {
            defaultBackground.getChildren().pop();  //as long as is one of the item, add to it
        }

        //checkVisibilityOfItemCount();
    }

    @Override
    public void add(Actor actor)
    {
        super.add(actor);

        if(numItemsLabel == null)
        {
            return ;
        }
        if(!actor.equals(defaultBackground) && !actor.equals(numItemsLabel))
        {
            incrementItemCount();
        }
    }

    public void add(Array<Actor> array)
    {
        for(Actor actor: array)
        {
            super.add(actor);

            if(numItemsLabel == null)
            {
                return;
            }

            if(!actor.equals(defaultBackground) && !actor.equals(numItemsLabel))
            {
                incrementItemCount();
            }

        }
    }


}

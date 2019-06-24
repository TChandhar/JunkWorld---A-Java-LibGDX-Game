package com.chandhar.junkworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.chandhar.junkworld.screens.MainGameScreen;

import com.badlogic.gdx.InputProcessor;

public class PlayerInputComponent extends InputComponent implements InputProcessor
{
    private final static String TAG = PlayerInputComponent.class.getSimpleName();
    private Vector3 lastMouseCoordinates;

    public PlayerInputComponent()
    {
        this.lastMouseCoordinates = new Vector3();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void receiveMessage(String message)
    {
        String[] string = message.split(MESSAGE_TOKEN);
        if(string.length == 0) return;

        //specifically for messages with 1 object payload
        if(string.length == 2)
        {
            if(string[0].equalsIgnoreCase(MESSAGE.CURRENT_DIRECTION.toString()))
            {
                currentDirection = json.fromJson(Entity.Direction.class, string[1]);
            }

            else if(string[0].equalsIgnoreCase(MESSAGE.CURRENT_STATE.toString()))
            {
                currentState = json.fromJson(Entity.State.class, string[1]);
            }
            else if(string[0].equalsIgnoreCase(MESSAGE.CURRENT_MOTION.toString()))
            {
                currentMotion = json.fromJson(Entity.Motion.class, string[1]);
            }
        }
    }

    @Override
    public void dispose()
    {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void update(Entity entity, float delta)
    {
        //Keyboard input
        if(keys.get(Keys.LEFT))
        {
            //if player is not pushing down a direction key (IDLE) allow them to change direction and trigger walking
            if(currentState == Entity.State.IDLE)
            {
                entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.WALKING));
                entity.sendMessage(MESSAGE.CURRENT_DIRECTION, json.toJson(Entity.Direction.LEFT));
            }

            //if player is pushing down a direction key but not actually moving (STANDING) i.e. colliding with wall let them change direction
            else if(currentState == Entity.State.WALKING && currentMotion == Entity.Motion.STANDING)
            {
                entity.sendMessage(MESSAGE.CURRENT_DIRECTION, json.toJson(Entity.Direction.LEFT));
            }

        }
        else if(keys.get(Keys.RIGHT))
        {


            if(currentState == Entity.State.IDLE)
            {
                entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.WALKING));
                entity.sendMessage(MESSAGE.CURRENT_DIRECTION, json.toJson(Entity.Direction.RIGHT));
            }
            else if(currentState == Entity.State.WALKING && currentMotion == Entity.Motion.STANDING)
            {

                entity.sendMessage(MESSAGE.CURRENT_DIRECTION, json.toJson(Entity.Direction.RIGHT));
            }
        }

        else if(keys.get(Keys.UP))
        {

            if(currentState == Entity.State.IDLE)
            {
                entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.WALKING));
                entity.sendMessage(MESSAGE.CURRENT_DIRECTION, json.toJson(Entity.Direction.UP));
            }
            else if(currentState == Entity.State.WALKING && currentMotion == Entity.Motion.STANDING)
            {
                entity.sendMessage(MESSAGE.CURRENT_DIRECTION, json.toJson(Entity.Direction.UP));
            }
        }
        else if(keys.get(Keys.DOWN))
        {
            if(currentState == Entity.State.IDLE)
            {
                entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.WALKING));
                entity.sendMessage(MESSAGE.CURRENT_DIRECTION, json.toJson(Entity.Direction.DOWN));
            }
            else if(currentState == Entity.State.WALKING && currentMotion == Entity.Motion.STANDING)
            {
                entity.sendMessage(MESSAGE.CURRENT_DIRECTION, json.toJson(Entity.Direction.DOWN));
            }

        }
        else if(keys.get(Keys.QUIT))
        {
            Gdx.app.exit();
        }
        else
        {
            //if player is not pushing any arrows keys and player is walking but not moving go to idle
            //do not allow idle if motion is moving as will allow intertile movement DO NEVER ALLOW INTERTILE MOVEMENT
            if(currentState == Entity.State.WALKING && currentMotion == Entity.Motion.STANDING)
            {
                entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.IDLE));
            }


            if(currentState == null)
            {
                //if entity is standing set to idle
                entity.sendMessage(MESSAGE.CURRENT_STATE, json.toJson(Entity.State.IDLE));
            }
            if(currentDirection == null)
            {
                entity.sendMessage(MESSAGE.CURRENT_DIRECTION, json.toJson(Entity.Direction.DOWN));
            }

            if(currentMotion == null)
            {
                entity.sendMessage(MESSAGE.CURRENT_MOTION, json.toJson(Entity.Motion.STANDING));
            }

        }

        if(keys.get(Keys.J))
        {
            //if(currentState == Entity.State.IDLE)
                if(currentMotion == Entity.Motion.STANDING)
                    //run entity.mine
                    entity.sendMessage(MESSAGE.MINING_PHYSICS, json.toJson(true));

            //entity.sendMessage(MESSAGE.MINE);
        }

        //Mouse Input
        if(mouseButtons.get(Mouse.SELECT))
        {
            entity.sendMessage(MESSAGE.INIT_SELECT_ENTITY, json.toJson(lastMouseCoordinates));
            mouseButtons.put(Mouse.SELECT, false);
        }
    }

    @Override
    public boolean keyDown(int keycode)
    {
        if(keycode == Input.Keys.LEFT || keycode == Input.Keys.A)
        {
            this.leftPressed();
        }
        if(keycode == Input.Keys.RIGHT || keycode == Input.Keys.D)
        {
            this.rightPressed();
        }
        if(keycode == Input.Keys.UP || keycode == Input.Keys.W)
        {
            this.upPressed();
        }
        if(keycode == Input.Keys.DOWN || keycode == Input.Keys.S)
        {
            this.downPressed();
        }

        if(keycode == Input.Keys.J)
        {
            this.minePressed();
        }

        if(keycode == Input.Keys.Q)
        {
            this.quitPressed();
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        if(keycode == Input.Keys.LEFT || keycode == Input.Keys.A)
        {
            this.leftReleased();
        }
        if(keycode == Input.Keys.RIGHT || keycode == Input.Keys.D)
        {
            this.rightReleased();
        }
        if(keycode == Input.Keys.UP || keycode == Input.Keys.W)
        {
            this.upReleased();
        }
        if(keycode == Input.Keys.DOWN || keycode == Input.Keys.S)
        {
            this.downReleased();
        }

        if(keycode == Input.Keys.J)
        {
            this.mineReleased();
        }
        if(keycode == Input.Keys.Q)
        {
            this.quitReleased();
        }

        return true;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
        if (button == Input.Buttons.LEFT || button == Input.Buttons.RIGHT)
        {
            this.setClickedMouseCoordinates(screenX, screenY);
        }

        //Left is selection, right is context menu
        if (button == Input.Buttons.LEFT)
        {
            this.selectMouseButtonPressed(screenX, screenY);
        }

        if (button == Input.Buttons.RIGHT)
        {
            this.doActionButtonPressed(screenX, screenY);
        }
        return true;
    }

    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        //Left is selection, right is context menu
        if (button == Input.Buttons.LEFT)
        {
            this.selectMouseButtonReleased(screenX, screenY);
        }

        if (button == Input.Buttons.RIGHT)
        {
            this.doActionButtonReleased(screenX, screenY);
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
        return false;
    }

    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }

    //Key pressed
    public void leftPressed()
    {
        keys.put(Keys.LEFT, true);
    }

    public void rightPressed()
    {
        keys.put(Keys.RIGHT, true);
    }

    public void upPressed()
    {
        keys.put(Keys.UP, true);
    }

    public void downPressed()
    {
        keys.put(Keys.DOWN, true);
    }

    public void minePressed(){keys.put(Keys.J, true);}

    public void quitPressed()
    {
        keys.put(Keys.QUIT, true);
    }

    public void setClickedMouseCoordinates(int x, int y)
    {
        lastMouseCoordinates.set(x,y,0);
    }

    public void selectMouseButtonPressed(int x, int y)
    {
        mouseButtons.put(Mouse.SELECT, true);
    }

    public void doActionButtonPressed(int x, int y)
    {
        mouseButtons.put(Mouse.DOACTION, true);
    }

    //RELEASES

    public void leftReleased()
    {
        keys.put(Keys.LEFT, false);
    }

    public void rightReleased()
    {
        keys.put(Keys.RIGHT, false);
    }

    public void upReleased()
    {
        keys.put(Keys.UP, false);
    }

    public void downReleased()
    {
        keys.put(Keys.DOWN, false);
    }

    public void mineReleased(){keys.put(Keys.J,false);}

    public void quitReleased()
    {
        keys.put(Keys.QUIT, false);
    }

    public void selectMouseButtonReleased(int x, int y)
    {
        mouseButtons.put(Mouse.SELECT, false);
    }

    public void doActionButtonReleased(int x, int y)
    {
        mouseButtons.put(Mouse.DOACTION, false);
    }

    public static void clear()
    {
        keys.put(Keys.LEFT, false);
        keys.put(Keys.RIGHT, false);
        keys.put(Keys.UP, false);
        keys.put(Keys.DOWN, false);
        keys.put(Keys.J,false);
        keys.put(Keys.QUIT, false);
    }


}

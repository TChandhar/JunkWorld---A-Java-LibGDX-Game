package com.chandhar.junkworld;

import aurelienribon.tweenengine.TweenAccessor;



public class FloatAccessor implements TweenAccessor<Float>
{
    public static final int TYPE_XY = 1;

    @Override
    public int getValues(Float target, int tweenType, float[]returnValues)
    {
        switch(tweenType)
        {
            case TYPE_XY:
                returnValues[0]= target;
                return 1;

            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(Float target, int tweenType, float[]newValues)
    {
        switch(tweenType)
        {
            case TYPE_XY:
                target = newValues[0];
                break;
            default:
                assert false;
                break;
        }
    }
}

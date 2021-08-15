package me.thesilverecho.zeropoint.api.render.layer;

public record CustomRenderRunnable(String name, Runnable startAction, Runnable finishAction)
{
}

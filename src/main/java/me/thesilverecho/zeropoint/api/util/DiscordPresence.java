/*
package me.thesilverecho.zeropoint.api.util;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class DiscordPresence
{
	private static final DiscordRichPresence discordRichPresence = new DiscordRichPresence();
	private static final DiscordRPC discordRPC = DiscordRPC.INSTANCE;

	public static void startRPC()
	{
		DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
		eventHandlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + var1 + ", var2: " + var2));

		String discordID = "876838190663741520";
		discordRPC.Discord_Initialize(discordID, eventHandlers, true, null);

		discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
		discordRichPresence.largeImageKey = "zplogo1";
		discordRichPresence.largeImageText = "Zero-point client";
		discordRichPresence.details = "Development of client";
		discordRichPresence.state = null;
		discordRPC.Discord_UpdatePresence(discordRichPresence);
	}

	public static void stopRPC()
	{
		discordRPC.Discord_Shutdown();
		discordRPC.Discord_ClearPresence();
	}
}
*/

package me.thesilverecho.zeropoint.api.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import me.thesilverecho.zeropoint.api.notification.Notification;
import me.thesilverecho.zeropoint.api.notification.NotificationManager;
import me.thesilverecho.zeropoint.api.notification.NotificationType;

import java.util.ArrayList;
import java.util.Collection;

public class TrackManager extends AudioEventAdapter
{
	private static final ArrayList<AudioTrack> ALL_AUDIO_TRACKS = new ArrayList<>();

	private int currentTrack = 0;

	public static void addAllTracks(AudioPlayer audioPlayer, Collection<? extends AudioTrack> tracks)
	{
		tracks.forEach(track -> addTrack(audioPlayer, track));
	}

	public static void addTrack(AudioPlayer player, AudioTrack track)
	{
		if (track == null)
			return;
		if (player.startTrack(track, true))
		{
			if (ALL_AUDIO_TRACKS.contains(track) || player.getPlayingTrack() == track)
				track = track.makeClone();

			ALL_AUDIO_TRACKS.add(track);
		}
	}

	public void skipToTrack(AudioPlayer player, int index)
	{
		if (ALL_AUDIO_TRACKS.size() > index)
		{
			final AudioTrack track = ALL_AUDIO_TRACKS.get(index);
			if (track != null && !track.equals(player.getPlayingTrack()))
			{
				player.playTrack(track);
				currentTrack = index;
			}
		}
	}

	public void skipTrackForward(AudioPlayer player)
	{
		skipToTrack(player, currentTrack++);
	}

	public void testDisplay()
	{
		ALL_AUDIO_TRACKS.forEach(track ->
		{
			System.out.println(track.getInfo().title);
		});
	}

	@Override
	public void onPlayerPause(AudioPlayer player)
	{
		player.setPaused(true);
	}

	@Override
	public void onPlayerResume(AudioPlayer player)
	{
		player.setPaused(false);
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track)
	{
		NotificationManager.INSTANCE.addNotification(Notification.Builder.builder("Music", "Now playing: " + track.getInfo().title + ".").setTimeInSeconds(3).setType(NotificationType.INFO).build());
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason)
	{
		if (endReason.mayStartNext)
			skipToTrack(player, currentTrack + 1);
	}

	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception)
	{
		NotificationManager.INSTANCE.addNotification(Notification.Builder.builder("Music", "Music player encountered an error." + exception).setType(NotificationType.ERROR).build());
	}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs)
	{
		NotificationManager.INSTANCE.addNotification(Notification.Builder.builder("Music", "Music player stuck for: " + thresholdMs + ".").setType(NotificationType.ERROR).build());
		skipToTrack(player, currentTrack + 1);
	}

}

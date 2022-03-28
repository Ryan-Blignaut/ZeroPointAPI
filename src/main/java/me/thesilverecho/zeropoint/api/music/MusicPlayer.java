package me.thesilverecho.zeropoint.api.music;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.AudioPlayerInputStream;
import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import me.thesilverecho.zeropoint.api.notification.Notification;
import me.thesilverecho.zeropoint.api.notification.NotificationManager;
import me.thesilverecho.zeropoint.api.notification.NotificationType;
import me.thesilverecho.zeropoint.api.util.ZeroPointApiLogger;

import javax.sound.sampled.*;
import java.io.IOException;

public enum MusicPlayer
{
	INSTANCE;

	private static MusicPlayer player;
	private final AudioPlayer audioPlayer;
	private final TrackManager trackManager;
	private float[] samples;

	MusicPlayer()
	{

		DefaultAudioPlayerManager defaultAudioPlayerManager = new DefaultAudioPlayerManager();
		defaultAudioPlayerManager.getConfiguration().setOutputFormat(StandardAudioDataFormats.COMMON_PCM_S16_BE);
		AudioSourceManagers.registerRemoteSources(defaultAudioPlayerManager);
		this.audioPlayer = defaultAudioPlayerManager.createPlayer();
		this.trackManager = new TrackManager();
		this.audioPlayer.addListener(trackManager);

		final String identifier = "https://www.youtube.com/watch?v=XR1kx-_mXg4&list=RDXR1kx-_mXg4&start_radio=1";/*"https://music.youtube.com/playlist?list=PL63ZO-jXFTasqvj7WdEFQ6QtG6UBrl9CR";*///https://music.youtube.com/watch?v=NZ_zGP9xE5M&list=RDAMVMZ0eX4gzCGm8";
		defaultAudioPlayerManager.loadItem(identifier, new AudioLoadResultHandler()
		{
			@Override
			public void trackLoaded(AudioTrack track)
			{
				TrackManager.addTrack(audioPlayer, track);
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist)
			{
				TrackManager.addAllTracks(audioPlayer, playlist.getTracks());
			}

			@Override
			public void noMatches()
			{
				NotificationManager.INSTANCE.addNotification(Notification.Builder.builder("Music", "No matches found.").setType(NotificationType.WARNING).build());
			}

			@Override
			public void loadFailed(FriendlyException exception)
			{
				NotificationManager.INSTANCE.addNotification(Notification.Builder.builder("Music", "Load failed. " + exception).setType(NotificationType.WARNING).build());
			}
		});


		final Thread thread = new Thread(() ->
		{

			final AudioDataFormat format = StandardAudioDataFormats.COMMON_PCM_S16_LE;
			final long frameDuration = format.frameDuration();

			final AudioInputStream stream = AudioPlayerInputStream.createStream(audioPlayer, format, frameDuration, false);


			final DataLine.Info info = new DataLine.Info(SourceDataLine.class, stream.getFormat());
			samples = new float[format.totalSampleCount()];
			try (SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info))
			{
				line.open(stream.getFormat());
				line.start();
				final byte[] buffer = new byte[format.chunkSampleCount * format.channelCount * 2];

				for (int chunkSize; (chunkSize = stream.read(buffer)) > -1; )
				{
					while (audioPlayer.isPaused()) Thread.sleep(100);
					SimpleAudioConversion.decode(buffer, samples, chunkSize, stream.getFormat());
					line.write(buffer, 0, chunkSize);
				}

				audioPlayer.destroy();
				line.drain();
				line.stop();
			} catch (LineUnavailableException | IOException | InterruptedException e)
			{
				ZeroPointApiLogger.error("There was an error in the music player.", e);
			}

		}, "Music Thread");
		thread.start();


	}

	public AudioTrack getCurrentSong()
	{
		return audioPlayer.getPlayingTrack();
	}

	public void togglePause()
	{
		audioPlayer.setPaused(!audioPlayer.isPaused());
	}

	public void skipSong()
	{
		trackManager.skipTrackForward(audioPlayer);
		trackManager.testDisplay();
	}


	public void play()
	{
		trackManager.skipToTrack(audioPlayer, 0);
	}


	public float[] getSamples()
	{
		return samples;
	}
}

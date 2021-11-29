package me.thesilverecho.zeropoint.api.music;

import com.sedmelluq.discord.lavaplayer.format.AudioDataFormat;
import com.sedmelluq.discord.lavaplayer.format.AudioPlayerInputStream;
import com.sedmelluq.discord.lavaplayer.format.Pcm16AudioDataFormat;
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

import javax.sound.sampled.*;
import java.io.IOException;

public class MusicPlayer
{
	private static MusicPlayer player;
	private final AudioPlayer audioPlayer;
	private final TrackManager trackManager;

	public MusicPlayer()
	{
		DefaultAudioPlayerManager defaultAudioPlayerManager = new DefaultAudioPlayerManager();
		defaultAudioPlayerManager.getConfiguration().setOutputFormat(StandardAudioDataFormats.COMMON_PCM_S16_BE);
		AudioSourceManagers.registerRemoteSources(defaultAudioPlayerManager);
		this.audioPlayer = defaultAudioPlayerManager.createPlayer();
		this.trackManager = new TrackManager();
		this.audioPlayer.addListener(trackManager);


		final String identifier = "https://music.youtube.com/playlist?list=PL63ZO-jXFTasqvj7WdEFQ6QtG6UBrl9CR";//https://music.youtube.com/watch?v=NZ_zGP9xE5M&list=RDAMVMZ0eX4gzCGm8";
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
			AudioDataFormat format = new Pcm16AudioDataFormat(2, 44100, StandardAudioDataFormats.COMMON_PCM_S16_BE.chunkSampleCount, false);
			AudioInputStream stream = AudioPlayerInputStream.createStream(audioPlayer, format, 100L, true);
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, stream.getFormat());
			try (SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info))
			{
				line.open(stream.getFormat());
				line.start();

				byte[] buffer = new byte[StandardAudioDataFormats.COMMON_PCM_S16_BE.maximumChunkSize() + 1024];
				int chunkSize;

				while ((chunkSize = stream.read(buffer)) > 0)
				{
				/*	while (audioPlayer.isPaused())
					{
						try
						{
							Thread.sleep(10);
						} catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}*/

					line.write(buffer, 0, chunkSize);
				}
				System.out.println(line.getLevel());
				audioPlayer.destroy();
				line.drain();
				line.stop();
			} catch (LineUnavailableException | IOException e)
			{
				e.printStackTrace();
			}

		}, "Music Thread");
		thread.setPriority(2);
		thread.start();


	}

	public AudioTrack getCurrentSong()
	{
		return audioPlayer.getPlayingTrack();
	}

	public void skipSong()
	{
//		trackManager.skipTrackForward(audioPlayer);
		trackManager.testDisplay();
	}


	public void play()
	{
		trackManager.skipToTrack(audioPlayer, 0);
	}

	public static void load()
	{
		player = new MusicPlayer();
	}

	public static MusicPlayer getPlayer()
	{
		return player;
	}
}

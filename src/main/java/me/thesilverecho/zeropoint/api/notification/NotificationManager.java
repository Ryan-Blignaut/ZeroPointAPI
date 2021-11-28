package me.thesilverecho.zeropoint.api.notification;

import net.minecraft.client.util.math.MatrixStack;

import java.util.concurrent.LinkedBlockingQueue;

public class NotificationManager
{
	public static final NotificationManager INSTANCE = new NotificationManager();

	private final LinkedBlockingQueue<Notification> notificationQueue = new LinkedBlockingQueue<>();

	//Initial notification, with temporary values.
	private Notification notification = Notification.Builder.builder(null, null).setShowing(false).build();

	public void addNotification(Notification notification)
	{
		notificationQueue.add(notification);
	}

	public void onTick()
	{
		//Tick the current notification.
		notification.update();
		//On startup the notification
		if (!notificationQueue.isEmpty() && !notification.isShowing())
			notification = notificationQueue.poll();
	}

	public void onRender(MatrixStack matrixStack)
	{
		if (notification.isShowing())
			notification.render(matrixStack);
	}
}

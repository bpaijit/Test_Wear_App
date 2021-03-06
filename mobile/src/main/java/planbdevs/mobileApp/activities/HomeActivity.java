package planbdevs.mobileApp.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import planbdevs.mobileApp.R;


public class HomeActivity extends ActionBarActivity
{
	private static final String EXTRA_EVENT_ID = "1";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		sendNotification();
	}

	private void sendNotification()
	{
		int notificationId = 001;
		int eventId = 1;

		// Build intent for notification content
		Intent viewIntent = new Intent(this, ViewEventActivity.class);
		viewIntent.putExtra(EXTRA_EVENT_ID, eventId);
		PendingIntent viewPendingIntent = PendingIntent.getActivity(this, 0, viewIntent, 0);

		NotificationCompat.Builder notificationBuilder =
				new NotificationCompat.Builder(this)
						.setSmallIcon(R.drawable.common_signin_btn_icon_dark)
						.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.abc_list_selector_background_transition_holo_dark))
						.setStyle(new NotificationCompat.BigPictureStyle())
						.setContentTitle("Test")
						.setContentText("Test Notification")
						.setContentIntent(viewPendingIntent)
						.addAction(R.drawable.abc_ic_go, "Hi", viewPendingIntent);

		// Get an instance of the NotificationManager service
		NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

		// Build the notification and issues it with notification manager.
		notificationManager.notify(notificationId, notificationBuilder.build());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

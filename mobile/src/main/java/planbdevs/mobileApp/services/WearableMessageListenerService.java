package planbdevs.mobileApp.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import planbdevs.mobileApp.activities.BidActivity;

/**
 * Created by Bryan on 7/20/2014.
 */
public class WearableMessageListenerService extends WearableListenerService
{
	private final static String TAG = "WearableMessageListenerService";
	private final static String RECEIVED_PATH = "/bid_received";

	GoogleApiClient mGoogleApiClient = null;

	@Override
	public void onCreate()
	{
		super.onCreate();

		mGoogleApiClient = new GoogleApiClient.Builder(this)
			.addApi(Wearable.API)
			.build();
		mGoogleApiClient.connect();

	}

	@Override
	public void onMessageReceived(MessageEvent messageEvent)
	{
		if (messageEvent != null)
		{
			if (RECEIVED_PATH.equals(messageEvent.getPath()))
			{
				DataMap data = DataMap.fromByteArray(messageEvent.getData());

				Intent iBid = new Intent(this, BidActivity.class);
				iBid.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				Bundle bundle = new Bundle();
				bundle.putInt("id", data.getInt("id"));
				bundle.putFloat("bid", data.getFloat("bid"));

				iBid.putExtra("extras", bundle);

				startActivity(iBid);
			}

		}
	}
}

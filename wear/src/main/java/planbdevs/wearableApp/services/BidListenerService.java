package planbdevs.wearableApp.services;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import planbdevs.wearableApp.activities.BidActivity;
import planbdevs.wearableApp.classes.AuctionItemWear;


public class BidListenerService extends WearableListenerService
{
	private static final String TAG = "BidListenerService";
	private static final String START_ACTIVITY_PATH = "/start-bid-activity";
	private static final String DATA_ITEM_RECEIVED_PATH = "/data-item-received";

	GoogleApiClient mGoogleApiClient;

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
	public void onDataChanged(DataEventBuffer dataEvents)
	{
		//Get the passed in data items
		final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
		dataEvents.close();

		//Make sure the app is connected to the Wearable API
		if (!mGoogleApiClient.isConnected())
		{
			ConnectionResult connectionResult = mGoogleApiClient.blockingConnect(30, TimeUnit.SECONDS);

			if (!connectionResult.isSuccess())
			{
				Log.e(TAG, "BidListenerService failed to connect to GoogleApiClient.");
				return;
			}
		}

		//Find the node that sent the data
		String nodeId = "";

		for(DataEvent event : events)
		{
			Uri uri = event.getDataItem().getUri();

			if(uri != null)
			{
				nodeId = uri.getHost();
			}

			if (!nodeId.isEmpty())
			{
				Wearable.MessageApi.sendMessage(mGoogleApiClient, nodeId, DATA_ITEM_RECEIVED_PATH, null);
			}
		}


	}

	@Override
	public void onMessageReceived(MessageEvent messageEvent)
	{
		LogD("onMessageReceived");

		if (messageEvent != null && messageEvent.getPath().equals(START_ACTIVITY_PATH))
		{

			Intent iBid = new Intent(this, BidActivity.class);
			Bundle bundle = new Bundle();
			DataMap data = DataMap.fromByteArray(messageEvent.getData());
			bundle.putInt("id", data.getInt("id"));
			bundle.putFloat("bid", data.getFloat("bid"));

			iBid.putExtra("extras", bundle);
			iBid.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(iBid);
		}
	}

	public static void LogD(String message)
	{
		if (Log.isLoggable(TAG, Log.DEBUG))
		{
			Log.d(TAG,message);
		}
	}

	private AuctionItemWear convertToObject(byte[] bytes)
	{
		AuctionItemWear returnObject = null;

		try
		{
			ByteArrayInputStream in = new ByteArrayInputStream(bytes);
			ObjectInputStream stream = new ObjectInputStream(in);
			Object obj = stream.readObject();
			returnObject = (AuctionItemWear) stream.readObject();

		}
		catch (Exception ex)
		{
			Log.e(TAG, ex.getMessage());

		}

		return returnObject;
	}
}

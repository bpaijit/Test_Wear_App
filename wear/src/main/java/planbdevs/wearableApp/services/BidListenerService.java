package planbdevs.wearableApp.services;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
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
		LogD("onDataChanged");
	}

	@Override
	public void onMessageReceived(MessageEvent messageEvent)
	{
		LogD("onMessageReceived");

		if (messageEvent != null && messageEvent.getPath().equals(START_ACTIVITY_PATH))
		{

			Intent iBid = new Intent(this, BidActivity.class);
			Bundle bundle = new Bundle();
			byte[] messageData = messageEvent.getData();
			ByteBuffer buffer = ByteBuffer.wrap(messageData);

			DataMap data = DataMap.fromByteArray(buffer.array());
			bundle.putInt("id", data.getInt("id"));
			bundle.putFloat("bid", data.getFloat("bid"));
			//bundle.putByteArray("image", getBitmapBytesFromAsset(data.getAsset("image")));

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

	private byte[] getBitmapBytesFromAsset(Asset asset)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Bitmap bitmap = loadBitmapFromAsset(asset);

		if (bitmap != null)
		{
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		}

		return stream.toByteArray();
	}
	private Bitmap loadBitmapFromAsset(Asset asset)
	{
		Bitmap returnBitmap = null;

		if (asset != null)
		{
			InputStream assetStream = Wearable.DataApi.getFdForAsset(mGoogleApiClient, asset).await().getInputStream();
			mGoogleApiClient.disconnect();

			if(assetStream != null)
			{
				returnBitmap = BitmapFactory.decodeStream(assetStream);
			}
		}

		return returnBitmap;
	}
}

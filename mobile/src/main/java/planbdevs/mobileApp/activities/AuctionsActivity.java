package planbdevs.mobileApp.activities;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableStatusCodes;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import planbdevs.mobileApp.R;
import planbdevs.mobileApp.classes.AuctionItem;
import planbdevs.mobileApp.bases.EbayApplication;
import planbdevs.mobileApp.utilities.ImageUtilities;

public class AuctionsActivity extends ActionBarActivity implements
                                                        DataApi.DataListener,
                                                        MessageApi.MessageListener,
                                                        NodeApi.NodeListener,
                                                        GoogleApiClient.ConnectionCallbacks,
                                                        GoogleApiClient.OnConnectionFailedListener
{
	public static final String TAG = AuctionsActivity.class.getSimpleName();
	private static final String START_ACTIVITY_PATH = "/start-bid-activity";

	ListView lvAuctionItems = null;
	GoogleApiClient mGoogleApiClient;

	List<AuctionItem> mAuctions = null;
	EbayApplication mApp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auctions);
	    lvAuctionItems = (ListView) findViewById(R.id.lvAuctions);

	    mApp = (EbayApplication) getApplication();
	    mAuctions = mApp.mAuctionItems;

	    mGoogleApiClient = new GoogleApiClient.Builder(this)
			    .addApi(Wearable.API)
			    .build();
	    mGoogleApiClient.connect();

	    loadAuctions();

	    lvAuctionItems.setAdapter(new AuctionAdapter(this, 1));

	    //startBidding();
	    //sendNotification(mAuctions.get(0));
		//fireMessage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.auctions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	private void loadAuctions()
	{
		mAuctions.add(new AuctionItem(123,R.drawable.auction_image_bmw, getString(R.string.auction_item_bmw_name), getString(R.string.auction_item_bmw_description), 10));
		mAuctions.add(new AuctionItem(456,R.drawable.auction_image_watch, getString(R.string.auction_item_watch_name), getString(R.string.auction_item_watch_description), 32));
		mAuctions.add(new AuctionItem(789,R.drawable.auction_image_tv, getString(R.string.auction_item_tv_name), getString(R.string.auction_item_tv_description), 2));


	}

	private void sendNotification(AuctionItem item)
	{

		if (item != null)
		{
			Intent iBid = new Intent(this, BidActivity.class);
			iBid.putExtra("AuctionItem", (android.os.Parcelable) item);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, iBid, 0);

			//PendingIntent pBidActivity = PendingIntent.getActivity(this, 1, iBid,PendingIntent.FLAG_UPDATE_CURRENT);

			NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this)
					.setSmallIcon(R.drawable.ebay)
					.setContentTitle("Ebay")
					.setContentText("You've been outbid!")
					.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ebay))
					.addAction(item.getImageId(), "Increase your maximum bid.", pendingIntent)
					.setContentIntent(pendingIntent)
					;

			NotificationManagerCompat nManager = NotificationManagerCompat.from(this);
			nManager.notify(item.getAuctionId(), nBuilder.build());
		}
	}

	private void startBidding()
	{
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run()
			{
				checkBids();
			}
		},1, 60, TimeUnit.SECONDS);
	}

	private void checkBids()
	{
		Calendar cal = Calendar.getInstance();
		long currentTime = cal.getTimeInMillis();
		long minutes = 0;

		for(AuctionItem item : mAuctions)
		{
			minutes = (currentTime - item.getLastBidDate()) /(1000 * 60);

			//Send an outbid notification for the first auction every minute
			if (minutes >= 1)
			{
				sendNotification(item);
				return;
			}

		}
	}


	private void fireMessageWithImage(final int index) {
		// Send the RPC
		PendingResult<NodeApi.GetConnectedNodesResult> nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient);

		nodes.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
			@Override
			public void onResult(NodeApi.GetConnectedNodesResult result) {
				for (int i = 0; i < result.getNodes().size(); i++) {
					Node node = result.getNodes().get(i);
					String nName = node.getDisplayName();
					String nId = node.getId();
					Log.d(TAG, "Node name and ID: " + nName + " | " + nId);

					Wearable.MessageApi.addListener(mGoogleApiClient, new MessageApi.MessageListener() {
						@Override
						public void onMessageReceived(MessageEvent messageEvent) {
							Log.d(TAG, "Message received: " + messageEvent);
						}
					});

					AuctionItem auction = mAuctions.get(index);

					Bitmap b = BitmapFactory.decodeResource(getResources(), auction.getImageId());
					Asset a = ImageUtilities.createAssetFromBitmap(b);

					PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(getString(R.string.bid_path));
					DataMap map = putDataMapRequest.getDataMap();
					map.putInt(getString(R.string.key_auctionId), auction.getAuctionId());
					map.putFloat(getString(R.string.key_highestbid), auction.getHighestBid());
					map.putAsset("image", a);
					PutDataRequest request = putDataMapRequest.asPutDataRequest();
					PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient, request);

				}
			}
		});
	}

	private void fireMessage(final int index) {
		// Send the RPC
		PendingResult<NodeApi.GetConnectedNodesResult> nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient);

		nodes.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
			@Override
			public void onResult(NodeApi.GetConnectedNodesResult result) {
				for (int i = 0; i < result.getNodes().size(); i++) {
					Node node = result.getNodes().get(i);
					String nName = node.getDisplayName();
					String nId = node.getId();
					Log.d(TAG, "Node name and ID: " + nName + " | " + nId);

					Wearable.MessageApi.addListener(mGoogleApiClient, new MessageApi.MessageListener() {
						@Override
						public void onMessageReceived(MessageEvent messageEvent) {
							Log.d(TAG, "Message received: " + messageEvent);
						}
					});

					AuctionItem auction = mAuctions.get(index);

					DataMap data = new DataMap();
					data.putInt("id", auction.getAuctionId());
					data.putFloat("bid", auction.getHighestBid());

					PendingResult<MessageApi.SendMessageResult> messageResult = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(),START_ACTIVITY_PATH,data.toByteArray());

					messageResult.setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
						@Override
						public void onResult(MessageApi.SendMessageResult sendMessageResult) {
							Status status = sendMessageResult.getStatus();
							Log.d(TAG, "Status: " + status.toString());
							if (status.getStatusCode() != WearableStatusCodes.SUCCESS) {

							}
						}
					});

				}
			}
		});
	}

	@Override
	public void onConnected(Bundle bundle)
	{
		Log.d(TAG, "Google API Client was connected");

		//Add the Wearable API listeners
		Wearable.DataApi.addListener(mGoogleApiClient, this);
		Wearable.MessageApi.addListener(mGoogleApiClient, this);
		Wearable.NodeApi.addListener(mGoogleApiClient, this);

	}

	@Override
	public void onConnectionSuspended(int i)
	{

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult)
	{
		if (connectionResult.hasResolution())
		{
			try
			{
				connectionResult.startResolutionForResult(this, 1);
			}
			catch(IntentSender.SendIntentException ex)
			{
				//Try again
				mGoogleApiClient.connect();
			}
		}
		else
		{
			Log.e(TAG, "Connection to Google Api client has failed");

			//Remove the listeners if the connection failed
			Wearable.DataApi.removeListener(mGoogleApiClient, this);
			Wearable.MessageApi.removeListener(mGoogleApiClient, this);
			Wearable.NodeApi.removeListener(mGoogleApiClient, this);
		}
	}

	@Override
	public void onDataChanged(DataEventBuffer dataEvents)
	{
		Log.d(TAG, "onDataChanged, " + dataEvents);
		final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
		dataEvents.close();

	}

	@Override
	public void onMessageReceived(MessageEvent messageEvent)
	{
		Log.d(TAG, "onMessageReceived() - A message from watch was received: " + messageEvent.getRequestId() + " " + messageEvent.getPath());

	}

	@Override
	public void onPeerConnected(Node node)
	{

	}

	@Override
	public void onPeerDisconnected(Node node)
	{

	}

	public class AuctionAdapter extends ArrayAdapter<AuctionItem>
	{
		public AuctionAdapter(Context context, int resource)
		{
			super(context, resource);
		}

		@Override
		public int getCount()
		{
			return mAuctions.size();
		}

		@Override
		public AuctionItem getItem(int position)
		{
			return mAuctions.get(position);
		}

		@Override
		public int getPosition(AuctionItem item)
		{
			/*int index = 0;

			for(AuctionItem i : mAuctions)
			{
				if (!i.equals(item))
				{
					index++;
				}
				else
				{
					return index;
				}
			}*/

			return 0;
		}

		@Override
		public long getItemId(int position)
		{
			return super.getItemId(position);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				convertView = getLayoutInflater().inflate(R.layout.li_auction, null);
			}
			AuctionItem item = mAuctions.get(position);
			if (item != null)
			{
				ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivAuctionItemPhoto);
				TextView tvAuctionItemName = (TextView) convertView.findViewById(R.id.tvAuctionItemName);
				TextView tvAuctionItemHighestBid = (TextView) convertView.findViewById(R.id.tvAuctionItemHighestBid);
				TextView tvAuctionItemLastBidDate = (TextView) convertView.findViewById(R.id.tvAuctionItemLastBidDate);

				ivPhoto.setImageDrawable(getResources().getDrawable(item.getImageId()));
				tvAuctionItemName.setText(item.getName());
				tvAuctionItemHighestBid.setText(String.format("$%.2f", item.getHighestBid()));
				tvAuctionItemLastBidDate.setText(new SimpleDateFormat("MM/dd/yyyy hh:mm aa").format(item.getLastBidDate()));
			}

			convertView.setOnClickListener(
					new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					//sendNotification(mAuctions.get(position));
					//fireMessage(position);
					fireMessageWithImage(position);


				}
			});
			return convertView;
		}
	}

}

package planbdevs.wearableApp.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.graphics.Path;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import planbdevs.wearableApp.R;
import planbdevs.wearableApp.classes.AuctionItemWear;


public class BidActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
	private final static String TAG = "WearApp.BidActivity";
	private final static String RESPONSE_PATH = "/bid_received";

	private TextView tvBidHeader;
	private Button bnBidSubmit;
	private NumberPicker npMaxBid;
	private int mId = 0;
	private float mBid = 0;

	GoogleApiClient mGoogleApiClient = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_update_bid);
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addApi(Wearable.API)
				.build();
		mGoogleApiClient.connect();

		Bundle bundle = getIntent().getBundleExtra("extras");

		if (bundle != null)
		{
			mId = bundle.getInt("id");
			mBid = bundle.getFloat("bid");
		}

		final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener()
		{
			@Override
			public void onLayoutInflated(WatchViewStub stub)
			{
				tvBidHeader = (TextView) stub.findViewById(R.id.tvBidHeader);
				bnBidSubmit = (Button) stub.findViewById(R.id.bnBidSubmit);
				npMaxBid = (NumberPicker) stub.findViewById(R.id.npMaxBid);

				npMaxBid.setMinValue((int)mBid);
				npMaxBid.setMaxValue(9999);
				npMaxBid.setWrapSelectorWheel(false);

				bnBidSubmit.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v)
					{
						sendUpdatedBid(mId, npMaxBid.getValue());
					}
				});
			}
		});
	}

	private void sendUpdatedBid(final int id, final int newBidAmount)
	{
		if (mGoogleApiClient.isConnected())
		{


			//Get Connected Nodes
			PendingResult<NodeApi.GetConnectedNodesResult> nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient);
			nodes.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
				@Override
				public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult)
				{
					Node node = getConnectedNodesResult.getNodes().get(0);

					DataMap data = new DataMap();
					data.putInt("id", id);
					data.putFloat("bid", (float)newBidAmount);

					//Send Message
					PendingResult<MessageApi.SendMessageResult> request = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), RESPONSE_PATH, data.toByteArray());
				}
			});
		}

	}

	@Override
	public void onConnected(Bundle bundle)
	{

	}

	@Override
	public void onConnectionSuspended(int i)
	{

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult)
	{

	}
}

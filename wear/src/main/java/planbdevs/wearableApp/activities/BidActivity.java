package planbdevs.wearableApp.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.google.android.gms.wearable.WearableStatusCodes;

import java.text.DecimalFormat;
import java.util.Locale;

import planbdevs.wearableApp.R;
import planbdevs.wearableApp.classes.AuctionItemWear;


public class BidActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
	private final static String TAG = "WearApp.BidActivity";
	private final static String RESPONSE_PATH = "/bid_received";

	private LinearLayout llBidContainer;

	private TextView tvBidHeader;
	private TextView tvBidCurrentValue;

	private Button bnBidSubmit;
	private NumberPicker npMaxBid;
	private int mId = 0;
	private float mBid = 0;
	private byte[] mImage = null;

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
			mImage = bundle.getByteArray("image");
		}

		final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener()
		{
			@Override
			public void onLayoutInflated(WatchViewStub stub)
			{
				llBidContainer = (LinearLayout) stub.findViewById(R.id.llBidContainer);
				tvBidHeader = (TextView) stub.findViewById(R.id.tvBidHeader);
				tvBidCurrentValue = (TextView) stub.findViewById(R.id.tvBidCurrentValue);
				bnBidSubmit = (Button) stub.findViewById(R.id.bnBidSubmit);
				npMaxBid = (NumberPicker) stub.findViewById(R.id.npMaxBid);

				//Set the values
				tvBidCurrentValue.setText(String.format(Locale.US, "$%.2f", mBid));
				npMaxBid.setMinValue((int) mBid);
				npMaxBid.setMaxValue(9999);
				npMaxBid.setWrapSelectorWheel(false);

				if (mImage != null && mImage.length > 0)
				{

					llBidContainer.setBackground(new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(mImage, 0, mImage.length)));

				}
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
					request.setResultCallback(new ResultCallback<MessageApi.SendMessageResult>() {
						@Override
						public void onResult(MessageApi.SendMessageResult sendMessageResult)
						{
							if (sendMessageResult.getStatus().getStatusCode() == WearableStatusCodes.SUCCESS)
							{
								finish();
							}
						}

					});
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

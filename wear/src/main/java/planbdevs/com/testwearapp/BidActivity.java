package planbdevs.com.testwearapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.widget.Button;
import android.widget.TextView;

public class BidActivity extends Activity
{

	private TextView tvBidHeader;
	private Button bnBidSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_update_bid);
		final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
		stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener()
		{
			@Override
			public void onLayoutInflated(WatchViewStub stub)
			{
				tvBidHeader = (TextView) stub.findViewById(R.id.tvBidHeader);
				bnBidSubmit = (Button) stub.findViewById(R.id.bnBidSubmit);
			}
		});
	}
}

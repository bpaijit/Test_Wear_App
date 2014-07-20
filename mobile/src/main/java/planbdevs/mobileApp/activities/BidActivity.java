package planbdevs.mobileApp.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import planbdevs.mobileApp.R;
import planbdevs.mobileApp.classes.AuctionItem;
import planbdevs.mobileApp.bases.EbayApplication;

public class BidActivity extends ActionBarActivity{

	ImageView ivBidItemPhoto = null;

	TextView tvAuctionItemHighestBid = null;
	TextView tvAuctionItemLastBidDate = null;

	//Used to receive an updated bid from the wearable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid);

	    ivBidItemPhoto = (ImageView) findViewById(R.id.ivBidItemPhoto);

	    tvAuctionItemHighestBid = (TextView) findViewById(R.id.tvAuctionItemHighestBid);
	    tvAuctionItemLastBidDate = (TextView) findViewById(R.id.tvAuctionItemLastBidDate);

	    List<AuctionItem> mAuctions = null;
	    EbayApplication mApp = null;

	    mApp = (EbayApplication) getApplication();
	    mAuctions = mApp.mAuctionItems;

	    Bundle b = getIntent().getBundleExtra("extras");

	    if (b != null)
	    {
		    int id = b.getInt("id");
		    float bidAmount = b.getFloat("bid");

		    for (AuctionItem a : mAuctions)
		    {
			    if (a.getAuctionId() == id)
			    {
				    a.setHighestBid(bidAmount);
				    a.setLastBidDate(Calendar.getInstance(Locale.US).getTimeInMillis());

				    ivBidItemPhoto.setImageDrawable(getResources().getDrawable(a.getImageId()));
				    tvAuctionItemHighestBid.setText(String.format(Locale.US, "$%.2f", bidAmount));
				    tvAuctionItemLastBidDate.setText(new SimpleDateFormat("MM/dd/yyyy hh:mm aa").format(a.getLastBidDate()));
			    }
		    }
	    }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.bid, menu);
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

}

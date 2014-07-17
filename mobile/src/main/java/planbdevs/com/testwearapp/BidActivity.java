package planbdevs.com.testwearapp;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;
import java.util.List;

import planbdevs.com.classes.AuctionItem;
import planbdevs.com.palnbdevs.com.bases.EbayApplication;
import planbdevs.com.testwearapp.R;

public class BidActivity extends ActionBarActivity {

	//Used to receive an updated bid from the wearable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid);

	    List<AuctionItem> mAuctions = null;
	    EbayApplication mApp = null;

	    mApp = (EbayApplication) getApplication();
	    mAuctions = mApp.mAuctionItems;

	    Bundle b = getIntent().getExtras();

	    if (b != null)
	    {
		    AuctionItem item = (AuctionItem)b.get("AuctionItem");

		    if(item != null)
		    {
			    item.setHighestBid(item.getHighestBid() + 1);
			    item.setLastBidDate(Calendar.getInstance().getTimeInMillis());
			    int index = mAuctions.indexOf(item);
			    if(index >= 0)
			    {
				    mAuctions.remove(index);
			    }
			    mAuctions.add(item);
		    }
	    }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bid, menu);
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

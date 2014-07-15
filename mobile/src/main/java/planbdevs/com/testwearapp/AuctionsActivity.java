package planbdevs.com.testwearapp;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import planbdevs.com.classes.AuctionItem;
import planbdevs.com.testwearapp.R;

public class AuctionsActivity extends ActionBarActivity {
	List<AuctionItem> mAuctions = new ArrayList<AuctionItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auctions);
	    loadAuctions();

	    sendNotification(mAuctions.get(0));
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
		mAuctions.add(new AuctionItem(123,R.drawable.auction_image_bmw, getString(R.string.auction_item_bmw_name), getString(R.string.auction_item_bmw_description)));
		mAuctions.add(new AuctionItem(456,R.drawable.auction_image_watch, getString(R.string.auction_item_watch_name), getString(R.string.auction_item_watch_description)));
		mAuctions.add(new AuctionItem(789,R.drawable.auction_image_watch, getString(R.string.auction_item_tv_name), getString(R.string.auction_item_tv_description)));
	}

	private void sendNotification(AuctionItem item)
	{

		if (item != null)
		{
			Intent iBid = new Intent(this, BidActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, iBid, 0);

			NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this)
					.setSmallIcon(R.drawable.ebay)
					.setContentTitle("Ebay")
					.setContentText("You've been outbid!")
					//.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ebay))
					.addAction(item.getImageId(), "Increase your maximum bid.", pendingIntent)
					.setContentIntent(pendingIntent)
					;

			NotificationManagerCompat nManager = NotificationManagerCompat.from(this);
			nManager.notify(item.getAuctionId(), nBuilder.build());
		}
	}
}

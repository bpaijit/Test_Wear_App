package planbdevs.com.testwearapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import planbdevs.com.classes.AuctionItem;
import planbdevs.com.testwearapp.R;

public class AuctionsActivity extends ActionBarActivity {
	List<AuctionItem> mAuctions = new ArrayList<AuctionItem>();
	ListView lvAuctionItems = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auctions);
	    lvAuctionItems = (ListView) findViewById(R.id.lvAuctions);

	    loadAuctions();

	    lvAuctionItems.setAdapter(new AuctionAdapter(this, 1));

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
		mAuctions.add(new AuctionItem(789,R.drawable.auction_image_tv, getString(R.string.auction_item_tv_name), getString(R.string.auction_item_tv_description)));


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
					.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ebay))
					.addAction(item.getImageId(), "Increase your maximum bid.", pendingIntent)
					.setContentIntent(pendingIntent)
					;

			NotificationManagerCompat nManager = NotificationManagerCompat.from(this);
			nManager.notify(item.getAuctionId(), nBuilder.build());
		}
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
			int index = 0;

			for(AuctionItem i : mAuctions)
			{
				if (i.equals(mAuctions.get(index)))
				{
					return index;
				}

				index++;
			}

			return 0;
		}

		@Override
		public long getItemId(int position)
		{
			return super.getItemId(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
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
				tvAuctionItemHighestBid.setText(String.valueOf(item.getHighestBid()));
				tvAuctionItemLastBidDate.setText(String.valueOf(item.getLastBidDate()));
			}


			return convertView;
		}
	}
}

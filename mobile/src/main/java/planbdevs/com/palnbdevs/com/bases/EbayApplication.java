package planbdevs.com.palnbdevs.com.bases;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import planbdevs.com.classes.AuctionItem;

/**
 * Created by Bryan on 7/16/2014.
 */
public class EbayApplication extends Application
{
	public List<AuctionItem> mAuctionItems = new ArrayList<AuctionItem>();
}

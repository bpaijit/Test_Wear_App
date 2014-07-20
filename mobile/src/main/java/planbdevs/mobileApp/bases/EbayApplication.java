package planbdevs.mobileApp.bases;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import planbdevs.mobileApp.classes.AuctionItem;

/**
 * Created by Bryan on 7/16/2014.
 */
public class EbayApplication extends Application
{
	public List<AuctionItem> mAuctionItems = new ArrayList<AuctionItem>();
}

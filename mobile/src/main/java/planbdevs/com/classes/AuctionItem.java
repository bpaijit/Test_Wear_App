package planbdevs.com.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

/**
 * Created by Bryan on 7/15/2014.
 */
public class AuctionItem implements Parcelable
{
	private int auctionId;
	private int imageId;
	private String name;
	private String description;
	private long lastBidDate;
	private float highestBid;

	public int getAuctionId()
	{
		return auctionId;
	}

	public void setAuctionId(int auctionId)
	{
		this.auctionId = auctionId;
	}

	public int getImageId()
	{
		return imageId;
	}

	public void setImageId(int imageId)
	{
		this.imageId = imageId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public long getLastBidDate()
	{
		return lastBidDate;
	}

	public void setLastBidDate(long lastBidDate)
	{
		this.lastBidDate = lastBidDate;
	}

	public float getHighestBid()
	{
		return highestBid;
	}

	public void setHighestBid(float highestBid)
	{
		this.highestBid = highestBid;
	}

	//Constructors
	public AuctionItem()
	{
	}

	public AuctionItem(int auctionId, int imageId, String name, String description)
	{
		this.auctionId = auctionId;
		this.imageId = imageId;
		this.name = name;
		this.description = description;
		this.lastBidDate = Calendar.getInstance().getTimeInMillis();
		this.highestBid = 1;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	public AuctionItem(Parcel in)
	{
		String[] data = new String[6];
		in.readStringArray(data);
		this.auctionId = Integer.parseInt(data[0]);
		this.imageId = Integer.parseInt(data[1]);
		this.name = data[2];
		this.description = data[3];
		this.highestBid = Integer.parseInt(data[4]);
		this.lastBidDate = Integer.parseInt(data[5]);
	}
	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeStringArray(new String[] {
											String.valueOf(this.auctionId),
											String.valueOf(this.imageId),
											this.name,
											this.description,
											String.valueOf(this.highestBid),
											String.valueOf(this.lastBidDate)});


	}

}

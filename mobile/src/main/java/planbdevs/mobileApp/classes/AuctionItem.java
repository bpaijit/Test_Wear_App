package planbdevs.mobileApp.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Bryan on 7/15/2014.
 */
public class AuctionItem implements Parcelable, Serializable
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

	public AuctionItem(int auctionId, int imageId, String name, String description, float highestBid)
	{
		this.auctionId = auctionId;
		this.imageId = imageId;
		this.name = name;
		this.description = description;
		this.lastBidDate = Calendar.getInstance().getTimeInMillis();
		this.highestBid = highestBid;
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
		this.highestBid = Float.parseFloat(data[4]);
		this.lastBidDate = Long.parseLong(data[5]);
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

	public static final Creator CREATOR = new Creator<AuctionItem>()
	{
		public AuctionItem createFromParcel(Parcel in)
		{
			return new AuctionItem(in);
		}
		public AuctionItem[] newArray(int size)
		{
			return new AuctionItem[size];
		}
	};
}

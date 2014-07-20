package planbdevs.mobileApp.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.wearable.Asset;

import java.io.ByteArrayOutputStream;

/**
 * Created by Bryan on 7/20/2014.
 */
public class ImageUtilities
{
	public static Asset createAssetFromBitmap(Bitmap bitmap)
	{
		final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
		return Asset.createFromBytes(byteStream.toByteArray());
	}
}

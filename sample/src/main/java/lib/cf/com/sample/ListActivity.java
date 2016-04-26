package lib.cf.com.sample;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cf.lib.extensible.ExpandRecyclerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListActivity extends Activity
{
	protected final static String TAG = "ListActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		List<Integer> mDatas = new ArrayList<>(Arrays.asList(R.drawable.a,
				R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e,
				R.drawable.f, R.drawable.g, R.drawable.h, R.drawable.i,
				R.drawable.j, R.drawable.k, R.drawable.l, R.drawable.m,
				R.drawable.n, R.drawable.o, R.drawable.p, R.drawable.q,
				R.drawable.r, R.drawable.s, R.drawable.t, R.drawable.u,
				R.drawable.v, R.drawable.w));

		RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.activity_list_view);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(linearLayoutManager);
		mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
		MyAdapter mAdapter = new MyAdapter(this, mDatas);
		mRecyclerView.setAdapter(mAdapter);
	}

	private class MyAdapter extends ExpandRecyclerAdapter<MyHolder> {
		private LayoutInflater inflater;
		private List<Integer> array;
		public MyAdapter(Context context, List<Integer> array) {
			inflater = LayoutInflater.from(context);
			this.array = array;
			setExpandAnimable(true);
			setFlagExpandCloseable(true);
		}

		@Override
		public MyHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
			View view = inflater.inflate(R.layout.item_activity_list, viewGroup, false);
			MyHolder holder = new MyHolder(view);
			holder.titleView = (TextView) view.findViewById(R.id.item_activity_list_title);
			holder.imgView = (ImageView) view.findViewById(R.id.item_activity_list_img);
			holder.expandView = holder.imgView;
			return holder;
		}

		@Override
		public void onBindRealViewHolder(MyHolder myHolder, int i) {
			int resId = array.get(i);
			String str = ListActivity.TAG + "position " + i;
			myHolder.titleView.setText(str);
			myHolder.imgView.setVisibility(i == getExpandIndex() ? View.VISIBLE : View.GONE);

			int height = 0;
			Drawable drawable = getDrawable(resId);
			if(drawable != null) {
				height = drawable.getIntrinsicHeight();
			}
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inJustDecodeBounds = true;
			Bitmap bmp = BitmapFactory.decodeResource(getResources(), resId, opt);
			if(bmp != null) {
				height = bmp.getHeight();
			}
			if(i == 3) {
				myHolder.expandView.setTag(0);
				myHolder.imgView.setImageBitmap(null);
			} else {
				myHolder.expandView.setTag(height);
				myHolder.imgView.setImageResource(resId);
			}
		}

		@Override
		public int getItemCount() {
			return array == null ? 0 : array.size();
		}
	}

	private static class MyHolder extends ExpandRecyclerAdapter.ExpandHolder {

		public TextView titleView;
		public ImageView imgView;
		public MyHolder(View itemView) {
			super(itemView);
		}
	}
}

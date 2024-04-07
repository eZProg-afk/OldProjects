package spiral.bit.dev.wallsforteens.Fragments;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import spiral.bit.dev.wallsforteens.R;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

public class CelebritiesFragment extends BaseFragment {

    GridView grid;
    ImageView currWall;
    Drawable myDrawable;
    WallpaperManager manager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_celebrities, container, false);
        grid = view.findViewById(R.id.grid);
        currWall = view.findViewById(R.id.my_wall_img);
        grid.setAdapter(new SampleGridViewAdapter(view.getContext()));
        if (view.getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST);
        }
        updateWallpapers();
        return view;
    }

    void updateWallpapers() {
        manager = WallpaperManager.getInstance(getContext());
        myDrawable = manager.getDrawable();
        currWall.setImageDrawable(myDrawable);
    }

    class SampleGridViewAdapter extends BaseAdapter {

        private final Context context;
        private final List<String> urls = new ArrayList<>();

        String one = "https://www.pinterest.ru/pin/599752875360180669/";
        String two = "https://www.pinterest.ru/pin/599682506625335051/";
        String three = "https://www.pinterest.ru/pin/76490893659361027/";
        String four = "https://www.pinterest.ru/pin/76490893659361027/";
        String five = "https://www.pinterest.ru/pin/76490893659361027/";
        String six = "https://www.pinterest.ru/pin/76490893659361027/";
        String seven = "https://www.pinterest.ru/pin/76490893659361027/";

        public SampleGridViewAdapter(Context context) {
            this.context = context;

            urls.add(one);
            urls.add(two);
            urls.add(three);
            urls.add(five);

            // Ensure we get a different ordering of images on each run.
            Collections.addAll(urls);
            Collections.shuffle(urls);

            // Triple up the list.
            ArrayList<String> copy = new ArrayList<String>(urls);
            urls.addAll(copy);
            urls.addAll(copy);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView view = (ImageView) convertView;
            if (view == null) {
                view = new ImageView(context);
                view.setScaleType(CENTER_CROP);
            }

            // Get the image URL for the current position.
            String url = getItem(position);

            // Trigger the download of the URL asynchronously into the image view.
            Picasso.with(context) //
                    .load(url) //
                    .placeholder(R.drawable.ic_diamond) // not found
                    .error(R.drawable.star) //error
                    .fit() //
                    .into(view);
            return view;
        }

        @Override
        public int getCount() {
            return urls.size();
        }

        @Override
        public String getItem(int position) {
            return urls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }
}


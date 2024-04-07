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
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import spiral.bit.dev.wallsforteens.R;
import static android.widget.ImageView.ScaleType.CENTER_CROP;
import static spiral.bit.dev.wallsforteens.utils.ReferencesKt.*;

public class CarsFragment extends BaseFragment {

    GridView grid;
    ImageView currWall;
    Drawable myDrawable;
    WallpaperManager manager;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cars, container, false);
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

        public SampleGridViewAdapter(Context context) {
            this.context = context;

            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_2);
            urls.add(FOR_BOY_3);
            urls.add(FOR_BOY_4);
            urls.add(FOR_BOY_5);
            urls.add(FOR_BOY_6);
            urls.add(FOR_BOY_7);
            urls.add(FOR_BOY_8);
            urls.add(FOR_BOY_9);
            urls.add(FOR_BOY_10);
            urls.add(FOR_BOY_11);
            urls.add(FOR_BOY_12);
            urls.add(FOR_BOY_13);
            urls.add(FOR_BOY_14);
            urls.add(FOR_BOY_15);
            urls.add(FOR_BOY_16);
            urls.add(FOR_BOY_17);
            urls.add(FOR_BOY_18);
            urls.add(FOR_BOY_19);
            urls.add(FOR_BOY_20);
            urls.add(FOR_BOY_21);
            urls.add(FOR_BOY_22);
            urls.add(FOR_BOY_23);
            urls.add(FOR_BOY_24);
            urls.add(FOR_BOY_25);
            urls.add(FOR_BOY_26);
            urls.add(FOR_BOY_27);
            urls.add(FOR_BOY_28);
            urls.add(FOR_BOY_29);
            urls.add(FOR_BOY_30);
            urls.add(FOR_BOY_31);
            urls.add(FOR_BOY_32);
            urls.add(FOR_BOY_33);
            urls.add(FOR_BOY_34);
            urls.add(FOR_BOY_35);
            urls.add(FOR_BOY_36);
            urls.add(FOR_BOY_37);
            urls.add(FOR_BOY_38);
            urls.add(FOR_BOY_39);
            urls.add(FOR_BOY_40);
            urls.add(FOR_BOY_41);
            urls.add(FOR_BOY_42);
            urls.add(FOR_BOY_43);
            urls.add(FOR_BOY_44);
            urls.add(FOR_BOY_45);
            urls.add(FOR_BOY_46);
            urls.add(FOR_BOY_47);
            urls.add(FOR_BOY_48);
            urls.add(FOR_BOY_49);
            urls.add(FOR_BOY_50);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);
            urls.add(FOR_BOY_1);

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
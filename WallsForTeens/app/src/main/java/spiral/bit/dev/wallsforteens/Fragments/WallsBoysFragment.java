package spiral.bit.dev.wallsforteens.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
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

import java.io.IOException;

import spiral.bit.dev.wallsforteens.R;

public class WallsBoysFragment extends BaseFragment {

    GridView grid;
    ImageView currWall;
    Drawable myDrawable;
    WallpaperManager manager;
    Integer[] imgArray = {

    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_walls_boys, container, false);
        grid = view.findViewById(R.id.grid);
        currWall = view.findViewById(R.id.my_wall_img);
        grid.setAdapter(new ImageAdapter(view.getContext()));
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

    class ImageAdapter extends BaseAdapter {

        Context context;

        public ImageAdapter(Context applicationContext) {
            context = applicationContext;
        }

        @Override
        public int getCount() {
            return imgArray.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            ImageView gridImgView;

            if (view == null) {
                gridImgView = new ImageView(context);
                gridImgView.setLayoutParams(new GridView.LayoutParams(512, 512));
                gridImgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                gridImgView = (ImageView) view;
            }

            gridImgView.setImageResource(imgArray[i]);

            gridImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        manager.setResource(imgArray[i]);
                    } catch (IOException e) {
                        Toast.makeText(getContext(), getString(R.string.error_happens_file_not_found),  Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    updateWallpapers();
                    Toast.makeText(getContext(), R.string.walls_updated, Toast.LENGTH_SHORT).show();
                }
            });

            gridImgView.setOnLongClickListener(new View.OnLongClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.Q)
                @Override
                public boolean onLongClick(View view) {
                    showFullImageWindow(imgArray[i]);
                    return false;
                }
            });

            currWall.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.Q)
                @Override
                public void onClick(View view) {
                    showFullCurrImageWindow(currWall.getDrawable());
                }
            });

            return gridImgView;
        }
    }
}
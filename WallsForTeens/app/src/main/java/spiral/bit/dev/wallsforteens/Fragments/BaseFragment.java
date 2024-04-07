package spiral.bit.dev.wallsforteens.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import spiral.bit.dev.wallsforteens.R;

public class BaseFragment extends Fragment {

    private static final String READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final int PERMISSION_REQUEST = 300;

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    void showFullImageWindow(final int viewID) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View fullImageWindow = inflater.inflate(R.layout.full_wall_item, null);
        ImageView wall_full = fullImageWindow.findViewById(R.id.full_wall_img);
        wall_full.setImageResource(viewID);
        dialog.setTitle(R.string.view_wall);
        dialog.setView(fullImageWindow);
        dialog.setNegativeButton(getString(R.string.close_title), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.setPositiveButton(getString(R.string.share_title), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Bitmap b = BitmapFactory.decodeResource(getResources(), viewID);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/jpeg");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(),
                        b, getString(R.string.walls_on_phone), null);
                Uri imageUri =  Uri.parse(path);
                share.putExtra(Intent.EXTRA_STREAM, imageUri);
                startActivity(Intent.createChooser(share, "Выберите кому нужно отправить файл."));
            }
        });
        dialog.show();
    }

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.Q)
    void showFullCurrImageWindow(final Drawable drawable) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View fullImageWindow = inflater.inflate(R.layout.full_wall_item, null);
        ImageView wall_full = fullImageWindow.findViewById(R.id.full_wall_img);
        wall_full.setImageDrawable(drawable);
        dialog.setTitle(R.string.view_wall);
        dialog.setView(fullImageWindow);
        dialog.setNegativeButton(R.string.close_title, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }
}
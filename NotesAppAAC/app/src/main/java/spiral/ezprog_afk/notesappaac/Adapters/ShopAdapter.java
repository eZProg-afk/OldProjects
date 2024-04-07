package spiral.ezprog_afk.notesappaac.Adapters;

//CREATED BY SPIRAL(EZ_PROG)//

//Imports:

import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import spiral.ezprog_afk.notesappaac.Models.ShopItem;
import spiral.ezprog_afk.notesappaac.Models.ToDoItem;
import spiral.ezprog_afk.notesappaac.R;

public class ShopAdapter extends ListAdapter<ShopItem, ShopAdapter.ShopViewHolder> {

    //Vars:

    private OnItemClickListener listener;

    public ShopAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<ShopItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<ShopItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull ShopItem oldItem, @NonNull ShopItem newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ShopItem oldItem, @NonNull ShopItem newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    };


    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item, parent, false);
        return new ShopViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        ShopItem currentShopItem = getItem(position);
        holder.shopTextView.setText(currentShopItem.getName());
    }

    public ShopItem getShopItemAt(int position) {
        return getItem(position);
    }

    class ShopViewHolder extends RecyclerView.ViewHolder {

        private TextView shopTextView;
        private Button buttonBought;
        private boolean btnToggle = false;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);
            shopTextView = itemView.findViewById(R.id.shop_item_TV);
            buttonBought = itemView.findViewById(R.id.btn_buy);

            buttonBought.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!btnToggle) {
                        btnToggle = true;
                        buttonBought.setBackgroundColor(Color.RED);
                        buttonBought.setText("Не куплено");
                        shopTextView.setPaintFlags(0);
                    } else {
                        btnToggle = false;
                        buttonBought.setBackgroundColor(Color.GREEN);
                        buttonBought.setText("Куплено");
                        shopTextView.setPaintFlags(shopTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ShopItem shopItem);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}


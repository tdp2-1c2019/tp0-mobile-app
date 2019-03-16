package tdp2.tp0app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Book> mData;
    private LayoutInflater mInflater;

    public BooksAdapter(Context context, ArrayList<Book> data) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public BooksAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.book_item, parent, false);
        return new BooksAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return this.mData.size();
    }

    @Override
    public void onBindViewHolder(BooksAdapter.ViewHolder holder, int position) {
        final Book book = this.mData.get(position);
        holder.name.setText(book.name);
        holder.authors.setText(TextUtils.join(", ", book.authors));
        holder.categories.setText(TextUtils.join(", ", book.categories));
        holder.description.setContent(book.description);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView authors;
        TextView categories;
        SeeMoreTextView description;

        ViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.book_item_name);
            this.authors = itemView.findViewById(R.id.book_item_author);
            this.categories = itemView.findViewById(R.id.book_item_categories);
            this.description = itemView.findViewById(R.id.book_item_description);
        }
    }
}

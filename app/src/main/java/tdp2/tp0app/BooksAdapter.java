package tdp2.tp0app;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
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

    public static void setImageFromUrl(String url, ImageView imageView, int placeholderResId) {
        try {
            if (url != null && url.length() > 0) {
                Picasso
                        .get()
                        .load(url)
                        .placeholder(placeholderResId)
                        .into(imageView);
            } else {
                // Load the placeholder instead
                Picasso
                        .get()
                        .load(placeholderResId)
                        .placeholder(placeholderResId)
                        .into(imageView);
            }
        } catch (Exception e) {
            // The FileUri cannot be parsed.
            // Swallow the exception and load a placeholder
            Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onBindViewHolder(BooksAdapter.ViewHolder holder, int position) {
        final Book book = this.mData.get(position);
        setImageFromUrl(book.coverLink, holder.cover, R.drawable.book_cover_placeholder);
        holder.name.setText(book.name);
        holder.authors.setText(TextUtils.join(", ", book.authors));
        holder.categories.setText(TextUtils.join(", ", book.categories));
        holder.description.setContent(book.description);
        if (! book.downloadLink.isEmpty()){
            holder.downloadButton.setVisibility(View.VISIBLE);
            holder.downloadButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    new DownloadFileFromURL().execute(book.downloadLink);
                }
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView name;
        TextView authors;
        TextView categories;
        SeeMoreTextView description;
        ImageButton downloadButton;

        ViewHolder(View itemView) {
            super(itemView);
            this.cover = itemView.findViewById(R.id.book_cover);
            this.name = itemView.findViewById(R.id.book_item_name);
            this.authors = itemView.findViewById(R.id.book_item_author);
            this.categories = itemView.findViewById(R.id.book_item_categories);
            this.description = itemView.findViewById(R.id.book_item_description);
            this.downloadButton = itemView.findViewById(R.id.download_book_button);
        }
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                File path = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS);
                File file = new File(path, "/2011.kml");

                // Make sure the directory exists.
                path.mkdirs();
                file.createNewFile();
                FileOutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }
    }
}

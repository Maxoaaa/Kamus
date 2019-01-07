package id.web.skytacco.kamus.Adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.web.skytacco.kamus.DetailActivity;
import id.web.skytacco.kamus.Model.KamusModel;
import id.web.skytacco.kamus.R;

public class KamusAdapter extends RecyclerView.Adapter<KamusAdapter.SearchHolder> {
    private ArrayList<KamusModel> WordList = new ArrayList<>();

    public KamusAdapter() {

    }

    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup mviewGroup, int viewType) {
        View view = LayoutInflater.from(mviewGroup.getContext()).inflate(R.layout.content_main_dictionary, mviewGroup, false);
        return new SearchHolder(view);
    }

    public void replaceAll(ArrayList<KamusModel> items) {
        WordList = items;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
        holder.bind(WordList.get(position));
    }

    @Override
    public int getItemCount() {
        return WordList.size();
    }

    public static class SearchHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtWord)
        TextView txtWord;
        @BindView(R.id.txtDesc)
        TextView txtDescription;

        SearchHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(final KamusModel mKamusModel) {
            txtWord.setText(mKamusModel.getWord());
            txtDescription.setText(mKamusModel.getDescription());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), DetailActivity.class);
                    intent.putExtra(DetailActivity.ITEM_WORD, mKamusModel.getWord());
                    intent.putExtra(DetailActivity.ITEM_DESCRIPTION, mKamusModel.getDescription());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}

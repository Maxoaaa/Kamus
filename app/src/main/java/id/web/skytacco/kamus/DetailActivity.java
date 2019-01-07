package id.web.skytacco.kamus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    public static final String ITEM_WORD = "item_word";
    public static final String ITEM_DESCRIPTION = "item_description";

    @BindView(R.id.txtWord)
    TextView txtWord;
    @BindView(R.id.txtDesc)
    TextView txtDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        txtWord.setText(getIntent().getStringExtra(ITEM_WORD));
        txtDesc.setText(getIntent().getStringExtra(ITEM_DESCRIPTION));
    }
}

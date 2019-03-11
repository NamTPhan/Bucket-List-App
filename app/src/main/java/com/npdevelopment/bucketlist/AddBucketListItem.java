package com.npdevelopment.bucketlist;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

public class AddBucketListItem extends AppCompatActivity {

    private TextInputEditText mTitleInput, mDescriptionInput;
    private Button mAddNewItemBtn;
    private Snackbar mSnackBar;
    private final int DEFAULT_STATUS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bucket_list_item);

        mTitleInput = findViewById(R.id.titleInput);
        mDescriptionInput = findViewById(R.id.descriptionInput);
        mAddNewItemBtn = findViewById(R.id.addItemBtn);

        mAddNewItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mTitleInput.getText()) || TextUtils.isEmpty(mDescriptionInput.getText())) {
                    mSnackBar = Snackbar.make(v, getString(R.string.fields_required), Snackbar.LENGTH_SHORT);

                    // Change snackbar view with custom colors
                    View sbView = mSnackBar.getView();
                    sbView.setBackgroundColor(getResources().getColor(R.color.red));
                    mSnackBar.show();
                } else {
                    BucketListItem bucketListItem = new BucketListItem(mTitleInput.getText().toString(),
                            mDescriptionInput.getText().toString(), DEFAULT_STATUS);

                    Intent data = new Intent();
                    data.putExtra(BucketListActivity.NEW_ITEM_KEY, bucketListItem);

                    //Send the result back to the activity
                    setResult(Activity.RESULT_OK, data);

                    //Go back to the previous activity
                    finish();
                }
            }
        });
    }
}

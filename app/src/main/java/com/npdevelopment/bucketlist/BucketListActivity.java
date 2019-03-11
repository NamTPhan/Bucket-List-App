package com.npdevelopment.bucketlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BucketListActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener, BucketListAdapter.BucketListListener {

    private RecyclerView rvBucketList;
    private Snackbar mSnackBar;
    private BucketListItem mBucketListItem;
    private BucketListAdapter mBucketListAdapter;
    private List<BucketListItem> bucketList = new ArrayList<>();

    private BucketListRoomDatabase db;
    private Executor executor = Executors.newSingleThreadExecutor();
    private GestureDetector mGestureDetector;

    public static final String NEW_ITEM_KEY = "newBucketListItemKey";
    public static final int REQUEST_CODE_OK = 200;
    private final int DEFAULT_STATUS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bucket_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvBucketList = findViewById(R.id.bucket_list_rv);
        db = BucketListRoomDatabase.getDatabase(this);

        FloatingActionButton fab = findViewById(R.id.add_new_item_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BucketListActivity.this, AddBucketListItem.class);
                startActivityForResult(intent, REQUEST_CODE_OK);
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        rvBucketList.setLayoutManager(mLayoutManager);
        rvBucketList.setHasFixedSize(true);
        rvBucketList.addOnItemTouchListener(this);
        rvBucketList.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));

        mBucketListAdapter = new BucketListAdapter(bucketList, this);
        rvBucketList.setAdapter(mBucketListAdapter);
        getAllBucketListItems();

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                View child = rvBucketList.findChildViewUnder(e.getX(), e.getY());

                if (child != null) {
                    int adapterPosition = rvBucketList.getChildAdapterPosition(child);

                    mSnackBar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.deleted_message), Snackbar.LENGTH_LONG);
                    View sbView = mSnackBar.getView();
                    sbView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    mSnackBar.show();

                    deleteBucketListItem(bucketList.get(adapterPosition));
                }
            }
        });
    }

    // Used for dynamically refreshing the list
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Check if the result code is the right one
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_OK) {
                mBucketListItem = data.getParcelableExtra(NEW_ITEM_KEY);

                mSnackBar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.success_message), Snackbar.LENGTH_LONG);
                View sbView = mSnackBar.getView();
                sbView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                mSnackBar.show();

                insertBucketListItem(mBucketListItem);
            }
        }
    }

    /**
     * Update the bucket list
     */
    private void updateUI(List<BucketListItem> bucketListItems) {
        bucketList.clear();
        bucketList.addAll(bucketListItems);
        mBucketListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bucket_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete_item) {
            deleteAllBucketListItems(bucketList);
            Toast.makeText(this, getString(R.string.deleted_message), Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
        mGestureDetector.onTouchEvent(motionEvent);
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

    /**
     * Get all bucket list items from the database and update the ui with these items.
     */
    private void getAllBucketListItems() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                final List<BucketListItem> bucketListItems = db.bucketListItemDao().getAllBucketListItems();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateUI(bucketListItems);
                    }
                });
            }
        });
    }

    /**
     * Insert new bucket list item into the database
     * @param bucketListItem object
     */
    private void insertBucketListItem(final BucketListItem bucketListItem) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.bucketListItemDao().insert(bucketListItem);
                getAllBucketListItems();
            }
        });
    }

    /**
     * Update bucket list item
     * @param bucketListItem
     */
    private void updateBucketListItem(final BucketListItem bucketListItem) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.bucketListItemDao().update(bucketListItem);
                getAllBucketListItems();
            }
        });
    }

    /**
     * Delete one specific bucket list item from database
     * @param bucketListItem selected bucket list item
     */
    private void deleteBucketListItem(final BucketListItem bucketListItem) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.bucketListItemDao().delete(bucketListItem);
                getAllBucketListItems();
            }
        });
    }

    /**
     * Delete all bucket list items from the database
     * @param bucketListItems current bucket list
     */
    private void deleteAllBucketListItems(final List<BucketListItem> bucketListItems) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                db.bucketListItemDao().delete(bucketListItems);
                getAllBucketListItems();
            }
        });
    }

    @Override
    public void onCheckBoxClick(BucketListItem bucketListItem) {
        bucketListItem.setDone((bucketListItem.getDone() == DEFAULT_STATUS ? 1 : 0));
        updateBucketListItem(bucketListItem);

        mSnackBar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.update_message), Snackbar.LENGTH_LONG);
        View sbView = mSnackBar.getView();
        sbView.setBackgroundColor(getResources().getColor(R.color.blue_gray));
        mSnackBar.show();

        getAllBucketListItems();
    }
}

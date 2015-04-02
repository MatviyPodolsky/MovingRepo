package com.sdex.webteb.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.NewbornAdapter;
import com.sdex.webteb.database.DatabaseHelper;
import com.sdex.webteb.database.model.DbUser;
import com.sdex.webteb.model.Child;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.request.BabyProfileRequest;
import com.sdex.webteb.rest.response.BabyProfileResponse;
import com.sdex.webteb.utils.DateUtil;
import com.sdex.webteb.utils.PreferencesManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.client.Response;

/**
 * Created by Yuriy Mysochenko on 24.03.2015.
 */
public class NewbornActivity extends BaseActivity {

    public static final int BIRTH_DATE = 3;

    private NewbornAdapter mAdapter;
    @InjectView(R.id.children_list)
    ListView mList;
    @InjectView(R.id.save)
    Button mSave;
    private List<Child> mChildren;

    private BabyProfileRequest request = new BabyProfileRequest();
    private RestCallback<BabyProfileResponse> getBabyProfileCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendAnalyticsScreenName(R.string.screen_congratulations);
        enableFullscreen();
        mAdapter = new NewbornAdapter(NewbornActivity.this);
        mAdapter.setCallback(new NewbornAdapter.Callback() {
            @Override
            public void onDeleteChild(final Child child) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(NewbornActivity.this)
                        .setMessage(R.string.are_u_sure_u_want_to_delete_child)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAdapter.removeChild(child);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog.show();

            }
        });

        getBabyProfileCallback = new RestCallback<BabyProfileResponse>() {
            @Override
            public void failure(RestError restError) {
            }

            @Override
            public void success(BabyProfileResponse babyProfileResponse, Response response) {
                if(babyProfileResponse != null && babyProfileResponse.getChildren() != null){
                    request.setFamilyRelation(babyProfileResponse.getFamilyRelation());
                    request.setDateType(BIRTH_DATE);
                    Date currentDate = Calendar.getInstance().getTime();
                    String requestDate = DateUtil.formatDate(currentDate, "yyyy-MM-dd'T'HH:mm:ssZ");
                    request.setDate(requestDate);
                    mAdapter.setItems(babyProfileResponse.getChildren());
                    mList.setAdapter(mAdapter);
                }
            }
        };

        RestClient.getApiService().getBabyProfile(getBabyProfileCallback);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_newborn;
    }

    @OnClick(R.id.add)
    public void addChild() {
        mAdapter.addChild(new Child());
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.save)
    void save() {
        request.setChildren(mAdapter.getChildren());
        sendRequest();
    }

    public void sendRequest() {
        mSave.setEnabled(false);
        RestClient.getApiService().setBabyProfile(request, new RestCallback<String>() {
            @Override
            public void failure(RestError restError) {
                Toast.makeText(NewbornActivity.this, "Error", Toast.LENGTH_SHORT).show();
                mSave.setEnabled(true);
            }

            @Override
            public void success(String s, Response response) {
                mSave.setEnabled(true);
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(NewbornActivity.this);
                DbUser user = databaseHelper.getUser(PreferencesManager.getInstance().getEmail());
                String children = "";
                for (Child child : request.getChildren()) {
                    if (children.isEmpty()) {
                        children = children + child.getName();
                    } else {
                        children = children + "/" + child.getName();
                    }
                }
                user.setChildren(children);
                databaseHelper.updateUser(user);
                finish();
            }
        });
    }

    public static void launch(Context context) {
        Intent launch = new Intent(context, NewbornActivity.class);
        context.startActivity(launch);
    }

}

package com.sdex.webteb.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.NewbornAdapter;
import com.sdex.webteb.database.DatabaseHelper;
import com.sdex.webteb.database.model.DbUser;
import com.sdex.webteb.dialogs.DatePickerFragmentDialog;
import com.sdex.webteb.model.Child;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.request.BabyProfileRequest;
import com.sdex.webteb.rest.response.BabyProfileResponse;
import com.sdex.webteb.utils.DateUtil;
import com.sdex.webteb.utils.PreferencesManager;
import com.sdex.webteb.utils.ResourcesUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.client.Response;

/**
 * Created by Yuriy Mysochenko on 24.03.2015.
 */
public class NewbornActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    public static final int BIRTH_DATE = 3;
    public static final int NEWBORN_ACTIVITY_REQUEST_CODE = 19937;
    public static final int REQUEST_GET_DATE = 0;

    private NewbornAdapter mAdapter;
    @InjectView(R.id.children_list)
    ListView mList;
    @InjectView(R.id.save)
    Button mSave;
    @InjectView(R.id.select_date)
    TextView mDate;
    @InjectView(R.id.text)
    TextView mText;
    private List<Child> mChildren;

    private BabyProfileRequest request = new BabyProfileRequest();
    private RestCallback<BabyProfileResponse> getBabyProfileCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendAnalyticsScreenName(R.string.screen_congratulations);

        mText.setText(ResourcesUtil.getString(this, "newborn_text"));

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

        Date date = DateUtil.getCurrentDate();
        String textDate = DateUtil.formatDate(date, "MMM dd, yyyy");
        mDate.setText(textDate);

        getBabyProfileCallback = new RestCallback<BabyProfileResponse>() {
            @Override
            public void failure(RestError restError) {
            }

            @Override
            public void success(BabyProfileResponse babyProfileResponse, Response response) {
                List<Child> children = babyProfileResponse.getChildren();
                if (babyProfileResponse != null && children != null) {
                    request.setFamilyRelation(babyProfileResponse.getFamilyRelation());
                    request.setDateType(BIRTH_DATE);
                    Date currentDate = Calendar.getInstance().getTime();
                    String requestDate = DateUtil.formatDate(currentDate, "yyyy-MM-dd'T'HH:mm:ssZ");
                    request.setDate(requestDate);
                    if (children.isEmpty()) {
                        children.add(new Child());
                    }
                    mAdapter.setItems(children);
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

    @OnClick(R.id.select_date)
    public void selectDate() {
        String dateString = mDate.getText().toString();
        DialogFragment dialog = new DatePickerFragmentDialog();
        if (!TextUtils.isEmpty(dateString)) {
            Bundle args = new Bundle();
            args.putString(DatePickerFragmentDialog.EXTRA_DATE, dateString);
            dialog.setArguments(args);
        }
        dialog.show(getSupportFragmentManager(), null);
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
                PreferencesManager.getInstance().setCurrentDate(String.valueOf(0),
                        PreferencesManager.DATE_TYPE_MONTH);
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

    @Override
    public void onBackPressed() {
    }

    public static void launch(Activity activity) {
        Intent launch = new Intent(activity, NewbornActivity.class);
        activity.startActivityForResult(launch, NEWBORN_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (year >= 0 && monthOfYear >= 0 && dayOfMonth >= 0) {
            Calendar date = new GregorianCalendar(year, monthOfYear, dayOfMonth);
            Date time = date.getTime();
            String requestDate = DateUtil.formatDate(time, "yyyy-MM-dd'T'HH:mm:ssZ");
            String textDate = DateUtil.formatDate(time, "MMM dd, yyyy");
            if (DateUtil.compareDatesWithToday(time, DateUtil.getCurrentDate(), true)) {
                Toast.makeText(this, getString(R.string.please_select_correct_date), Toast.LENGTH_SHORT).show();
            } else {
                mDate.setText(textDate);
                request.setDate(requestDate);
            }
        }
    }
}

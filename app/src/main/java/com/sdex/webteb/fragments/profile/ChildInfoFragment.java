package com.sdex.webteb.fragments.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.activities.SetupProfileActivity;
import com.sdex.webteb.adapters.ChildrenAdapter;
import com.sdex.webteb.fragments.BaseFragment;
import com.sdex.webteb.model.Child;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by MPODOLSKY on 03.02.2015.
 */
public class ChildInfoFragment extends BaseFragment {

    public static final String EDIT_MODE = "EDIT_MODE";
    public static final String CHILDREN = "CHILDREN";
    private ChildrenAdapter mAdapter;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.childs_list)
    ListView mList;
    private List<Child> mChildren;
    private boolean isInEditMode;

    public static ChildInfoFragment newInstance(List<Child> children, boolean isInEditMode) {
        ChildInfoFragment frag = new ChildInfoFragment();
        Bundle args = new Bundle();
        args.putBoolean(EDIT_MODE, isInEditMode);
        if (children != null) {
            Parcelable parcChildren = Parcels.wrap(children);
            args.putParcelable(SetupProfileActivity.CHILDREN, parcChildren);
        }
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mChildren = new ArrayList<>();
        final Bundle args = getArguments();
        if (args != null) {
            final Parcelable children = args.getParcelable(SetupProfileActivity.CHILDREN);
            if (children != null) {
                mChildren = Parcels.unwrap(children);
            }
            isInEditMode = args.getBoolean(EDIT_MODE);
        }

        mAdapter = new ChildrenAdapter(getActivity());
        mAdapter.setCallback(new ChildrenAdapter.Callback() {
            @Override
            public void onDeleteChild(final int position) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity())
                        .setMessage(R.string.are_u_sure_u_want_to_delete_child)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAdapter.removeChild(position);
                                updateChildrenCount();
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
        if (mChildren.isEmpty()) {
            mChildren.add(new Child());
        }
        mAdapter.setItems(mChildren);
        if (isInEditMode) {
            mAdapter.makeAllComplete();
        }
        mList.setAdapter(mAdapter);
        updateChildrenCount();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_child_info;
    }

    @OnClick(R.id.add)
    public void addChild() {
        Child child = new Child();
        child.setName("");
        mAdapter.addChild(child);
        mAdapter.notifyDataSetChanged();
        mList.setSelection(mAdapter.getCount());
        updateChildrenCount();
    }

    @OnClick(R.id.done)
    public void done() {
        if (getActivity() instanceof SetupProfileActivity) {
            ((SetupProfileActivity) getActivity()).setChildren(mAdapter.getChildren());
            ((SetupProfileActivity) getActivity()).saveChanges();
        }
    }

    private void updateChildrenCount() {
        int count = mAdapter.getCount();
        if (count == 1) {
            mTitle.setText(R.string.child_count);
        }
        if (count > 1) {
            String title = getString(R.string.children_count);
            mTitle.setText(String.format(title, count));
        }
    }
}

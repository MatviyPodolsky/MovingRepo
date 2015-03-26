package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.model.EntityField;
import com.sdex.webteb.model.EntityFieldBody;
import com.sdex.webteb.rest.response.EntityResponse;

import org.parceler.Parcels;

import java.util.List;

import butterknife.InjectView;

public class PreviewFragment extends BaseMainFragment {

    public static final String NAME = PreviewFragment.class.getSimpleName();

    public static final String ENTITY = "ENTITY";

    @InjectView(R.id.content)
    WebView contentView;
    @InjectView(R.id.title)
    TextView title;
    private EntityResponse entity;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        entity = Parcels.unwrap(args.getParcelable(ENTITY));
        title.setText(entity.getName());
        List<EntityField> fields = entity.getFields();
        if (fields != null && fields.size() > 0) {
            EntityField entityField = fields.get(0);
            if (entityField != null) {
                List<EntityFieldBody> bodies = entityField.getBodies();
                if (bodies != null && bodies.size() > 0) {
                    EntityFieldBody entityFieldBody = bodies.get(0);
                    String content = entityFieldBody.getContent();
                    if (entityFieldBody.getTypeID() == EntityFieldBody.Html && content != null) {
                        contentView.loadData(content, "text/html; charset=UTF-8", null);
                    }
                }
            }
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_article;
    }

}

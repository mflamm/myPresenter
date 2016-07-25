package com.artifex.mupdfdemo.view.page;


import android.content.Context;
import android.widget.ImageView;

// Make our ImageViews opaque to optimize redraw
public class OpaqueImageView extends ImageView {

    public OpaqueImageView(Context context) {
        super(context);
    }

    @Override
    public boolean isOpaque() {
        return true;
    }
}

package com.twtstudio.retrox.bike.common;

import android.content.Context;

import com.twtstudio.retrox.bike.api.BikeApiClient;


/**
 * Created by jcy on 2016/8/7.
 */

public abstract class BikePresenter extends Presenter {
    public BikePresenter(Context context) {
        super(context);
    }

    @Override
    public void onDestroy() {
        BikeApiClient.getInstance().unSubscribe(mContext);
    }
}

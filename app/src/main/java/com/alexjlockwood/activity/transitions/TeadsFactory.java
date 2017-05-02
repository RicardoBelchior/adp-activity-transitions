package com.alexjlockwood.activity.transitions;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import tv.teads.logger.ConsoleLog;
import tv.teads.sdk.publisher.TeadsAd;
import tv.teads.sdk.publisher.TeadsConfiguration;
import tv.teads.sdk.publisher.TeadsContainerType;

public class TeadsFactory {

    public static TeadsAd createArticleDetailAdvert(Activity activity, RecyclerView recyclerView, String publisherId) {

        TeadsConfiguration teadsConfig = new TeadsConfiguration();
        teadsConfig.adPosition = 4;

        ConsoleLog.setLogLevel(ConsoleLog.LogLevel.debug);

        return new TeadsAd.TeadsAdBuilder(activity, publisherId)
                .viewGroup(recyclerView)
                .containerType(TeadsContainerType.inRead)
                .configuration(teadsConfig)
                .build();
    }

    @NonNull
    public static String getPublisherId(Context context) {
        return context.getResources().getString(R.string.teads_publisher_id);
    }

}
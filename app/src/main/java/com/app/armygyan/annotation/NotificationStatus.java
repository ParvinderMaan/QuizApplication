package com.app.armygyan.annotation;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.SOURCE)
@StringDef({ NotificationStatus.OFF, NotificationStatus.ON})
public @interface NotificationStatus {
    String OFF = "0";
    String ON = "1";
}

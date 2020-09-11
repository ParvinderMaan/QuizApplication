package com.app.armygyan.annotation;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.SOURCE)
@StringDef({NotificationType.CHAPTER,NotificationType.QUIZ,NotificationType.GENERAL})
public @interface NotificationType {
    String QUIZ = "1";
    String CHAPTER = "2";
    String GENERAL = "3";
}

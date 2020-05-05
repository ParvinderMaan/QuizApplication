package com.app.quiz.annotation;

import androidx.annotation.IntDef;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.SOURCE)
@IntDef ({ Status.FAILURE, Status.SUCCESS})
public @interface Status {
    int FAILURE = 0;
    int SUCCESS = 1;
}
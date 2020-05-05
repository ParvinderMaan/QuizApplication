package com.app.quiz.annotation;


import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



@Retention(RetentionPolicy.SOURCE)
@StringDef({ Language.ENGLISH, Language.HINDI})
public @interface Language {
      String ENGLISH = "en";
      String HINDI = "hi";
}


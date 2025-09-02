package com.expenditure_diary.expenditure_diary.annotate;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SecondaryDbRepository {}
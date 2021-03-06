package com.sdex.webteb.rest.service;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import retrofit.http.RestMethod;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(METHOD)
@Retention(RUNTIME)
@RestMethod(hasBody = true, value = "DELETE")
public @interface DELETEWITHBODY {
    String value();
}
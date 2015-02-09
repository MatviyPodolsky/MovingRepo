package com.sdex.webteb.rest.request;

/**
 * Created by Yuriy Mysochenko on 09.02.2015.
 */
public class LoginUserRequest {


//    grant_type=password&username=alice%40example.com&password=Password1!

    public String grant_type = "password";
    public String username;
    public String password;

}

package com.rex.wepeiyang.ui.gpa;

/**
 * Created by sunjuntao on 15/11/22.
 */
public interface GpaPresenter {
    void getGpaWithoutToken();

    void getGpaWithToken(String token, String captcha);
}

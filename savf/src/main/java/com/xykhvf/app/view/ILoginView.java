package com.xykhvf.app.view;


import com.xykhvf.app.model.Result;
import com.xykhvf.app.model.response.Dispense;
import com.xykhvf.app.model.response.ResLoginModel;

import java.util.List;

/**
 * Created by Administrator on 2017-08-28.
 */
public interface ILoginView {
    void setLoginName(String text);

    void setLoginPwd(String text);

    void setList(List<Dispense> list);

    void setVersion(String version);

    void loginResult(Result<ResLoginModel> result);
}

package com.xykhvf.app.dao;

import com.vcom.publiclibrary.util.okhttp.callback.StringCallback;
import com.xykhvf.app.model.request.ReqLoginModel;
import com.xykhvf.app.model.request.ReqPayModel;
import com.xykhvf.app.model.request.ReqReport;

public interface IApiDao {

    /**
     * 获取平台接口地址
     */
    void getUri(StringCallback callback);

    /**
     * 登录
     *
     * @param model
     * @param callback
     */
    void login(ReqLoginModel model, StringCallback callback);

    String loginSync(ReqLoginModel model);

    /**
     * 根据小区标识获取指定小区的楼栋信息
     *
     * @param id
     * @param callback
     */
    void getBuilding(Long id, String token, StringCallback callback);

    /**
     * 根据某楼栋的标识获取该楼栋的房间相关信息
     *
     * @param id
     * @param callback
     */
    void getHouses(Long id, String token, StringCallback callback);

    /**
     * 根据房间号获取房间的未缴费的详细信息
     *
     * @param id
     * @param callback
     */
    void getOrders(Long id, String token, StringCallback callback);

    /**
     * 判断是否可以进行支付
     *
     * @param ids      逗号分割
     * @param token
     * @param callback
     */
    void checkPay(String ids, String token, StringCallback callback);

    /**
     * 提交交易成功的订单
     *
     * @param model
     * @param token
     * @param callback
     */
    void submitPay(ReqPayModel model, String token, StringCallback callback);

    /**
     * 根据时间范围统计缴费的基本情况
     *
     * @param report
     * @param token
     * @param callback
     */
    void getReport(ReqReport report, String token, StringCallback callback);

    /**
     * 通过mac获取商户号
     *
     * @param mac
     * @param token
     * @param callback
     */

    void getMerchat(String mac, String token, StringCallback callback);
}

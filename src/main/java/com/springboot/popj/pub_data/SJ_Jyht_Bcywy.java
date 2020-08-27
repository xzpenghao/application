package com.springboot.popj.pub_data;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.List;

/**
 * @author chenb
 * @version 2020/8/27
 * description：交易合同的补充与违约类
 */
@Data
public class SJ_Jyht_Bcywy {
    private String infoId;      //主键-和合同的主键一致
    private String ydtj;        //约定条件，（约定的时间期限或其它条件）
    private String ydlxtk;      //应当履行条款

    private String jfwyzfqx;    //甲方违约支付期限
    private String jfwyzfdjbs;  //甲方违约支付定金倍数
    private String sfwyznjfe;   //双方违约滞纳金份额

    private String jfdmcftkqx;    //甲方多卖惩罚退款期限
    private String jfdmcflxzffe;  //甲方多卖惩罚利息支付份额
    private String jfdmcfwyjzfbl; //甲方多卖惩罚违约金支付比例（同已付款金额比例）

    private List<SJ_Jyht_Bcywymx> bcmxes;    //补充明细列表
    private List<SJ_Jyht_Bcywymx> wymxes;    //违约明细列表
}

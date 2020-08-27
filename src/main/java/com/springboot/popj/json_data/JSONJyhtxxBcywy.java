package com.springboot.popj.json_data;

/**
 * @author chenb
 * @version 2020/8/27
 * description：合同补充与违约说明
 */
public class JSONJyhtxxBcywy {
    private String infoId;      //主键-和合同的主键一致
    private String ydtj;        //约定条件，（约定的时间期限或其它条件）
    private String ydlxtk;      //应当履行条款

    private String jfwyzfqx;    //甲方违约支付期限
    private String jfwyzfdjbs;  //甲方违约支付定金倍数
    private String sfwyznjfe;   //双方违约滞纳金份额

    private String jfdmcftkqx;    //甲方多卖惩罚退款期限
    private String jfdmcflxzffe;  //甲方多卖惩罚利息支付份额
    private String jfdmcfwyjzfbl; //甲方多卖惩罚违约金支付比例（同已付款金额比例）

    private String bcmxes;    //补充明细列表
    private String wymxes;    //违约明细列表

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getYdtj() {
        return ydtj;
    }

    public void setYdtj(String ydtj) {
        this.ydtj = ydtj;
    }

    public String getYdlxtk() {
        return ydlxtk;
    }

    public void setYdlxtk(String ydlxtk) {
        this.ydlxtk = ydlxtk;
    }

    public String getJfwyzfqx() {
        return jfwyzfqx;
    }

    public void setJfwyzfqx(String jfwyzfqx) {
        this.jfwyzfqx = jfwyzfqx;
    }

    public String getJfwyzfdjbs() {
        return jfwyzfdjbs;
    }

    public void setJfwyzfdjbs(String jfwyzfdjbs) {
        this.jfwyzfdjbs = jfwyzfdjbs;
    }

    public String getSfwyznjfe() {
        return sfwyznjfe;
    }

    public void setSfwyznjfe(String sfwyznjfe) {
        this.sfwyznjfe = sfwyznjfe;
    }

    public String getJfdmcftkqx() {
        return jfdmcftkqx;
    }

    public void setJfdmcftkqx(String jfdmcftkqx) {
        this.jfdmcftkqx = jfdmcftkqx;
    }

    public String getJfdmcflxzffe() {
        return jfdmcflxzffe;
    }

    public void setJfdmcflxzffe(String jfdmcflxzffe) {
        this.jfdmcflxzffe = jfdmcflxzffe;
    }

    public String getJfdmcfwyjzfbl() {
        return jfdmcfwyjzfbl;
    }

    public void setJfdmcfwyjzfbl(String jfdmcfwyjzfbl) {
        this.jfdmcfwyjzfbl = jfdmcfwyjzfbl;
    }

    public String getBcmxes() {
        return bcmxes;
    }

    public void setBcmxes(String bcmxes) {
        this.bcmxes = bcmxes;
    }

    public String getWymxes() {
        return wymxes;
    }

    public void setWymxes(String wymxes) {
        this.wymxes = wymxes;
    }
}

package com.springboot.util.chenbin;

import com.alibaba.fastjson.JSONArray;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.chenbin.personnel.resp.OtherResponseEntity;
import com.springboot.entity.newPlat.query.resp.Dzzzxx;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.springboot.constant.newPlat.chenbin.HandleKeywordConstant.BDC_INTF_HANDLE_RETURN_CODE_SUCCESS;

@Slf4j
public class TestRun {
    public static void main(String[] args) {
        getIdTypeNumber("身份证", "身份证,1$社会信用统一代码,8$组织机构代码,6");
    }

    public static String getIdTypeNumber(String idName, String idTypess) {
        if (StringUtils.isBlank(idName)) {
            return "1";//或者抛异常
        }
        String idd = "99";
        String[] idTypes = idTypess.split("$");
        System.err.println(JSONArray.toJSONString(idTypes));
        for (int i = 0; i < idTypes.length; i++) {
            String idType = idTypes[i];
            System.out.println("整体：" + idType);
            String[] id_ = idType.split(",");
            String idTypeName = id_[0];
            System.out.println("本次待比较：" + idTypeName);
            System.out.println("本次传入：" + idName);
            if (idType.contains(idName)) {
                System.out.println("为：" + id_[1]);
                idd = id_[1];
            }
        }
        return idd;
    }

    public static OtherResponseEntity<List<Dzzzxx>> mockDzzz(String ywhstr){
        OtherResponseEntity<List<Dzzzxx>> respDzzzs = new OtherResponseEntity<>();
        List<Dzzzxx> dzzzxxes = new ArrayList<>();
        String []ywhs = ywhstr.split(",");
        for(String ywh:ywhs){
            Dzzzxx dzzzxx = new Dzzzxx();
            dzzzxx.setYwbh(ywh);
            dzzzxx.setDjlx("抵押登记");
            dzzzxx.setDzzzfjxx(
                    UUID.randomUUID().toString().replaceAll("-","").toUpperCase().substring(0,10) +"-"+ywh+ ".pdf" );
            dzzzxx.setZh("苏(2020)宿迁市不动产证明第"+ywh+"号");
            dzzzxx.setZhlx("DYZMH");
            dzzzxx.setDzzzwj(Base64Util.pdfToBase64(new File("D:\\file\\pdf\\dyECert\\INFO-20200225-7622151B22E.pdf")));
            dzzzxxes.add(dzzzxx);
        }
        respDzzzs.setCode(BDC_INTF_HANDLE_RETURN_CODE_SUCCESS);
        respDzzzs.setMsg("pull successfully");
        respDzzzs.setData(dzzzxxes);
        return respDzzzs;
    }

    //下载附件
    public static byte[] downloadFileLocal(String fjlj){
        File file = new File(fjlj);
        if(file.exists()&&file.isFile()){
            FileInputStream inputStream = null;
            byte[] data = null;
            try {
                inputStream = new FileInputStream(file);
                data = new byte[(int)file.length()];
                int length = inputStream.read(data);
                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                log.error("附件不存在"+e);
                throw new ZtgeoBizException("您所查找的附件不存在");
            } catch (IOException e){
                e.printStackTrace();
                log.error("文件流操作异常"+e);
                throw new ZtgeoBizException("文件流操作异常");
            }
            return data;
        }else{
            return null;
        }
    }
}

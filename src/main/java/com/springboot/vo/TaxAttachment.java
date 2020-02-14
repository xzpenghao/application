package com.springboot.vo;

import lombok.Data;

import java.util.List;

/**
 * @author sk
 * @version 2020/1/19
 */
@Data
public class TaxAttachment {
    //申请编号
    private String sqbh;
    //返回电子税票集合
    private List<ETax> dzsps;

    @Data
    public static class ETax{
        //地税申请号码
        private String dzsphm;
        //提供单位
        private String tgdw;
        //Base64字符串
        private String base64;
        //filePath
        private String filePath;
        //isFtp
        private String isFtp;
        //文件大小
        private Integer fileSize;

        public ETax() {
        }

        public ETax(ETax eTax, String filePath, String isFtp,Integer fileSize){
            this.dzsphm = eTax.getDzsphm();
            this.tgdw = eTax.getTgdw();
            this.filePath = filePath;
            this.isFtp = isFtp;
            this.fileSize = fileSize;
        }
    }

    public TaxAttachment() {
    }

    public TaxAttachment(TaxAttachment taxAttachment){
        this.sqbh = taxAttachment.getSqbh();
        this.dzsps = taxAttachment.getDzsps();
    }
}

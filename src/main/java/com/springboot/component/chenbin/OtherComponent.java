package com.springboot.component.chenbin;

import com.springboot.component.chenbin.file.FromFTPDownloadComponent;
import com.springboot.component.chenbin.file.ToFTPUploadComponent;
import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.SJ_Fjfile;
import com.springboot.popj.registration.ImmovableFile;
import com.springboot.util.TimeUtil;
import com.springboot.util.chenbin.BusinessDealBaseUtil;
import com.springboot.util.chenbin.IDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class OtherComponent {

    @Autowired
    private FromFTPDownloadComponent fromFTPDownloadComponent;
    @Autowired
    private ToFTPUploadComponent toFTPUploadComponent;

    public List<ImmovableFile> getInnerFileListByOut(List<SJ_Fjfile> fileVoList,boolean dealFile) {
        List<ImmovableFile> fileList = new ArrayList<ImmovableFile>();
        for (SJ_Fjfile file : fileVoList) {
            ImmovableFile immovableFile = new ImmovableFile();
            immovableFile.setFileName(file.getFileName());
            immovableFile.setpName(file.getLogicPath());
            immovableFile.setFileType(file.getFileExt());
            immovableFile.setFileSize(file.getFileSize());
            int sort = BusinessDealBaseUtil.getFileXh(file, fileVoList);

            //排序
            if (sort == 0) {
                throw new ZtgeoBizException("附件异常");
            }
            immovableFile.setFileSequence(Integer.toString(sort));

            //获取附件并上传至指定地址
            System.out.println("FTP路径：" + file.getFtpPath());
            if(dealFile) {
                String applyDate = file.getFileSubmissionTime();
                if (StringUtils.isBlank(applyDate) || applyDate.length() != 19) {
                    TimeUtil.getTimeString(new Date());
                }
                if (fromFTPDownloadComponent.downFile(
                        file.getFtpPath().substring(0, file.getFtpPath().lastIndexOf("\\")),
                        file.getFtpPath().substring(file.getFtpPath().lastIndexOf("\\") + 1),
                        file
                )) {
                    //开始进行上传
                    String binid = IDUtil.getBinID();//binid的产生
                    String fileAdress = "/" + applyDate.substring(0, 4) + "/" + applyDate.substring(5, 7) + "/" + applyDate.substring(8, 10) + "/" + binid + "." + file.getFileExt();
                    if (toFTPUploadComponent.uploadFile(fileAdress, file.getFileContent())) {
                        immovableFile.setFileAdress(fileAdress);
                    } else {
                        //记录附件上传失败并记录返回该信息
                        throw new ZtgeoBizException("附件上传异常");
                    }
                } else {
                    //记录附件下载失败并记录返回该信息
                    throw new ZtgeoBizException("附件下载异常");
                }
            } else {
                if(StringUtils.isBlank(file.getSaveType()) || !"0".equals(file.getSaveType())) {
                    immovableFile.setFileAdress(file.getFtpPath());
                }
            }
            if(StringUtils.isNotBlank(immovableFile.getFileAddress())) {
                fileList.add(immovableFile);
            }
        }
        return fileList;
    }
}

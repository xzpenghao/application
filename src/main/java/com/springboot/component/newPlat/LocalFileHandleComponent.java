package com.springboot.component.newPlat;

import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.newPlat.settingTerm.FtpSettings;
import com.springboot.util.TimeUtil;
import com.springboot.util.chenbin.ErrorDealUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.UUID;

/**
 * @author chenb
 * @version 2020/8/12
 * description：本地附件处理Component
 */
@Slf4j
@Component
public class LocalFileHandleComponent {

    @Autowired
    private FtpSettings ftpSettings;

    public String uploadFileLocal(MultipartFile file, String processId){
        String basePath = ftpSettings.getIfLocalBasePath();
        basePath = basePath + TimeUtil.getDateString(new Date()) + "/" +processId;
        File fileDir = new File(basePath);
        if  (!fileDir.exists()  || !fileDir.isDirectory()){
            //创建文件夹
            fileDir.mkdir();
        }
        String fileName = file.getOriginalFilename();
        fileName = fileName.substring(0, fileName.lastIndexOf("."))
                + "-" + UUID.randomUUID().toString().substring(0,10)
                +fileName.substring(fileName.lastIndexOf("."));
        String filePath = fileDir+"/"+fileName;
        //以获取到的文件名命名文件，生成文件对象
        File destFile = new File(filePath);
        //文件对象读获取到的字节数组
        InputStream file_is = null;
        try {
            file_is = file.getInputStream();
            byte[] fileData = new byte[(int) file.getSize()];
            file_is.read(fileData);
            //使用aphache工具处理文件
            FileUtils.writeByteArrayToFile(destFile,fileData);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("数据写入异常："+e);
            throw new ZtgeoBizException("数据写入本地异常");
        } finally {
            try {
                file_is.close();
            } catch (IOException e) {
                e.printStackTrace();
                log.error("数据流关闭异常："+e);
                throw new ZtgeoBizException("数据流关闭异常");
            }
        }

        return filePath;
    }

    public byte[] downloadFileLocal(String fjlj){
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

    //删除文件
    public boolean deleteFileLocal(String fjlj){
        boolean result = false;
        try {
            File srcFile = new File(fjlj);
            if (!srcFile.exists()) {
                log.error("----------------被删除的文件不存在{}---------------", fjlj);
                return false;
            }
            result = srcFile.delete();
            if(result){
                log.info("-------------------本地附件删除成功-------------");
            }else{
                log.info("-------------------本地附件删除失败-------------");
            }
        }catch (Exception e){
            log.error(ErrorDealUtil.getErrorInfo(e));
        }
        return result;
    }
}

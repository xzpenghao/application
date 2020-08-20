package com.springboot.component.newPlat;

import com.springboot.config.ZtgeoBizException;
import com.springboot.entity.newPlat.result.FtpFileDownResult;
import com.springboot.entity.newPlat.settingTerm.FtpSettings;
import com.springboot.entity.newPlat.transInner.req.fromZY.domain.TransFjxx;
import com.springboot.entity.newPlat.transInner.req.fromZY.domain.TwoOrNFjxx;
import com.springboot.util.chenbin.ErrorDealUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * @author chenb
 * @version 2020/8/12
 * description：全新附件操作component类
 */
@Slf4j
@Component
public class FileInteractComponent {

    @Autowired
    private FtpFileHandleComponent ftpFileHandleComponent;
    @Autowired
    private LocalFileHandleComponent localFileHandleComponent;

    /**
     * 描述：附件传输同步处理
     * 作者：chenb
     * 日期：2020/8/19
     * 参数：[willAsynFiles]
     * 返回：void
     * 更新记录：更新人：{}，更新日期：{}
    */
    public void SynchTrans(List<TwoOrNFjxx> willAsynFiles){
        //下载并上传附件
        handleTransFiles(willAsynFiles);
    }

    /**
     * 描述：附件传输异步执行线程
     * 作者：chenb
     * 日期：2020/8/13
     * 参数：[willAsynFiles]
     * 返回：void
     * 更新记录：更新人：{}，更新日期：{}
     */
    public void AsynchTrans(List<TwoOrNFjxx> willAsynFiles){
        ExecutorService executor = Executors.newCachedThreadPool();
        FutureTask<String> futureTransFiles = new FutureTask<String>(new Callable<String>() {
            public String call() {//建议抛出异常
                log.info("附件的异步上传线程启动");
                //执行发送电部门过户申请
                handleTransFiles(willAsynFiles);
                return "附件的异步上传执行结束";
            }
        });
        executor.execute(futureTransFiles);
    }

    /**
     * 描述：附件传输实际执行线程
     * 作者：chenb
     * 日期：2020/8/13
     * 参数：[willAsynFiles]
     * 返回：void
     * 更新记录：更新人：{}，更新日期：{}
     */
    public void handleTransFiles(List<TwoOrNFjxx> willAsynFiles){
        try{
            if(willAsynFiles!=null){
                for(TwoOrNFjxx willAsynFile:willAsynFiles){
                    log.info("进入附件处理，异步处理时间是："+new Date().getTime());
                    byte[] comeBytes = null;
                    if("0".equals(willAsynFile.getCome().getSaveType())){   //本地加载
                        comeBytes = localFileHandleComponent.downloadFileLocal(willAsynFile.getCome().getPath());
                    } else if("-1".equals(willAsynFile.getCome().getSaveType())) {  //base64处理
                        if(StringUtils.isNotBlank(willAsynFile.getCome().getFileBase64())) {
                            BASE64Decoder base64Decoder = new BASE64Decoder();
                            comeBytes = base64Decoder.decodeBuffer(willAsynFile.getCome().getFileBase64());
                        }
                    } else {    //ftp加载
                        FtpFileDownResult fileFtpDownResult =ftpFileHandleComponent.downFile(
                                willAsynFile.getCome().getIfFtpKey(),
                                willAsynFile.getCome().getPath().substring(0,willAsynFile.getCome().getPath().lastIndexOf("/")),
                                willAsynFile.getCome().getPath().substring(willAsynFile.getCome().getPath().lastIndexOf("/")+1)
                        );
                        if(fileFtpDownResult.isSuccess()){
                            comeBytes = fileFtpDownResult.getFjnr();
                        }else{
                            throw new ZtgeoBizException("【"+willAsynFile.getCome().getPath()+"】附件异步上传时，源文件下载失败！错误信息为："+fileFtpDownResult.getError());
                        }
                    }

                    if(comeBytes!=null){
                        List<TransFjxx> toFjxxs = willAsynFile.getTo();
                        if(toFjxxs!=null) {
                            for(TransFjxx toFjxx:toFjxxs) {
                                if ("0".equals(toFjxx.getSaveType())) {
                                    localFileHandleComponent.uploadFileLocal(
                                            new ByteArrayInputStream(comeBytes),
                                            toFjxx.getPath().substring(0, toFjxx.getPath().lastIndexOf("\\")),
                                            toFjxx.getPath().substring(toFjxx.getPath().lastIndexOf("\\") + 1),
                                            comeBytes.length
                                    );
                                } else {
                                    if(toFjxx.getPath().contains("\\"))
                                        toFjxx.setPath(toFjxx.getPath().replaceAll("\\\\","/"));
                                    ftpFileHandleComponent.uploadFile(
                                            toFjxx.getIfFtpKey(),
                                            toFjxx.getPath().substring(0, toFjxx.getPath().lastIndexOf("/")),
                                            toFjxx.getPath().substring(toFjxx.getPath().lastIndexOf("/") + 1),
                                            new ByteArrayInputStream(comeBytes)
                                    );
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e){
            log.error("异步处理附件时，程序执行出现错误，错误信息描述："+ ErrorDealUtil.getErrorInfo(e));
        }
    }
}

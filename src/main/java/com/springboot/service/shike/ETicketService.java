package com.springboot.service.shike;

import com.springboot.vo.TaxAttachment;

import java.util.List;

/**
 * @author sk
 * @version 2020/1/16
 */
public interface ETicketService {
    List<TaxAttachment> ETicketQuery(List<String> receiptNumbers, String ftpFlag);
}

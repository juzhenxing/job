package com.zxkj.job.bean.po;

import com.baomidou.mybatisplus.annotations.TableName;
import com.zxkj.job.common.bean.BasePo;
import lombok.Data;

/**
 * 附件信息类
 */
@Data
@TableName("t_job_attachment_information")
public class AttachmentInformationPo extends BasePo<AttachmentInformationPo> {

    private Long resumeId;

    private String name;

    private String url;

}

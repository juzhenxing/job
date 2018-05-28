package com.zxkj.job.exp;

import com.zxkj.job.common.exp.BaseException;
import lombok.Getter;

/**
 * 规则为[类型_错误信息]
 * 如：EXAM_ERROR_TYPE
 *
 * @author jzx 2018/1/31 14:40
 */
@Getter
public enum JobOutput {

    NULL_EMAIL(101, "邮箱不能为空"),

    WRONG_EMAIL(102, "邮箱格式不正确"),

    REPEAT_EMAIL(103, "邮箱不可用"),

    NULL_UNDERGRADUATE(104, "用户不存在"),

    WRONG_PASSWORD(105, "账号密码错误"),

    UNDERGRADUATE_ADD(106, "用户注册失败"),

    NULL_CODE(107, "验证码已失效，请重新获取"),

    WRONG_CODE(108, "验证码不正确"),

    NULL_EMAILKEY(109, "session中不存在此emailKey"),

    ENTERPRISE_ADD(110, "企业注册失败"),

    NULL_USERNAME(111, "用户名不能为空"),

    REPEAT_USERNAME(112, "用户名已存在"),

    ENTERPRISE_UPDATE(113, "企业更新失败"),

    USED_CODE(114, "校验码已使用, 您已经注册成功"),

    NULL_ENTERPRISE(115, "该企业账户不存在"),

    LICENSE_TEMP_DIRECTORY(116, "创建临时保存营业执照的目录失败"),

    ADMINISTRATOR_ADD(117, "管理员注册失败"),

    NULL_ADMINISTRATOR(118, "该管理员账户不存在"),

    ADMINISTRATOR_UPDATE(119, "管理员更新失败"),

    ENTERPRISE_ACTIVATE(120, "您的账户还未激活"),

    NULL_ENTERPRISE_ID(121, "企业id不存在"),

    NOT_LOGGED_IN(122, "您还未登录"),

    WRONG_ENDTIME(123, "结束时间应大于开始时间"),

    NULL_CAREERTALK(124, "该宣讲会不存在"),

    CAREERTALK_DELETE_TIME(125, "该宣讲会开始时间不足一天，无法删除！"),

    CAREERTALK_UPDATE_TIME(126, "该宣讲会开始时间不足一天，无法更新！"),

    NULL_ID(127, "id不能为空"),

    NULL_PROFESSIONAL(128, "该招聘岗位不存在"),

    CAMPUS_RECRUITMENT_INSERT(129, "校招网申记录插入失败"),

    NULL_CAMPUS_RECRUITMENT(130, "该校招网申记录不存在"),

    CAMPUS_RECRUITMENT_DELETE(131, "校招网申记录删除失败"),

    CAMPUS_RECRUITMENT_UPDATE(132, "校招网申记录更新失败"),

    UNDERGRADUATE_UPDATE(133, "用户更新失败"),

    NULL_RESUME(134, "该简历不存在"),

    NULL_EDUCATION_BACKGROUND(135, "该教育背景记录不存在"),

    RESUME_ALREADY_ARRANGED(136, "本次投递已被企业处理，简历不可修改"),

    NULL_DELIVERYINFORMATIONPO(137, "该投递记录不存在"),

    EXIST_DELIVERYINFORMATIONPO(137, "该简历已投递, 无法删除"),

    APPLY_ALREADY_ARRANGED(136, "申请已被处理完成，不可修改"),

    CAMPUSRECRUITMENT_PROFESSIONAL_DELETE(139, "该校招网申记录所提供的职位已有人申请, 无法删除"),

    DELIVERYINFORMATIONPO_UPDATE(140, "投递记录更新失败"),

    COLLECTPO_EXIST(141, "已收藏"),

    CAREERTALK_ADD(142, "宣讲会增加失败"),

    CAREERTALK_PROFESSIONAL_DELETE(143, "该宣讲会记录所提供的职位已有人申请, 无法删除"),

    CAREERTALK_COLLECT_DELETE(144, "该宣讲会记录已有人收藏, 无法删除"),

    CAMPUSRECRUITMENT_COLLECT_DELETE(145, "该校招网申记录已有人收藏, 无法删除"),

    CAREERTALK_DELETE(146, "宣讲会删除失败"),

    CAMPUSRECRUITMENT_UPDATE_COLLECT(147, "该校招网申记录已有人收藏, 无法更新"),

    CAMPUSRECRUITMENT_UPDATE_PROFESSIONAL(148, "该校招网申记录所提供的职位已有人申请, 无法更新"),

    CAREERTALK_UPDATE_COLLECT(149, "该宣讲会记录已有人收藏, 无法更新"),

    CAREERTALK_UPDATE_PROFESSIONAL(150, "该宣讲会记录所提供的职位已有人申请, 无法更新"),

    CAREERTALK_UPDATE(151, "宣讲会更新失败"),

    PROFESSIONAL_DELETE_CAREERTALK(152, "该招聘岗位已被包含在某宣讲会中, 无法删除"),

    PROFESSIONAL_DELETE_CAMPUSRECRUITMENT(153, "该招聘岗位已被包含在某校园招聘中, 无法删除"),

    PROFESSIONAL_UPDATE_CAREERTALK(152, "该招聘岗位已被包含在某宣讲会中, 无法更新"),

    PROFESSIONAL_UPDATE_CAMPUSRECRUITMENT(153, "该招聘岗位已被包含在某校园招聘中, 无法更新"),
    ;

    private int code;
    private String msg;

    JobOutput(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "[" + this.code + "]" + this.msg;
    }

}

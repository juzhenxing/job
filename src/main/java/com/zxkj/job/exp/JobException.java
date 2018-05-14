package com.zxkj.job.exp;

import com.zxkj.job.common.exp.BaseException;

/**
 * @author jzx 2018/1/31 15:13
 */
public class JobException extends BaseException {

    public JobException(JobOutput jobOutput) {
        super(jobOutput);
    }

    public static final JobException NULL_EMAIL_EXCEPTION = new JobException(JobOutput.NULL_EMAIL);

    public static final JobException WRONG_EMAIL_EXCEPTION = new JobException(JobOutput.WRONG_EMAIL);

    public static final JobException REPEAT_EMAIL_EXCEPTION = new JobException(JobOutput.REPEAT_EMAIL);

    public static final JobException NULL_UNDERGRADUATE_EXCEPTION = new JobException(JobOutput.NULL_UNDERGRADUATE);

    public static final JobException WRONG_PASSWORD_EXCEPTION = new JobException(JobOutput.WRONG_PASSWORD);

    public static final JobException UNDERGRADUATE_ADD_EXCEPTION = new JobException(JobOutput.UNDERGRADUATE_ADD);

    public static final JobException NULL_CODE_EXCEPTION = new JobException(JobOutput.NULL_CODE);

    public static final JobException WRONG_CODE_EXCEPTION = new JobException(JobOutput.WRONG_CODE);

    public static final JobException NULL_EMAILKEY_EXCEPTION = new JobException(JobOutput.NULL_EMAILKEY);

    public static final JobException ENTERPRISE_ADD_EXCEPTION = new JobException(JobOutput.ENTERPRISE_ADD);

    public static final JobException NULL_USERNAME_EXCEPTION = new JobException(JobOutput.NULL_USERNAME);

    public static final JobException REPEAT_USERNAME_EXCEPTION = new JobException(JobOutput.REPEAT_USERNAME);

    public static final JobException ENTERPRISE_UPDATE_EXCEPTION = new JobException(JobOutput.ENTERPRISE_UPDATE);

    public static final JobException USED_CODE_EXCEPTION = new JobException(JobOutput.USED_CODE);

    public static final JobException NULL_ENTERPRISE_EXCEPTION = new JobException(JobOutput.NULL_ENTERPRISE);

    public static final JobException LICENSE_TEMP_DIRECTORY_EXCEPTION = new JobException(JobOutput.LICENSE_TEMP_DIRECTORY);

    public static final JobException ADMINISTRATOR_ADD_EXCEPTION = new JobException(JobOutput.ADMINISTRATOR_ADD);

    public static final JobException NULL_ADMINISTRATOR_EXCEPTION = new JobException(JobOutput.NULL_ADMINISTRATOR);

    public static final JobException ADMINISTRATOR_UPDATE_EXCEPTION = new JobException(JobOutput.ADMINISTRATOR_UPDATE);

    public static final JobException ENTERPRISE_ACTIVATE_EXCEPTION = new JobException(JobOutput.ENTERPRISE_ACTIVATE);

    public static final JobException NULL_ENTERPRISE_ID_EXCEPTION = new JobException(JobOutput.NULL_ENTERPRISE_ID);

    public static final JobException NOT_LOGGED_IN_EXCEPTION = new JobException(JobOutput.NOT_LOGGED_IN);

    public static final JobException WRONG_ENDTIME_EXCEPTION = new JobException(JobOutput.WRONG_ENDTIME);

    public static final JobException NULL_CAREERTALK_EXCEPTION = new JobException(JobOutput.NULL_CAREERTALK);

    public static final JobException CAREERTALK_DELETE_EXCEPTION = new JobException(JobOutput.CAREERTALK_DELETE);

    public static final JobException CAREERTALK_UPDATE_EXCEPTION = new JobException(JobOutput.CAREERTALK_UPDATE);

    public static final JobException NULL_ID_EXCEPTION = new JobException(JobOutput.NULL_ID);

    public static final JobException NULL_PROFESSIONAL_EXCEPTION = new JobException(JobOutput.NULL_PROFESSIONAL);

    public static final JobException CAMPUS_RECRUITMENT_INSERT_EXCEPTION = new JobException(JobOutput.CAMPUS_RECRUITMENT_INSERT);

    public static final JobException NULL_CAMPUS_RECRUITMENT_EXCEPTION = new JobException(JobOutput.NULL_CAMPUS_RECRUITMENT);

    public static final JobException CAMPUS_RECRUITMENT_DELETE_EXCEPTION = new JobException(JobOutput.CAMPUS_RECRUITMENT_DELETE);

    public static final JobException CAMPUS_RECRUITMENT_UPDATE_EXCEPTION = new JobException(JobOutput.CAMPUS_RECRUITMENT_UPDATE);

    public static final JobException UNDERGRADUATE_UPDATE_EXCEPTION = new JobException(JobOutput.UNDERGRADUATE_UPDATE);

    public static final JobException NULL_RESUME_EXCEPTION = new JobException(JobOutput.NULL_RESUME);

    public static final JobException NULL_EDUCATION_BACKGROUND_EXCEPTION = new JobException(JobOutput.NULL_EDUCATION_BACKGROUND);
}


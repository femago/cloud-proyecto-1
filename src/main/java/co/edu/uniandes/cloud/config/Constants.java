package co.edu.uniandes.cloud.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";
    public static final String DEFAULT_LANGUAGE = "es";

    //Profiles
    public static final String CLOICE_PROFILE_S3 = "s3";
    public static final String CLOICE_PROFILE_NO_S3 = "!s3";
    public static final String CLOICE_PROFILE_SQS = "sqs";
    public static final String CLOICE_PROFILE_NO_SQS = "!sqs";
    public static final String CLOICE_PROFILE_DYNAMODB = "dynamo";
    public static final String CLOICE_PROFILE_NO_DYNAMODB = "!dynamo";

    public static final String CLOICE_PROFILE_SQS_WORKER = "wk-sqs";
    public static final String CLOICE_PROFILE_SCH_WORKER = "wk-sch";

    private Constants() {
    }
}

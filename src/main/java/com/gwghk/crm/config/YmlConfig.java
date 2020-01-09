package com.gwghk.crm.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "crmConfig")
public class YmlConfig {
    private static final Logger logger = LoggerFactory.getLogger(YmlConfig.class);
    private String crmServer;
    private String unifyUrl;
    private String userManaUrl;
    private String unifyAppSecret;
    private String unifyMerchantNo;
    private String unifyAppId;
    private String kycUrl;
    private String kycKey;
    private String decryptionAppId;
    private String decryptionSecret;
    private String companyId;
	private String lbmPlatform;
    private String tokenGenUrl;
    private String decryptionAddAppUrl;
    private String decryptionUrl;
    private String decryptionKey;
    private String baseBiApiUrl;
    private String appId;
    private String csChangeImApiHost;//IM通知HOST
    private String crmMsgUrl;
    private String timezone;
    private String systemCategory;//bi chat业务平台
	private Long timeLimit;
    private Map<String,Integer> appidTimeZone;

    private String biCenterApiUrl;

    private String biCenterQueryApiUrl;

    private String biCenterChatUser;

    private String biCenterChatPass;
    public String getCrmServer() {
        return crmServer;
    }

    public void setCrmServer(String crmServer) {
        this.crmServer = crmServer;
    }

    public String getLbmPlatform() {
		return lbmPlatform;
	}

	public void setLbmPlatform(String lbmPlatform) {
		this.lbmPlatform = lbmPlatform;
	}

	public Long getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(Long timeLimit) {
		this.timeLimit = timeLimit;
	}

	public String getCrmMsgUrl() {
        return crmMsgUrl;
    }

    public void setCrmMsgUrl(String crmMsgUrl) {
        this.crmMsgUrl = crmMsgUrl;
    }

    public String getDecryptionKey() {
        return decryptionKey;
    }

    public void setDecryptionKey(String decryptionKey) {
        this.decryptionKey = decryptionKey;
    }

    public String getDecryptionAddAppUrl() {
        return decryptionAddAppUrl;
    }

    public void setDecryptionAddAppUrl(String decryptionAddAppUrl) {
        this.decryptionAddAppUrl = decryptionAddAppUrl;
    }

    public String getDecryptionUrl() {
        return decryptionUrl;
    }

    public void setDecryptionUrl(String decryptionUrl) {
        this.decryptionUrl = decryptionUrl;
    }

    public String getTokenGenUrl() {
        return tokenGenUrl;
    }

    public void setTokenGenUrl(String tokenGenUrl) {
        this.tokenGenUrl = tokenGenUrl;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getDecryptionAppId() {
        return decryptionAppId;
    }

    public void setDecryptionAppId(String decryptionAppId) {
        this.decryptionAppId = decryptionAppId;
    }

    public String getDecryptionSecret() {
        return decryptionSecret;
    }

    public void setDecryptionSecret(String decryptionSecret) {
        this.decryptionSecret = decryptionSecret;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getKycUrl() {
        return kycUrl;
    }

    public void setKycUrl(String kycUrl) {
        this.kycUrl = kycUrl;
    }

    public String getKycKey() {
        return kycKey;
    }

    public void setKycKey(String kycKey) {
        this.kycKey = kycKey;
    }

    public String getUnifyAppSecret() {
        return unifyAppSecret;
    }

    public void setUnifyAppSecret(String unifyAppSecret) {
        this.unifyAppSecret = unifyAppSecret;
    }

    public String getUnifyMerchantNo() {
        return unifyMerchantNo;
    }

    public void setUnifyMerchantNo(String unifyMerchantNo) {
        this.unifyMerchantNo = unifyMerchantNo;
    }

    public String getUnifyAppId() {
        return unifyAppId;
    }

    public void setUnifyAppId(String unifyAppId) {
        this.unifyAppId = unifyAppId;
    }

    public String getUnifyUrl() {
        return unifyUrl;
    }

    public void setUnifyUrl(String unifyUrl) {
        this.unifyUrl = unifyUrl;
    }

    public String getBaseBiApiUrl() {
        return baseBiApiUrl;
    }

    public void setBaseBiApiUrl(String baseBiApiUrl) {
        this.baseBiApiUrl = baseBiApiUrl;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

	public String getCsChangeImApiHost() {
		return csChangeImApiHost;
	}

	public void setCsChangeImApiHost(String csChangeImApiHost) {
		this.csChangeImApiHost = csChangeImApiHost;
	}

	public String getSystemCategory() {
        return systemCategory;
    }

    public void setSystemCategory(String systemCategory) {
        this.systemCategory = systemCategory;
    }

    public Map<String, Integer> getAppidTimeZone() {
        return appidTimeZone;
    }

    public void setAppidTimeZone(Map<String, Integer> appidTimeZone) {
        this.appidTimeZone = appidTimeZone;
    }

    public String getBiCenterApiUrl() {
        return biCenterApiUrl;
    }

    public void setBiCenterApiUrl(String biCenterApiUrl) {
        this.biCenterApiUrl = biCenterApiUrl;
    }

    public String getBiCenterQueryApiUrl() {
        return biCenterQueryApiUrl;
    }

    public void setBiCenterQueryApiUrl(String biCenterQueryApiUrl) {
        this.biCenterQueryApiUrl = biCenterQueryApiUrl;
    }

    public String getBiCenterChatUser() {
        return biCenterChatUser;
    }

    public void setBiCenterChatUser(String biCenterChatUser) {
        this.biCenterChatUser = biCenterChatUser;
    }

    public String getBiCenterChatPass() {
        return biCenterChatPass;
    }

    public void setBiCenterChatPass(String biCenterChatPass) {
        this.biCenterChatPass = biCenterChatPass;
    }

    public String getUserManaUrl() {
        return userManaUrl;
    }

    public void setUserManaUrl(String userManaUrl) {
        this.userManaUrl = userManaUrl;
    }
}

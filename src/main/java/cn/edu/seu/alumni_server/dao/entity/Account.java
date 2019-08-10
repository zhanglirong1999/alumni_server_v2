package cn.edu.seu.alumni_server.dao.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

public class Account implements Serializable {
    @Column(name = "account_id")
    @Id
    private Long accountId;

    private String name;

    /**
     * 0男1女
     */
    private Boolean gender;

    private Date birthday;

    @Column(name = "self_desc")
    private String selfDesc;

    private String avatar;

    private String city;

    private String openid;

    private String phone;

    private String wechat;

    private String email;

    private String industry;

    /**
     * 0学生1工作
     */
    private Boolean type;

    /**
     * 0未注册1已注册
     */
    private Boolean registered;

    @Column(name = "c_time", insertable = false)
    private Date cTime;

    /**
     * update
     */
    @Column(name = "u_time", insertable = false)
    private Date uTime;

    /**
     * 1有效0无效
     */
    @Column(name = "valid_status")
    private Boolean validStatus;

    private static final long serialVersionUID = 1L;

    /**
     * @return account_id
     */
    public Long getAccountId() {
        return accountId;
    }

    /**
     * @param accountId
     */
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取0男1女
     *
     * @return gender - 0男1女
     */
    public Boolean getGender() {
        return gender;
    }

    /**
     * 设置0男1女
     *
     * @param gender 0男1女
     */
    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    /**
     * @return birthday
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * @param birthday
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * @return self_desc
     */
    public String getSelfDesc() {
        return selfDesc;
    }

    /**
     * @param selfDesc
     */
    public void setSelfDesc(String selfDesc) {
        this.selfDesc = selfDesc == null ? null : selfDesc.trim();
    }

    /**
     * @return avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * @param avatar
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    /**
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     * @return openid
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * @param openid
     */
    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    /**
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * @return wechat
     */
    public String getWechat() {
        return wechat;
    }

    /**
     * @param wechat
     */
    public void setWechat(String wechat) {
        this.wechat = wechat == null ? null : wechat.trim();
    }

    /**
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * @return industry
     */
    public String getIndustry() {
        return industry;
    }

    /**
     * @param industry
     */
    public void setIndustry(String industry) {
        this.industry = industry == null ? null : industry.trim();
    }

    /**
     * 获取0学生1工作
     *
     * @return type - 0学生1工作
     */
    public Boolean getType() {
        return type;
    }

    /**
     * 设置0学生1工作
     *
     * @param type 0学生1工作
     */
    public void setType(Boolean type) {
        this.type = type;
    }

    /**
     * 获取0未注册1已注册
     *
     * @return registered - 0未注册1已注册
     */
    public Boolean getRegistered() {
        return registered;
    }

    /**
     * 设置0未注册1已注册
     *
     * @param registered 0未注册1已注册
     */
    public void setRegistered(Boolean registered) {
        this.registered = registered;
    }

    /**
     * @return c_time
     */
    public Date getcTime() {
        return cTime;
    }

    /**
     * @param cTime
     */
    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }

    /**
     * 获取update
     *
     * @return u_time - update
     */
    public Date getuTime() {
        return uTime;
    }

    /**
     * 设置update
     *
     * @param uTime update
     */
    public void setuTime(Date uTime) {
        this.uTime = uTime;
    }

    /**
     * 获取1有效0无效
     *
     * @return valid_status - 1有效0无效
     */
    public Boolean getValidStatus() {
        return validStatus;
    }

    /**
     * 设置1有效0无效
     *
     * @param validStatus 1有效0无效
     */
    public void setValidStatus(Boolean validStatus) {
        this.validStatus = validStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", accountId=").append(accountId);
        sb.append(", name=").append(name);
        sb.append(", gender=").append(gender);
        sb.append(", birthday=").append(birthday);
        sb.append(", selfDesc=").append(selfDesc);
        sb.append(", avatar=").append(avatar);
        sb.append(", city=").append(city);
        sb.append(", openid=").append(openid);
        sb.append(", phone=").append(phone);
        sb.append(", wechat=").append(wechat);
        sb.append(", email=").append(email);
        sb.append(", industry=").append(industry);
        sb.append(", type=").append(type);
        sb.append(", registered=").append(registered);
        sb.append(", cTime=").append(cTime);
        sb.append(", uTime=").append(uTime);
        sb.append(", validStatus=").append(validStatus);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
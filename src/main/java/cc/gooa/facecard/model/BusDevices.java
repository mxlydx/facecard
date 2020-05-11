package cc.gooa.facecard.model;

import java.util.Date;

public class BusDevices {
    private Integer id;

    private Integer schoolid;

    private String code;

    private String name;

    private String type;

    private Integer busitype;

    private String address;

    private Integer status;

    private Date createtime;

    private Date statustime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSchoolid() {
        return schoolid;
    }

    public void setSchoolid(Integer schoolid) {
        this.schoolid = schoolid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Integer getBusitype() {
        return busitype;
    }

    public void setBusitype(Integer busitype) {
        this.busitype = busitype;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getStatustime() {
        return statustime;
    }

    public void setStatustime(Date statustime) {
        this.statustime = statustime;
    }
}
package com.example.scavengerhunt;

import java.util.Date;

public class Invitation {
    private String title;
    private String huntid;
    private Date enddate;
    private Date begindate;

    public String toString() {
	return huntid + "," + title;

    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getHuntId() {
	return huntid;
    }

    public void setHuntId(String huntid) {
	this.huntid = huntid;
    }

    public Date getBeginDate() {
	return begindate;
    }

    public Date setBeginDate(Date begindate) {
	return this.begindate = begindate;
    }

    public Date getEndDate() {
	return enddate;
    }

    public Date setEndDate(Date enddate) {
	return this.enddate = enddate;
    }

    // public Invitation(String title, String huntid, Date begindate) {
    // super();
    // this.title = title;
    // this.huntid = huntid;
    // this.begindate = begindate;
    // }
}

// package com.example.scavengerhunt;
//
// import java.util.Date;
//
// public class Invitation {
// private String title;
// private String huntid;
// private Date endDate;
// private Date beginDate;
//
// public String toString() {
// return huntid;
// }
//
// public String getTitle() {
// return title;
// }
//
// public void setTitle(String title) {
// this.title = title;
// }
//
// public String getHuntId() {
// return huntid;
// }
//
// public void setHuntId(String huntid) {
// this.huntid = huntid;
// }
//
// public Date getBeginDate() {
// return beginDate;
// }
//
// public Date setBeginDate(Date beginDate) {
// return this.beginDate = beginDate;
// }
//
// public Date getEndDate() {
// return endDate;
// }
//
// public Date setEndDate(Date endDate) {
// return this.endDate = endDate;
// }
//
// }
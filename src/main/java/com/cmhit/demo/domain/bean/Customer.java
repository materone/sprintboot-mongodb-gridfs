package com.cmhit.demo.domain.bean;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="t_customer")
public class Customer {

public String _id;
    public String CarNumber;
    public String name;
    public int cntFee;
    public String HomeAddress;

    public String getHomeAddress() {
		return HomeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		HomeAddress = homeAddress;
	}

	public int getCntFee() {
		return cntFee;
	}

	public void setCntFee(int cntFee) {
		this.cntFee = cntFee;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCarNumber() {
        return CarNumber;
    }

    public void setCarNumber(String CarNumber) {
        this.CarNumber = CarNumber;
    }
}

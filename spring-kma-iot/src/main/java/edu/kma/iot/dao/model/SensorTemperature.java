package edu.kma.iot.dao.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Entity
@Table(name = "SENSOR_TEMPERATURE",  uniqueConstraints = {@UniqueConstraint(columnNames = "mac_address")})
@JsonPropertyOrder({"mac_address", "temperature_value", "moisture_value", "status_time"})
public class SensorTemperature extends Device implements Serializable {
	private static final long serialVersionUID = 1L;
	@Column(name = "temperature_value")
	private float temperature_value;
	@Column(name = "moisture_value")
	private float humidity_value;
	@Column(name = "status_time")
	private String status_time;
	
	public String getStatus_time() {
		return status_time;
	}
	public void setStatus_time(String status_time) {
		this.status_time = status_time;
	}

	public float getTemperature_value() {
		return temperature_value;
	}

	public void setTemperature_value(float temperature_value) {
		this.temperature_value = temperature_value;
	}

	public void setHumidity_value(float humidity_value) {
		this.humidity_value = humidity_value;
	}
	public float getHumidity_value() {
		return humidity_value;
	}
	

}
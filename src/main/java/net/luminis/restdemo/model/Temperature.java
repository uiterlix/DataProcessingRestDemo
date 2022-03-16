package net.luminis.restdemo.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import net.luminis.restdemo.LocalDateTimeAdapter;

import java.time.LocalDateTime;

@XmlRootElement(name="reading", namespace = "http://net.luminis/temperature")
public class Temperature {
    private String id;
    private String sensorId;
    private LocalDateTime timestamp;
    private float temperature;

    @XmlElement(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @XmlElement(name = "sensor-id")
    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "Temperature{" +
                "sensorId='" + sensorId + '\'' +
                ", timestamp=" + timestamp +
                ", temperature=" + temperature +
                '}';
    }
}

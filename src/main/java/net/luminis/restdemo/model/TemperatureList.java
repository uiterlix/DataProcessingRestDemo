package net.luminis.restdemo.model;

import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="readings")
public class TemperatureList {

    private List<Temperature> temperatures;

    public TemperatureList(List<Temperature> temperatures) {
        this.temperatures = temperatures;
    }

    public TemperatureList() {
        this.temperatures = new ArrayList<>();
    }

    @XmlElement(name="reading")
    public List<Temperature> getTemperatures() {
        return temperatures;
    }

    public void setTemperatures(List<Temperature> temperatures) {
        this.temperatures = temperatures;
    }
}

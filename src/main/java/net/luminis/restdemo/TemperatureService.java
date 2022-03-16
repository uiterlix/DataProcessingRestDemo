package net.luminis.restdemo;

import net.luminis.restdemo.model.Temperature;

import java.util.*;

public class TemperatureService {

    private final Map<String, Temperature> temperatures = new LinkedHashMap<>();

    public String storeTemperature(Temperature temperature) {
        String id = UUID.randomUUID().toString();
        temperature.setId(id);
        temperatures.put(id, temperature);
        return id;
    }

    public Temperature getTemperature(String id) throws EntityNotFoundException {
        Temperature temperature = temperatures.get(id);
        if (temperature == null) {
            throw new EntityNotFoundException(id);
        }
        return temperature;
    }

    public void updateTemperature(String id, Temperature temperature) throws EntityNotFoundException {
        Temperature existingTemperature = temperatures.get(id);
        temperature.setId(id);
        if (existingTemperature == null) {
            throw new EntityNotFoundException(id);
        }
        temperatures.put(id, temperature);
    }

    public void deleteTemperature(String id) throws EntityNotFoundException {
        Temperature existingTemperature = temperatures.get(id);
        if (existingTemperature == null) {
            throw new EntityNotFoundException(id);
        }
        temperatures.remove(id);
    }

    public List<Temperature> getTemperatures() {
        return new ArrayList<>(temperatures.values());
    }

}

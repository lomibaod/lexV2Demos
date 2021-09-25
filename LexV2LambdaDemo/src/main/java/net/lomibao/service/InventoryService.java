package net.lomibao.service;

import io.micronaut.context.annotation.Bean;
import net.lomibao.model.Vehicle;
import net.lomibao.util.ResourceUtil;

import java.util.List;
import java.util.stream.Collectors;

@Bean // equivalent to spring @Service
public class InventoryService {

    /**
     * mock service to demonstrate pulling data. loads from json but this could easily be replaced with a
     * database connection or external api call
     *
     * @param make
     * @param type
     * @return
     */
    public List<Vehicle> queryByMakeAndType(String make, String type) {
        List<Vehicle> allCars = ResourceUtil.loadFromResourceToList("inventory.json", Vehicle.class);
        return allCars.stream().filter(vehicle -> vehicle.getMake().equalsIgnoreCase(make) && vehicle.getType().equalsIgnoreCase(type)).collect(Collectors.toList());
    }

}

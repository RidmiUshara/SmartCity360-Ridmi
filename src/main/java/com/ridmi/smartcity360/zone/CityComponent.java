
package com.ridmi.smartcity360.zone;

public interface CityComponent {

    String getName();

    void setPower(boolean on);

    int getPowerUsage();

    int getActiveBuildings();

}

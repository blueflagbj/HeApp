package com.wade.mobile.common.map;

import android.os.Bundle;
import com.ailk.common.data.IData;
import com.ailk.common.data.impl.DataMap;
import com.amap.api.location.AMapLocation;
import com.wade.mobile.common.map.util.AMapUtil;
import com.wade.mobile.common.map.util.MapConstant;

public class AMapLocationInfo {
    private AMapLocation location;
    private String toString;

    public AMapLocationInfo(AMapLocation location2) {
        this.location = location2;
    }

    public String toString() {
        if (this.toString == null) {
            IData data = new DataMap();
            data.put(MapConstant.KEY_PROVINCE, this.location.getProvince());
            data.put(MapConstant.KEY_CITY, this.location.getCity());
            data.put(MapConstant.KEY_DISTRICT, this.location.getDistrict());
            data.put(MapConstant.KEY_ADCODE, this.location.getAdCode());
            data.put("Time", AMapUtil.convertToTime(this.location.getTime()));
            Bundle locBundle = this.location.getExtras();
            if (locBundle != null) {
                data.put(MapConstant.KEY_CITY_CODE, locBundle.getString("citycode"));
                data.put(MapConstant.KEY_LOCATION_DESC, locBundle.getString("desc"));
            }
            data.put(MapConstant.KEY_LNG, Double.valueOf(this.location.getLongitude()));
            data.put(MapConstant.KEY_LAT, Double.valueOf(this.location.getLatitude()));
            data.put(MapConstant.KEY_ACCURACY, Float.valueOf(this.location.getAccuracy()));
            data.put(MapConstant.KEY_PROVIDER, this.location.getProvider());
            data.put(MapConstant.KEY_ALTITUDE, Double.valueOf(this.location.getAltitude()));
            this.toString = data.toString();
        }
        return this.toString;
    }
}
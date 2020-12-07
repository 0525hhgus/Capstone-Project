package org.techtown.gwangjubus;

public class StationList {

    String busstopId;
    String busstopName;

    public StationList(String busstopId, String busstopName) {
        this.busstopId = busstopId;
        this.busstopName = busstopName;
    }

    public String getBusstopId() {
        return busstopId;
    }

    public void setBusstopId(String busstopId) {
        this.busstopId = busstopId;
    }

    public String getBusstopName() {
        return busstopName;
    }

    public void setBusstopName(String busstopName) {
        this.busstopName = busstopName;
    }
}

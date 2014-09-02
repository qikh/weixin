package weixin.message;

import java.math.BigDecimal;

public class LocationMessage extends Message {
    private BigDecimal location_x;
    private BigDecimal location_y;
    private Integer scale;
    private String label;

    public BigDecimal getLocation_x() {
        return location_x;
    }

    public void setLocation_x(BigDecimal location_x) {
        this.location_x = location_x;
    }

    public BigDecimal getLocation_y() {
        return location_y;
    }

    public void setLocation_y(BigDecimal location_y) {
        this.location_y = location_y;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}

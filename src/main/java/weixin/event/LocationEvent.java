package weixin.event;

import java.math.BigDecimal;

public class LocationEvent extends SubscribeEvent {
	private BigDecimal latitude;
	private BigDecimal longitude;
	private BigDecimal precision;

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public BigDecimal getPrecision() {
		return precision;
	}

	public void setPrecision(BigDecimal precision) {
		this.precision = precision;
	}

}

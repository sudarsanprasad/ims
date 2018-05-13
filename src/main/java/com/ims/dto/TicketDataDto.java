package com.ims.dto;

import lombok.Data;

@Data
public class TicketDataDto {

	private String col1;
	
	private String col2;
	
	private String col3;
	
	private String col4;
	
	private String col5;
	
	private String col6;
	
	private String col7;
	
	private String col8;
	
	private String col9;
	
	private String col10;
	
	private String col11;
	
	private String col12;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TicketDataDto other = (TicketDataDto) obj;
		if (col1 == null) {
			if (other.col1 != null)
				return false;
		} else if (!col1.equals(other.col1))
			return false;
		if (col10 == null) {
			if (other.col10 != null)
				return false;
		} else if (!col10.equals(other.col10))
			return false;
		if (col2 == null) {
			if (other.col2 != null)
				return false;
		} else if (!col2.equals(other.col2))
			return false;
		if (col3 == null) {
			if (other.col3 != null)
				return false;
		} else if (!col3.equals(other.col3))
			return false;
		if (col4 == null) {
			if (other.col4 != null)
				return false;
		} else if (!col4.equals(other.col4))
			return false;
		if (col5 == null) {
			if (other.col5 != null)
				return false;
		} else if (!col5.equals(other.col5))
			return false;
		if (col6 == null) {
			if (other.col6 != null)
				return false;
		} else if (!col6.equals(other.col6))
			return false;
		if (col7 == null) {
			if (other.col7 != null)
				return false;
		} else if (!col7.equals(other.col7))
			return false;
		if (col8 == null) {
			if (other.col8 != null)
				return false;
		} else if (!col8.equals(other.col8))
			return false;
		if (col9 == null) {
			if (other.col9 != null)
				return false;
		} else if (!col9.equals(other.col9))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((col1 == null) ? 0 : col1.hashCode());
		result = prime * result + ((col10 == null) ? 0 : col10.hashCode());
		result = prime * result + ((col2 == null) ? 0 : col2.hashCode());
		result = prime * result + ((col3 == null) ? 0 : col3.hashCode());
		result = prime * result + ((col4 == null) ? 0 : col4.hashCode());
		result = prime * result + ((col5 == null) ? 0 : col5.hashCode());
		result = prime * result + ((col6 == null) ? 0 : col6.hashCode());
		result = prime * result + ((col7 == null) ? 0 : col7.hashCode());
		result = prime * result + ((col8 == null) ? 0 : col8.hashCode());
		result = prime * result + ((col9 == null) ? 0 : col9.hashCode());
		return result;
	}

	

}

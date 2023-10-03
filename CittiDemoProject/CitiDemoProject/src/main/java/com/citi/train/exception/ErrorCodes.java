package com.citi.train.exception;

import org.springframework.stereotype.Component;

@Component
public final class ErrorCodes {


	public static final String NO_UPCOMING_TICKETS = "No upcoming tickets##404";
	public static final String NO_BOOKING_FOUND = "No booking found##404";
	public static final String NO_TICKET_PNR = "No ticket found for PNR no##404";
	public static final String	CONNECTION_ISSUE ="Server Connection Isusue ##500";
	public static final String NO_CANCELED_TICKET ="No Canceled Ticket found##404";
	public static final String NO_TRAIN_FOUND=" No train found";


	private ErrorCodes() {
	}
}

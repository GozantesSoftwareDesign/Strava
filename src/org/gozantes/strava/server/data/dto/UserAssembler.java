package org.gozantes.strava.server.data.dto;

import org.gozantes.strava.server.data.domain.auth.User;
import org.gozantes.strava.server.data.domain.auth.UserCredentials;

public class UserAssembler {
	private static UserAssembler instance;
	
	private UserAssembler() { }
	
	public static UserAssembler getInstance() {
		if (instance == null) {
			instance = new UserAssembler();
		}
		return instance;
	}
	public UserDTO UserToDTO(User user) {
		UserCredentials userC = new UserCredentials(user.getCredentials().type(), user.getCredentials().id(), null);
		
		UserDTO userdto = new UserDTO(userC, user.getData());
		return userdto;
	}
}

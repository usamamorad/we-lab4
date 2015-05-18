package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import play.data.validation.Constraints;
import play.data.validation.ValidationError;
import play.i18n.Messages;

@Entity
public class JeopardyUser extends BaseEntity {

	public enum Gender {
		male, female
	}
  
  //@TODO - as soon as the id attribute has been introduced to the base entity - remove the ID
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

	@Constraints.Required
	@Constraints.MinLength(value = 4, message = "error.userName")
	@Constraints.MaxLength(value = 8, message = "error.userName")
	private String userName;

	@Constraints.Required
	@Constraints.MinLength(value = 4, message = "error.password")
	@Constraints.MaxLength(value = 8, message = "error.password")
	private String password;

	private String firstName;
	private String lastName;
	private Date birthDate;
	private Gender gender;
	
	private Avatar avatar;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return userName;
	}

	public void setName(String name) {
		this.userName = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}
	
	public Avatar getAvatar() {
		return avatar;
	}
	
	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
	}

	public List<ValidationError> validate() {
		List<ValidationError> errors = new ArrayList<ValidationError>();
		if (userNameIsTaken())
			errors.add(new ValidationError("userName", "error.userexists"));
		if(getBirthDate() != null && getBirthDate().after(new Date()))
			errors.add(new ValidationError("birthdate", Messages.get("error.futuredate"))); 
		return errors.isEmpty() ? null : errors;
	}

	private boolean userNameIsTaken() {
		return JeopardyDAO.INSTANCE.findByUserName(userName) != null;
	}

	public boolean authenticate() {
		JeopardyUser user = JeopardyDAO.INSTANCE.findByUserName(userName);
		return user != null && user.authenticate(password);
	}

	public boolean authenticate(String password) {
		// very simplistic authentication, in a production scenario the password should be encrypted
		return this.getPassword().equals(password);
	}
	
	@Override
	public String toString() {
		return getUserName();
	}

}
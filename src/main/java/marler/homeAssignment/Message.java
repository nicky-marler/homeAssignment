  
package marler.homeAssignment;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;


@Data
@Entity
class Message {

    
	private @Id @GeneratedValue Long id;
	private String username;
    private String text;
    private int timeout = 60; //default value
    private LocalDateTime expiration_date;

    
	Message() {}

    void setExpirationDate(){
        this.expiration_date = LocalDateTime.now().plusSeconds(this.timeout);
    }

    void setExpirationDateNow(){
        this.expiration_date = LocalDateTime.now();
    }

    //Construcotrs to use if starting with loaded data
	Message(final String username, final String text) {
        this.username = username;
        this.text = text;
        this.expiration_date = LocalDateTime.now().plusSeconds(timeout);
    }

    // With specified timeout
    Message(final String username, final String text, final int timeout) {
        this.username = username;
        this.text = text;
        this.timeout = timeout;
        this.expiration_date = LocalDateTime.now().plusSeconds(timeout);
    }

    private String getDateTime() {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final Date date = new Date();
        return dateFormat.format(date);
        
    }

	// public Object getId() {
	// 	return null;
	// }
}
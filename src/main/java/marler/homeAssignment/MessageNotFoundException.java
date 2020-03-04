package marler.homeAssignment;

class MessageNotFoundException extends RuntimeException {

	MessageNotFoundException(Long id) {
		super("Could not find chat " + id);
	}
}
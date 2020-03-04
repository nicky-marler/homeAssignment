package marler.homeAssignment;

class MessageNotFoundException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	MessageNotFoundException(Long id) {
		super("Could not find chat " + id);
	}
}
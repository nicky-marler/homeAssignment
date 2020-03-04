package marler.homeAssignment;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
class MessageController {

    private final MessageRepository repository;
    private final MessageModelAssembler assembler;

	MessageController(MessageRepository repository, MessageModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
	}


	//Get by query the username. Assuming there will never be a blank username. Expires all read messages
	//Ex.
	//curl -v localhost:8080/chats?username=nmarler
	@GetMapping("/chats")
	CollectionModel<EntityModel<Message>> getByUserName(@RequestParam Optional<String> username) {

		//Assuming there will never be a blank username
		List<EntityModel<Message>> messages = repository.findByUsername(username.orElse("")).stream()
		.filter(m -> m.getExpiration_date().compareTo(LocalDateTime.now()) > 0 ) //Greater than 0 is greater than local now
		.map(message -> {
			message.setExpirationDateNow();
			return  assembler.toModel(repository.save(message));
		})
		.collect(Collectors.toList());

		return new CollectionModel<>(messages,
			linkTo(methodOn(MessageController.class).all()).withSelfRel());
		
	}

	
	//Get by exact id. 
	//Ex: curl -v localhost:8080/chats/1
	@GetMapping("/chats/{id}")
	EntityModel<Message> getById(@PathVariable Long id) {
		Message message = repository.findById(id)
			.orElseThrow(() -> new MessageNotFoundException(id));

			return assembler.toModel(message);
		
	}

	//Set message with optional timeout
	//Ex: curl -v -X POST localhost:8080/chats -H 'Content-Type:application/json' -d '{"username": "nmarler", "text": "Hey"}'
	//or: curl -v -X POST localhost:8080/chats -H 'Content-Type:application/json' -d '{"username": "nmarler", "text": "Hey", "timeout": 100}'

    @PostMapping("/chats")
    ResponseEntity<?> newMessage(@RequestBody Message newMessage) throws URISyntaxException {

        newMessage.setExpirationDate();
        EntityModel<Message> entityModel = assembler.toModel(repository.save(newMessage));

        return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);
	}
	

	//Private method for link building
	EntityModel<Message> one(@PathVariable Long id) {

		Message message = repository.findById(id)
			.orElseThrow(() -> new MessageNotFoundException(id));
		
		return assembler.toModel(message);
	}

	//Private method for link building
	CollectionModel<EntityModel<Message>> all() {

		List<EntityModel<Message>> messages = repository.findAll().stream()
			.map(assembler::toModel)
			.collect(Collectors.toList());
		
		return new CollectionModel<>(messages,
			linkTo(methodOn(MessageController.class).all()).withSelfRel());
	}

}
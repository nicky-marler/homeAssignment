package marler.homeAssignment;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
class MessageModelAssembler implements RepresentationModelAssembler<Message, EntityModel<Message>> {

	@Override
	public EntityModel<Message> toModel(Message message) {

		return new EntityModel<>(message,
			linkTo(methodOn(MessageController.class).one(message.getId())).withSelfRel(),
			linkTo(methodOn(MessageController.class).all()).withRel("Messages")
			);
	}
}
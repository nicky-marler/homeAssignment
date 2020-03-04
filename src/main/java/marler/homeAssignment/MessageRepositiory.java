package marler.homeAssignment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByUsername(String username);
}


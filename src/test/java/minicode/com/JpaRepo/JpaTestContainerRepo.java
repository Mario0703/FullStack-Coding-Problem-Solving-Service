package minicode.com.JpaRepo;

import minicode.com.entity.Container.Container;
import minicode.com.repository.ContainerRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class JpaTestContainerRepo {

    @Autowired
    private ContainerRepo containerRepo;

    @Test
    public void findByIdReturnsContainerWhenSaved() {
        Container container = new Container();
        container.setLanguage("python");
        container.setImageName("minileetcode-python:latest");
        container.setContainerMetadata("{\"base\":\"python:3.12-slim\"}");
        containerRepo.save(container);

        Optional<Container> foundContainer = containerRepo.findById("python");

        assertThat(foundContainer.isPresent()).isTrue();
        assertThat(foundContainer.get().getLanguage()).isEqualTo("python");
        assertThat(foundContainer.get().getImageName()).isEqualTo("minileetcode-python:latest");
    }

}

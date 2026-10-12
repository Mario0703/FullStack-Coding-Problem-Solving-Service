package minicode.com.EntityTest;

import jakarta.persistence.PersistenceException;
import minicode.com.entity.Container.Container;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class ContainerEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void savingContainerWithNullImageNameThrows() {
        // Given a container whose required image name is null
        Container container = new Container();
        container.setLanguage("python");
        container.setImageName(null);
        container.setContainerMetadata("Python 3.12 runtime");

        // When it is saved and flushed to the database
        // Then the null required field is rejected
        assertThrows(PersistenceException.class, () -> entityManager.persistAndFlush(container));
    }

    @Test
    public void savingContainerWithNullContainerMetadataSucceeds() {
        // Given a valid container whose nullable metadata is null
        Container container = new Container();
        container.setLanguage("python");
        container.setImageName("minileetcode-python:latest");
        container.setContainerMetadata(null);

        // When it is saved and flushed to the database
        // Then the null nullable field is accepted
        assertDoesNotThrow(() -> {
            entityManager.persistAndFlush(container);
        });
    }

}

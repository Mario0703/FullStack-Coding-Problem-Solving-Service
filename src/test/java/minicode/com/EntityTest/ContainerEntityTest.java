package minicode.com.EntityTest;

import jakarta.persistence.PersistenceException;
import minicode.com.entity.Container.Container;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

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

}

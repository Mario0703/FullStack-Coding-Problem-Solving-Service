package minicode.com.EntityTest;

import minicode.com.entity.Container.Container;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class ContainerEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testContainerCreationForAValidContainer() {
        // Given a container with a valid language, image name and metadata
        String language = "python";
        String imageName = "minileetcode/python-runner:latest";
        String containerMetadata = "Python 3.12 runtime";
        Container container = new Container(language, imageName, containerMetadata);

        // And it is saved and flushed to the database
        Container saved = entityManager.persistAndFlush(container);
        entityManager.clear(); // read the row back from the database, not the cache

        // Then it can be read back by its assigned primary key with the same properties
        Container retrievedContainer = entityManager.find(Container.class, saved.getLanguage());
        assertThat(retrievedContainer).isNotNull();
        assertThat(retrievedContainer.getLanguage()).isEqualTo(language);
        assertThat(retrievedContainer.getImageName()).isEqualTo(imageName);
        assertThat(retrievedContainer.getContainerMetadata()).isEqualTo(containerMetadata);
    }

}

package minicode.com.entity.Container;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "containers", schema = "public")
@Getter
@Setter
public class Container {

    @Id
    @Column(nullable = false, length = 20)
    private String language;

    @Column(nullable = false)
    private String imageName;

    @Column(columnDefinition = "TEXT")
    private String containerMetadata;

    protected Container() {
    }

    public Container(String language, String imageName, String containerMetadata) {
        this.language = language;
        this.imageName = imageName;
        this.containerMetadata = containerMetadata;
    }
}

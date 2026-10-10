package minicode.com.entity.Container;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "containers", schema = "public")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Container {

    @Id
    @Column(nullable = false, length = 20)
    private String language;

    @Column(nullable = false)
    private String imageName;

    @Column(columnDefinition = "TEXT")
    private String containerMetadata;
}

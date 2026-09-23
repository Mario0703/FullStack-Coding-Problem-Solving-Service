package minicode.com.entity;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Challange {
    @Column(nullable = false, unique = true)
    private String Name;
    @Column(nullable = false)
    private String Description;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String Id;
    @Column(nullable = false)
    private Difficulty difficulty;
    @Column(nullable = false)
    private Catagory catagory;


}

package minicode.com.controller;


import minicode.com.entity.Challange;
import minicode.com.repository.ChallangeRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/challange")
public class ChallangeController {

    private final ChallangeRepo ChallangeRepo;

    public ChallangeController(ChallangeRepo challengeRepository) {
        this.ChallangeRepo = challengeRepository;
    }

    @GetMapping("/ListChallanges")
    public List<Challange> listChallanges() {
        return ChallangeRepo.findAll();
    }


}


package io.github.harsh;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EvaluatorController {

    private final EvaluationService evaluationService;

    public EvaluatorController(EvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @GetMapping("/test")
    public String test() {
        return "Backend is running";
    }

    @PostMapping("/evaluate")
    public List<TruthTableRow> evaluate(
            @RequestBody EvaluationRequest request) {

        return evaluationService.evaluate(request.getExpression());
    }
}

// test for api to check if the backend is responding or not 
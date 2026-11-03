
package io.github.harsh;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class EvaluatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Test 1: AND operator
    @Test
    void shouldEvaluateAndCorrectly() throws Exception {
        assertTruthTable("A AND B", 4,
                false, false, false, true);
    }

    // Test 2: OR operator
    @Test
    void shouldEvaluateOrCorrectly() throws Exception {
        assertTruthTable("A OR B", 4,
                false, true, true, true);
    }

    // Test 3: NOT operator
    @Test
    void shouldEvaluateNotCorrectly() throws Exception {
        assertTruthTable("NOT A", 2,
                false, true);
    }

    // Test 4: XOR operator
    @Test
    void shouldEvaluateXorCorrectly() throws Exception {
        assertTruthTable("A XOR B", 4,
                false, true, true, false);
    }

    // Test 5: NAND operator
    @Test
    void shouldEvaluateNandCorrectly() throws Exception {
        assertTruthTable("A NAND B", 4,
                true, true, true, false);
    }

    // Test 6: XNOR operator
    @Test
    void shouldEvaluateXnorCorrectly() throws Exception {
        assertTruthTable("A XNOR B", 4,
                true, false, false, true);
    }

    // Test 7: Operator precedence
    // AND should be evaluated before OR
    @Test
    void shouldRespectOperatorPrecedence() throws Exception {
        assertTruthTable("A OR B AND C", 8,
                true, true, true, true, true, false, false, false);
    }

    // Test 8: Parentheses should override normal precedence
    @Test
    void shouldRespectParentheses() throws Exception {
        assertTruthTable("(A OR B) AND C", 8,
                true, true, true, false, false, false, false, false);
    }

    // Test 9: NOT should be evaluated before AND
    @Test
    void shouldRespectNotPrecedence() throws Exception {
        assertTruthTable("NOT A AND B", 4,
                false, false, true, false);
    }

    // Test 10: Invalid Boolean expression
    @Test
    void shouldReturnBadRequestForInvalidExpression() throws Exception {

        mockMvc.perform(post("/api/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "expression": "A AND OR B"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Invalid Boolean expression: Invalid input"));
    }

    // Test 11: Empty Boolean expression
    @Test
    void shouldReturnBadRequestForEmptyExpression() throws Exception {

        mockMvc.perform(post("/api/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "expression": ""
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value("Expression cannot be empty."));
    }

    // Common helper method for successful truth-table tests
    private void assertTruthTable(
            String expression,
            int expectedRows,
            Boolean... expectedResults) throws Exception {

        mockMvc.perform(post("/api/evaluate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "expression": "%s"
                        }
                        """.formatted(expression)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(expectedRows))
                .andExpect(jsonPath("$[*].result")
                        .value(containsInAnyOrder(expectedResults)));
    }
}

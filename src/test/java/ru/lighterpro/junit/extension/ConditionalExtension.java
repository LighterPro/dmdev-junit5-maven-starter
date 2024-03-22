package ru.lighterpro.junit.extension;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

public class ConditionalExtension implements ExecutionCondition {

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        String skip = System.getProperty("skip");
        return "TRUE".equalsIgnoreCase(skip)
                ? ConditionEvaluationResult.disabled("test is skipped")
                : ConditionEvaluationResult.enabled("enabled by default");
    }

}

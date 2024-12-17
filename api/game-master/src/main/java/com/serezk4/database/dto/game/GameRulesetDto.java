package com.serezk4.database.dto.game;

import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.serezk4.database.model.game.GameRuleset}
 */
@Value
public class GameRulesetDto implements Serializable {
    Integer maxDurationMinutes;
    Integer maxRounds;
    Integer timePerMoveSeconds;
    Double objectMass;
    Double gravity;
    Double frictionCoefficient;
    Double airResistance;
    Double maxObjectSpeed;
    Double elasticity;
    String initialConditions;
    String additionalRules;
}

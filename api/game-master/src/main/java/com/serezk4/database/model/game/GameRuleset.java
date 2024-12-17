package com.serezk4.database.model.game;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Represents the ruleset and physical parameters of a game.
 * <p>
 * Contains simulation parameters for physics-based games.
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class GameRuleset {

    /**
     * Максимальная продолжительность игры в минутах.
     */
    Integer maxDurationMinutes;

    /**
     * Максимальное количество раундов.
     */
    Integer maxRounds;

    /**
     * Время на один ход в секундах.
     */
    Integer timePerMoveSeconds;

    // === Физические параметры симуляции ===

    /**
     * Масса объектов в килограммах (kg).
     */
    Double objectMass;

    /**
     * Коэффициент гравитации (м/с²).
     */
    Double gravity;

    /**
     * Коэффициент трения (безразмерная величина).
     */
    Double frictionCoefficient;

    /**
     * Воздушное сопротивление (коэффициент сопротивления среды).
     */
    Double airResistance;

    /**
     * Максимальная скорость объектов в симуляции (м/с).
     */
    Double maxObjectSpeed;

    /**
     * Коэффициент упругости (для столкновений объектов).
     */
    Double elasticity;

    /**
     * Параметры начальных условий симуляции (например, позиции, скорости).
     */
    String initialConditions;

    /**
     * Дополнительные правила симуляции или конфигурации в JSON-формате.
     */
    String additionalRules;
}

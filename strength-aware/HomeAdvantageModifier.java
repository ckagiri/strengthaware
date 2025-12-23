package com.ligitabl.scoring;

/**
 * Applies home field advantage modifiers to team strengths.
 * Based on statistical analysis showing home teams typically have advantages in attack and defense.
 */
public class HomeAdvantageModifier {
    private final double homeAttackBoost;
    private final double homeDefenseBoost;
    private final double awayAttackPenalty;
    private final double awayDefensePenalty;
    
    /**
     * Creates a modifier with default home advantage values.
     * Default values based on Premier League statistics:
     * - Home teams score ~15% more goals
     * - Home teams have ~10% better defense
     * - Away teams score ~5% fewer goals
     * - Away teams have ~10% worse defense
     */
    public HomeAdvantageModifier() {
        this(1.15, 1.10, 0.95, 0.90);
    }
    
    /**
     * Creates a modifier with custom home advantage values.
     * 
     * @param homeAttackBoost Multiplier for home team attack (e.g., 1.15 = +15%)
     * @param homeDefenseBoost Multiplier for home team defense (e.g., 1.10 = +10%)
     * @param awayAttackPenalty Multiplier for away team attack (e.g., 0.95 = -5%)
     * @param awayDefensePenalty Multiplier for away team defense (e.g., 0.90 = -10%)
     */
    public HomeAdvantageModifier(
            double homeAttackBoost,
            double homeDefenseBoost,
            double awayAttackPenalty,
            double awayDefensePenalty) {
        this.homeAttackBoost = homeAttackBoost;
        this.homeDefenseBoost = homeDefenseBoost;
        this.awayAttackPenalty = awayAttackPenalty;
        this.awayDefensePenalty = awayDefensePenalty;
    }
    
    /**
     * Creates a modifier with no home advantage (neutral venue).
     */
    public static HomeAdvantageModifier neutralVenue() {
        return new HomeAdvantageModifier(1.0, 1.0, 1.0, 1.0);
    }
    
    /**
     * Applies home advantage to attacking strength.
     * 
     * @param strength Base attacking strength
     * @param isHome True if team is playing at home
     * @return Modified attacking strength
     */
    public double applyToAttack(double strength, boolean isHome) {
        return strength * (isHome ? homeAttackBoost : awayAttackPenalty);
    }
    
    /**
     * Applies home advantage to defensive strength.
     * 
     * @param strength Base defensive strength
     * @param isHome True if team is playing at home
     * @return Modified defensive strength
     */
    public double applyToDefense(double strength, boolean isHome) {
        return strength * (isHome ? homeDefenseBoost : awayDefensePenalty);
    }
    
    public double getHomeAttackBoost() {
        return homeAttackBoost;
    }
    
    public double getHomeDefenseBoost() {
        return homeDefenseBoost;
    }
    
    public double getAwayAttackPenalty() {
        return awayAttackPenalty;
    }
    
    public double getAwayDefensePenalty() {
        return awayDefensePenalty;
    }
    
    @Override
    public String toString() {
        return String.format("HomeAdvantage[attack: +%.0f%%, defense: +%.0f%%, away attack: %.0f%%, away defense: %.0f%%]",
            (homeAttackBoost - 1.0) * 100,
            (homeDefenseBoost - 1.0) * 100,
            (awayAttackPenalty - 1.0) * 100,
            (awayDefensePenalty - 1.0) * 100);
    }
}

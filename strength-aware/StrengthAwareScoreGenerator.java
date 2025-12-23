package com.ligitabl.scoring;

import java.util.Random;

/**
 * Generates realistic soccer scores based on team strengths and home advantage.
 * 
 * Uses Poisson distribution to model goal scoring, which matches real-world soccer statistics.
 * Takes into account:
 * - Team attacking strength (higher rating = more goals)
 * - Team defensive strength (higher rating = fewer goals conceded)
 * - Home field advantage (home teams score more, away teams score less)
 * 
 * Algorithm:
 * 1. Calculate expected goals for each team based on strength
 * 2. Apply home advantage modifiers
 * 3. Apply defensive strength to reduce opponent's expected goals
 * 4. Sample actual goals from Poisson distribution
 * 5. Cap at maximum goals (6)
 */
public class StrengthAwareScoreGenerator {
    
    /**
     * Base expected goals per team in an average match.
     * Tuned to produce realistic Premier League averages (~2.7 goals per game).
     */
    private static final double BASE_EXPECTED_GOALS = 1.75;
    
    /**
     * Maximum goals a team can score in a match.
     * Prevents unrealistic scorelines like 10-0.
     */
    private static final int MAX_GOALS = 6;
    
    private final HomeAdvantageModifier homeAdvantage;
    private final Random random;
    
    /**
     * Creates a score generator with default home advantage and random seed.
     */
    public StrengthAwareScoreGenerator() {
        this(new HomeAdvantageModifier(), new Random());
    }
    
    /**
     * Creates a score generator with a specific random seed for reproducibility.
     * Useful for testing and debugging.
     * 
     * @param seed Random seed
     */
    public StrengthAwareScoreGenerator(long seed) {
        this(new HomeAdvantageModifier(), new Random(seed));
    }
    
    /**
     * Creates a score generator with custom home advantage and random source.
     * 
     * @param homeAdvantage Home advantage modifier
     * @param random Random number generator
     */
    public StrengthAwareScoreGenerator(HomeAdvantageModifier homeAdvantage, Random random) {
        this.homeAdvantage = homeAdvantage;
        this.random = random;
    }
    
    /**
     * Generates a realistic match score based on team strengths.
     * 
     * @param home Home team profile
     * @param away Away team profile
     * @return Generated score
     */
    public Score generateScore(TeamProfile home, TeamProfile away) {
        // Calculate expected goals for each team
        double homeExpected = calculateExpectedGoals(home, away, true);
        double awayExpected = calculateExpectedGoals(away, home, false);
        
        // Sample actual goals from Poisson distribution
        int homeGoals = generateGoalsFromExpectation(homeExpected);
        int awayGoals = generateGoalsFromExpectation(awayExpected);
        
        return new Score(homeGoals, awayGoals);
    }
    
    /**
     * Calculates expected goals for the attacking team against the defending team.
     * 
     * Formula:
     * expected = BASE_EXPECTED_GOALS 
     *          × attacking_strength (with home/away modifier)
     *          × (1 - defending_strength)
     * 
     * @param attacking The attacking team
     * @param defending The defending team
     * @param isHome True if attacking team is at home
     * @return Expected number of goals
     */
    private double calculateExpectedGoals(
            TeamProfile attacking,
            TeamProfile defending,
            boolean isHome) {
        
        // Get base strengths from team profiles
        double attackStrength = attacking.getAttackingStrength();
        double defenseStrength = defending.getDefensiveStrength();
        
        // Apply home advantage to attack
        attackStrength = homeAdvantage.applyToAttack(attackStrength, isHome);
        
        // Apply home advantage to defense (defending team is opposite of attacking team)
        defenseStrength = homeAdvantage.applyToDefense(defenseStrength, !isHome);
        
        // Calculate expected goals
        double expected = BASE_EXPECTED_GOALS 
                        * attackStrength 
                        * (1.0 - defenseStrength);
        
        // Ensure minimum expected value
        return Math.max(0.1, expected);
    }
    
    /**
     * Generates actual goal count from expected goals using Poisson distribution.
     * Caps at MAX_GOALS to prevent unrealistic scorelines.
     * 
     * @param lambda Expected number of goals (Poisson parameter)
     * @return Actual goal count
     */
    private int generateGoalsFromExpectation(double lambda) {
        int goals = samplePoisson(lambda);
        return Math.min(goals, MAX_GOALS);
    }
    
    /**
     * Samples from a Poisson distribution using Knuth's algorithm.
     * 
     * The Poisson distribution is ideal for modeling rare events (like goals in soccer)
     * that occur independently over time.
     * 
     * @param lambda Expected value (mean) of the distribution
     * @return Random value from Poisson(lambda)
     */
    private int samplePoisson(double lambda) {
        if (lambda <= 0) {
            return 0;
        }
        
        // Knuth's algorithm for Poisson sampling
        double L = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;
        
        do {
            k++;
            p *= random.nextDouble();
        } while (p > L);
        
        return k - 1;
    }
    
    /**
     * Gets the home advantage modifier used by this generator.
     */
    public HomeAdvantageModifier getHomeAdvantage() {
        return homeAdvantage;
    }
    
    /**
     * Gets expected goals for a matchup (useful for analysis/testing).
     * 
     * @param home Home team
     * @param away Away team
     * @return Array with [homeExpected, awayExpected]
     */
    public double[] getExpectedGoals(TeamProfile home, TeamProfile away) {
        double homeExpected = calculateExpectedGoals(home, away, true);
        double awayExpected = calculateExpectedGoals(away, home, false);
        return new double[] { homeExpected, awayExpected };
    }
}

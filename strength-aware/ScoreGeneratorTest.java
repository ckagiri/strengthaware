package com.ligitabl.scoring;

/**
 * Validation and testing for the strength-aware score generator.
 * Runs statistical tests to ensure the generator produces realistic results.
 */
public class ScoreGeneratorTest {
    
    /**
     * Runs all validation tests.
     */
    public static void main(String[] args) {
        System.out.println("=".repeat(80));
        System.out.println("STRENGTH-AWARE SCORE GENERATOR - VALIDATION TESTS");
        System.out.println("=".repeat(80));
        System.out.println();
        
        testBasicFunctionality();
        testScoreDistribution();
        testHomeAdvantage();
        testStrengthDifferentials();
        testReproducibility();
        
        System.out.println();
        System.out.println("=".repeat(80));
        System.out.println("ALL TESTS COMPLETED");
        System.out.println("=".repeat(80));
    }
    
    /**
     * Test 1: Basic functionality - ensure no crashes and valid scores.
     */
    private static void testBasicFunctionality() {
        System.out.println("TEST 1: Basic Functionality");
        System.out.println("-".repeat(80));
        
        TeamProfile team1 = new TeamProfile("Team A", "TEA", TeamStrength.ELITE, 90);
        TeamProfile team2 = new TeamProfile("Team B", "TEB", TeamStrength.MEDIUM, 60);
        StrengthAwareScoreGenerator generator = new StrengthAwareScoreGenerator();
        
        boolean allValid = true;
        for (int i = 0; i < 100; i++) {
            Score score = generator.generateScore(team1, team2);
            
            if (score.getHomeGoals() < 0 || score.getHomeGoals() > 6 ||
                score.getAwayGoals() < 0 || score.getAwayGoals() > 6) {
                allValid = false;
                System.out.println("  ❌ Invalid score: " + score);
            }
        }
        
        if (allValid) {
            System.out.println("  ✅ All 100 scores valid (0-6 goals per team)");
        }
        System.out.println();
    }
    
    /**
     * Test 2: Score distribution - check if it matches real soccer statistics.
     */
    private static void testScoreDistribution() {
        System.out.println("TEST 2: Score Distribution");
        System.out.println("-".repeat(80));
        
        TeamProfile team1 = new TeamProfile("Team A", "TEA", TeamStrength.STRONG, 75);
        TeamProfile team2 = new TeamProfile("Team B", "TEB", TeamStrength.MEDIUM, 65);
        StrengthAwareScoreGenerator generator = new StrengthAwareScoreGenerator(42L);
        
        int totalMatches = 1000;
        int[] goalCounts = new int[7];
        int totalGoals = 0;
        
        for (int i = 0; i < totalMatches; i++) {
            Score score = generator.generateScore(team1, team2);
            goalCounts[score.getHomeGoals()]++;
            goalCounts[score.getAwayGoals()]++;
            totalGoals += score.getTotalGoals();
        }
        
        double avgGoalsPerMatch = (double) totalGoals / totalMatches;
        
        System.out.println("  Goal frequency distribution (1000 matches):");
        for (int goals = 0; goals < goalCounts.length; goals++) {
            double percentage = (goalCounts[goals] * 100.0) / (totalMatches * 2);
            System.out.printf("    %d goals: %4d times (%.1f%%)%n", goals, goalCounts[goals], percentage);
        }
        
        System.out.printf("%n  Average goals per match: %.2f%n", avgGoalsPerMatch);
        
        if (avgGoalsPerMatch >= 2.5 && avgGoalsPerMatch <= 3.2) {
            System.out.println("  ✅ Average within realistic range (2.5-3.2)");
        } else {
            System.out.println("  ⚠️  Average outside typical range (2.5-3.2)");
        }
        System.out.println();
    }
    
    /**
     * Test 3: Home advantage - verify home teams perform better.
     */
    private static void testHomeAdvantage() {
        System.out.println("TEST 3: Home Advantage");
        System.out.println("-".repeat(80));
        
        TeamProfile teamA = new TeamProfile("Team A", "TEA", TeamStrength.MEDIUM, 65);
        TeamProfile teamB = new TeamProfile("Team B", "TEB", TeamStrength.MEDIUM, 65);
        StrengthAwareScoreGenerator generator = new StrengthAwareScoreGenerator(123L);
        
        int totalMatches = 1000;
        int homeWins = 0, draws = 0, awayWins = 0;
        
        for (int i = 0; i < totalMatches; i++) {
            Score score = generator.generateScore(teamA, teamB);
            if (score.isHomeWin()) homeWins++;
            else if (score.isDraw()) draws++;
            else awayWins++;
        }
        
        double homeWinPct = (homeWins * 100.0) / totalMatches;
        double drawPct = (draws * 100.0) / totalMatches;
        double awayWinPct = (awayWins * 100.0) / totalMatches;
        
        System.out.printf("  Results (1000 matches between equal teams):%n");
        System.out.printf("    Home wins: %d (%.1f%%)%n", homeWins, homeWinPct);
        System.out.printf("    Draws:     %d (%.1f%%)%n", draws, drawPct);
        System.out.printf("    Away wins: %d (%.1f%%)%n", awayWins, awayWinPct);
        System.out.println();
        
        if (homeWinPct > awayWinPct) {
            System.out.println("  ✅ Home advantage evident (home wins > away wins)");
        } else {
            System.out.println("  ❌ Home advantage not working (home wins <= away wins)");
        }
        System.out.println();
    }
    
    /**
     * Test 4: Strength differentials - stronger teams should win more.
     */
    private static void testStrengthDifferentials() {
        System.out.println("TEST 4: Strength Differentials");
        System.out.println("-".repeat(80));
        
        TeamProfile elite = new TeamProfile("Elite Team", "ELI", TeamStrength.ELITE, 95);
        TeamProfile relegation = new TeamProfile("Weak Team", "WEA", TeamStrength.RELEGATION, 35);
        StrengthAwareScoreGenerator generator = new StrengthAwareScoreGenerator(456L);
        
        int totalMatches = 500;
        int homeWins = 0, draws = 0, awayWins = 0;
        int totalHomeGoals = 0, totalAwayGoals = 0;
        
        for (int i = 0; i < totalMatches; i++) {
            Score score = generator.generateScore(elite, relegation);
            if (score.isHomeWin()) homeWins++;
            else if (score.isDraw()) draws++;
            else awayWins++;
            
            totalHomeGoals += score.getHomeGoals();
            totalAwayGoals += score.getAwayGoals();
        }
        
        double homeWinPct = (homeWins * 100.0) / totalMatches;
        double avgHomeGoals = (double) totalHomeGoals / totalMatches;
        double avgAwayGoals = (double) totalAwayGoals / totalMatches;
        
        System.out.printf("  Elite (95) at home vs Relegation (35) away:%n");
        System.out.printf("    Elite wins: %d (%.1f%%)%n", homeWins, homeWinPct);
        System.out.printf("    Draws:      %d (%.1f%%)%n", draws, (draws * 100.0) / totalMatches);
        System.out.printf("    Weak wins:  %d (%.1f%%)%n", awayWins, (awayWins * 100.0) / totalMatches);
        System.out.printf("    Avg score:  %.2f - %.2f%n", avgHomeGoals, avgAwayGoals);
        System.out.println();
        
        if (homeWinPct >= 75 && avgHomeGoals >= 2.5) {
            System.out.println("  ✅ Elite team dominates (75%+ win rate, 2.5+ goals)");
        } else {
            System.out.println("  ⚠️  Elite team not dominant enough");
        }
        System.out.println();
    }
    
    /**
     * Test 5: Reproducibility - same seed should produce same results.
     */
    private static void testReproducibility() {
        System.out.println("TEST 5: Reproducibility");
        System.out.println("-".repeat(80));
        
        TeamProfile team1 = new TeamProfile("Team A", "TEA", TeamStrength.STRONG, 80);
        TeamProfile team2 = new TeamProfile("Team B", "TEB", TeamStrength.MEDIUM, 60);
        
        // Generate scores with same seed
        StrengthAwareScoreGenerator gen1 = new StrengthAwareScoreGenerator(999L);
        StrengthAwareScoreGenerator gen2 = new StrengthAwareScoreGenerator(999L);
        
        boolean allMatch = true;
        for (int i = 0; i < 20; i++) {
            Score score1 = gen1.generateScore(team1, team2);
            Score score2 = gen2.generateScore(team1, team2);
            
            if (!score1.equals(score2)) {
                allMatch = false;
                System.out.printf("  ❌ Mismatch at iteration %d: %s vs %s%n", i, score1, score2);
            }
        }
        
        if (allMatch) {
            System.out.println("  ✅ All 20 scores matched with same seed (reproducible)");
        }
        
        // Verify different seed produces different results
        StrengthAwareScoreGenerator gen3 = new StrengthAwareScoreGenerator(777L);
        Score scoreSeed1 = new StrengthAwareScoreGenerator(999L).generateScore(team1, team2);
        Score scoreSeed2 = gen3.generateScore(team1, team2);
        
        if (!scoreSeed1.equals(scoreSeed2)) {
            System.out.println("  ✅ Different seeds produce different results");
        } else {
            System.out.println("  ⚠️  Different seeds produced same result (rare but possible)");
        }
        
        System.out.println();
    }
}
